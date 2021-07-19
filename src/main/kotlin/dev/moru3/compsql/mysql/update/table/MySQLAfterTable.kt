package dev.moru3.compsql.mysql.update.table

import dev.moru3.compsql.mysql.update.table.column.MySQLAfterColumn
import dev.moru3.compsql.table.AfterTable
import dev.moru3.compsql.table.column.AfterColumn

class MySQLAfterTable(override val name: String): AfterTable {
    override val column: AfterColumn = MySQLAfterColumn(this)
}