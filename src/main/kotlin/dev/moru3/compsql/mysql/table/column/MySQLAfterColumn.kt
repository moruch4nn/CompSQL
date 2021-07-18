package dev.moru3.compsql.mysql.table.column

import dev.moru3.compsql.DataType
import dev.moru3.compsql.table.AfterTable
import dev.moru3.compsql.table.column.AfterColumn
import dev.moru3.compsql.table.column.Column

class MySQLAfterColumn(override val afterTable: AfterTable): AfterColumn {
    override fun add(name: String, type: DataType<*>, action: (Column) -> Unit): AfterColumn {
        return add(MySQLColumn(name, type).apply(action))
    }

    override fun delete(name: String): AfterColumn {
        TODO("Not yet implemented")
    }

    override fun rename(old: String, new: String): AfterColumn {
        TODO("Not yet implemented")
    }

    override fun add(column: Column): AfterColumn {
        TODO("Not yet implemented")
    }
}