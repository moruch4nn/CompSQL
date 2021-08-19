package dev.moru3.compsql

import dev.moru3.compsql.table.Table
import java.io.Closeable

interface Database: Closeable, Connection {

    val timeout: Int

    /**
     * テーブルを作成します。
     */
    fun table(name: String, force: Boolean = false, action: Table.()->Unit): Table

    /**
     * テーブルを作成します。
     */
    fun table(name: String, force: Boolean = false): Table

    /**
     * データをinsertします。
     */
    fun insert(name: String, force: Boolean, action: Insert.() -> Unit): Insert

    /**
     * データをinsertします。
     */
    fun insert(name: String, force: Boolean): Insert

    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun upsert(name: String, action: Upsert.()->Unit): Upsert

    /**
     * テーブルを作成します。また、自動的にSQLに変更内容が同期されます。
     */
    fun upsert(name: String): Upsert

    /**
     * Databaseにデータをプットします。
     */
    fun put(instance: Any, force: Boolean)

    /**
     * 重複しないように
     */
    fun putOrUpdate(instance: Any)

    /**
     * テーブルを追加します。
     */
    fun add(instance: Any, force: Boolean)

    /**
     * Whereを元にデータベースからデータを取得します。
     */
    fun <T> get(type: Class<T>, where: Where, limit: Int = Int.MAX_VALUE): List<T>

    /**
     * データベースからデータを取得します。
     */
    fun <T> get(type: Class<T>, limit: Int = Int.MAX_VALUE): List<T>

    /**
     * 接続が既に閉じているかを返します。
     */
    val isClosed: Boolean
}