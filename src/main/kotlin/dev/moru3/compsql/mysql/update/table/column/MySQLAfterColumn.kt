package dev.moru3.compsql.mysql.update.table.column

import dev.moru3.compsql.DataHub.Companion.connection
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.table.AfterTable
import dev.moru3.compsql.table.column.AfterColumn
import dev.moru3.compsql.table.column.Column

class MySQLAfterColumn(override val afterTable: AfterTable): AfterColumn {
    override fun add(name: String, type: DataType<*, *>, action: (Column) -> Unit): AfterColumn {
        return add(MySQLColumn(name, type).apply(action))
    }

    override fun delete(name: String): AfterColumn {
        connection.sendUpdate("ALTER TABLE ${afterTable.name} DROP COLUMN $name")
        return this
    }

    override fun rename(old: String, new: String): AfterColumn {
        connection.sendUpdate("ALTER TABLE ${afterTable.name} RENAME COLUMN $old TO $new")
        return this
    }

    override fun add(column: Column): AfterColumn {
        connection.sendUpdate("ALTER TABLE ${afterTable.name} ADD (${column.buildAsRaw()})")
        return this
    }
}