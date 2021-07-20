package dev.moru3.compsql.table.column

import dev.moru3.compsql.DataType
import dev.moru3.compsql.table.AfterTable

interface AfterColumn {

    val afterTable: AfterTable

    /**
     * テーブルにColumnを追加します。
     */
    fun add(name: String, type: DataType<*, *>, action: (Column)->Unit): AfterColumn

    /**
     * テーブルにColumnを追加します。
     */
    fun add(column: Column): AfterColumn

    /**
     * Columnをリネームします。
     */
    fun rename(old: String, new: String): AfterColumn

    /**
     * Columnを削除します。
     */
    fun delete(name: String): AfterColumn
}