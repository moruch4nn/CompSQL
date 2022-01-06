package dev.moru3.compsql.abstracts

import dev.moru3.compsql.Connection
import dev.moru3.compsql.syntax.Delete
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

abstract class AbstractDelete(val table: Table): Delete {
    override val connection: Connection = table.connection

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun send() = connection.sendUpdate(build())
}