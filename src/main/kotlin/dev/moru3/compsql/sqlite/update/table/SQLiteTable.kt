package dev.moru3.compsql.sqlite.update.table

import dev.moru3.compsql.Connection
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.table.AfterTable
import dev.moru3.compsql.table.Table
import dev.moru3.compsql.table.column.Column
import java.sql.PreparedStatement

class SQLiteTable(val connection: Connection, n: String): Table {
    override var name: String = n
        set(value) {
            connection.sendUpdate("ALTER TABLE $field to $value")
            field = value
        }

    override fun column(name: String, type: DataType<*, *>, action: (Column) -> Unit): Column {
        TODO("Not yet implemented")
    }

    override fun column(name: String, type: DataType<*, *>): Column {
        TODO("Not yet implemented")
    }

    override val after: AfterTable
        get() = TODO("Not yet implemented")

    override fun build(force: Boolean): PreparedStatement {
        TODO("Not yet implemented")
    }

    override fun build(): PreparedStatement {
        TODO("Not yet implemented")
    }

    override fun buildAsRaw(force: Boolean): Pair<String, List<Any>> {
        TODO("Not yet implemented")
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*, *>>>> {
        TODO("Not yet implemented")
    }

    override fun send(force: Boolean) {
        TODO("Not yet implemented")
    }

}