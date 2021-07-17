package dev.moru3.compsql

import dev.moru3.compsql.table.Table
import java.io.Closeable
import java.sql.Connection

interface CompSQL: Closeable, dev.moru3.compsql.Connection {

    val timeout: Int


    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が動悸されます。
     */
    fun table(table: Table, force: Boolean = false)

    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が動悸されます。
     */
    fun table(name: String, force: Boolean = false, action: Table.()->Unit = {})

    /**
     * 接続が既に閉じているか返します。
     */
    val isClosed: Boolean
}