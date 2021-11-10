package dev.moru3.compsql.sqlite.update.table

import dev.moru3.compsql.Connection
import dev.moru3.compsql.abstracts.AbstractTable
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.table.AfterTable
import dev.moru3.compsql.syntax.table.Table
import dev.moru3.compsql.syntax.table.column.Column
import java.sql.PreparedStatement

class SQLiteTable(connection: Connection, n: String): AbstractTable(connection, n) {
    override fun buildAsRaw(force: Boolean): Pair<String, List<Pair<Any?, DataType<*>>>> {
        TODO("Not yet implemented")
    }
}