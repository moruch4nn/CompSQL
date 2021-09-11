package dev.moru3.compsql.abstract

import dev.moru3.compsql.DataHub
import dev.moru3.compsql.mysql.query.select.MySQLWhere
import dev.moru3.compsql.syntax.Delete
import dev.moru3.compsql.syntax.KeyedWhere
import dev.moru3.compsql.syntax.Where
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

abstract class AbstractDelete(val table: Table): Delete {
    override var where: Where = MySQLWhere()

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = DataHub.connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun send() = DataHub.connection.sendUpdate(build())

    override fun where(key: String): KeyedWhere = MySQLWhere().also { this.where = it }.key(key)

}