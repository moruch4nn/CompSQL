package dev.moru3.compsql.syntax.table

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.QuerySendable
import dev.moru3.compsql.syntax.table.column.Column

interface AfterTable {

    val name: String

    /**
     * テーブルにColumnを追加します。
     */
    fun add(name: String, type: DataType<*>, action: (Column)->Unit): QuerySendable

    /**
     * テーブルにColumnを追加します。
     */
    fun add(column: Column): QuerySendable

    /**
     * Columnをリネームします。
     */
    fun rename(old: String, new: String): QuerySendable

    /**
     * Columnを削除します。
     */
    fun delete(name: String): QuerySendable
}