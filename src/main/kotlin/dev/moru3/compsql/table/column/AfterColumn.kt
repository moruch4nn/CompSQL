package dev.moru3.compsql.table.column

import dev.moru3.compsql.DataType
import dev.moru3.compsql.table.AfterTable

interface AfterColumn {

    val afterTable: AfterTable

    fun add(name: String, type: DataType<*>, action: (Column)->Unit): AfterColumn

    fun rename(old: String, new: String): AfterColumn

    fun delete(name: String): AfterColumn
}