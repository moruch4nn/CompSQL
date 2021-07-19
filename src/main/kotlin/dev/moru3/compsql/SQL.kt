package dev.moru3.compsql

import dev.moru3.compsql.table.Table
import java.io.Closeable

interface SQL: Closeable, Connection {

    val timeout: Int


    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun table(table: Table, force: Boolean = false)

    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun table(name: String, force: Boolean = false, action: Table.()->Unit = {})

    /**
     * データをinsertします。
     */
    fun insert(name: String, force: Boolean, action: Insert.() -> Unit)

    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun upsert(name: String, force: Boolean = false, action: Table.()->Unit = {})

    /**
     * データをinsertします。
     */
    fun upsert(name: String, force: Boolean, vararg values: Pair<String, Any> = arrayOf(), action: Insert.() -> Unit)

    /**
     * データをinsertします。
     */
    fun insert(insert: Insert)

    /**
     * 接続が既に閉じているかを返します。
     */
    val isClosed: Boolean
}