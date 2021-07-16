package dev.moru3.compsql.table

import dev.moru3.compsql.table.column.AfterColumn

interface AfterTable {

    val name: String

    val column: AfterColumn

    fun rename(old: String, new: String): AfterTable
}