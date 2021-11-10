package dev.moru3.compsql.syntax.table

import dev.moru3.compsql.Connection
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.NonForceUpdateSendable
import dev.moru3.compsql.interfaces.QuerySendable
import dev.moru3.compsql.syntax.table.column.Column

interface AfterTable {

    val connection: Connection

    val name: String

    /**
     * テーブルにColumnを追加します。
     */
    fun add(name: String, type: DataType<*>, action: (Column)->Unit): NonForceUpdateSendable

    /**
     * テーブルにColumnを追加します。
     */
    fun add(column: Column): NonForceUpdateSendable

    /**
     * Columnをリネームします。
     */
    fun rename(old: String, new: String): NonForceUpdateSendable

    /**
     * Columnを削除します。
     */
    fun delete(name: String): NonForceUpdateSendable
}