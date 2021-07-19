package dev.moru3.compsql

import dev.moru3.compsql.table.Table
import java.sql.PreparedStatement

interface Insert {

    /**
     * テーブル名
     */
    val table: Table

    fun add(type: DataType<*, *>, key: String, value: Any): Insert

    fun add(key: String, value: Any): Insert

    fun build(force: Boolean): PreparedStatement

    /**
     * 変更を送信します。
     */
    fun send(force: Boolean)
}