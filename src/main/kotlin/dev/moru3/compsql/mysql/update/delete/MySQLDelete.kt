package dev.moru3.compsql.mysql.update.delete

import dev.moru3.compsql.*
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.mysql.query.select.MySQLWhere
import dev.moru3.compsql.table.Table
import java.sql.PreparedStatement
import java.sql.ResultSet

class MySQLDelete(val table: Table): Delete {

     override var where: Where = MySQLWhere()

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = DataHub.connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*, *>>>> {
        buildString {
            val raw = where.buildAsRaw()
            append("DELETE FROM ${table.name}${raw.first}")
            return this.toString() to raw.second
        }
    }

    override fun send() = DataHub.connection.sendUpdate(build())

    override fun where(key: String): KeyedWhere = MySQLWhere().also { this.where = it }.key(key)
}