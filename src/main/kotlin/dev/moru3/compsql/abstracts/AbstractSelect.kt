package dev.moru3.compsql.abstracts

import dev.moru3.compsql.Connection
import dev.moru3.compsql.syntax.OrderType
import dev.moru3.compsql.syntax.Select
import dev.moru3.compsql.syntax.SelectKeyedWhere
import dev.moru3.compsql.syntax.SelectWhere
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class AbstractSelect(val table: Table, vararg columns: String): Select {
    protected val columns = columns.toList()

    override val connection: Connection = table.connection

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }
    override fun send(): ResultSet = connection.sendQuery(build())
}