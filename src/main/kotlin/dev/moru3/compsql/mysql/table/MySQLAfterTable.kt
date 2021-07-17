package dev.moru3.compsql.mysql.table

import dev.moru3.compsql.table.AfterTable
import dev.moru3.compsql.table.column.AfterColumn

class MySQLAfterTable(override val name: String): AfterTable {
    override val column: AfterColumn = TODO()

    override fun rename(old: String, new: String): AfterTable {
        TODO("Not yet implemented")
    }
}