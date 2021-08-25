package dev.moru3.compsql

import dev.moru3.compsql.table.Table
import java.io.Closeable

interface Database: Closeable, Connection {

    val timeout: Int

    /**
     * テーブルを作成します。
     */
    fun table(name: String, action: Table.()->Unit): Table

    /**
     * テーブルを作成します。
     */
    fun table(name: String): Table

    /**
     * データをinsertします。
     */
    fun insert(name: String, action: Insert.() -> Unit): Insert

    /**
     * データをinsertします。
     */
    fun insert(name: String): Insert

    /**
     * TODO 説明文書いといて
     */
    fun upsert(name: String, action: Upsert.()->Unit): Upsert

    /**
     * TODO 説明文書いといて
     */
    fun upsert(name: String): Upsert

    fun select(table: String, vararg columns: String): Select

    fun select(table: String, vararg columns: String, action: Select.()->Unit): Select

    fun delete(table: String): Delete

    fun delete(table: String, action: Delete.()->Unit): Delete

    /**
     * Databaseにデータをプットします。
     */
    fun put(instance: Any): Insert

    /**
     * 重複しないように
     */
    fun putOrUpdate(instance: Any): Upsert

    /**
     * テーブルを追加します。
     */
    fun add(instance: Any): Table

    fun remove(instance: Any): Delete

    /**
     * Whereを元にデータベースからデータを取得します。
     */
    fun <T> get(type: Class<T>, limit: Int = Int.MAX_VALUE): List<T>

    /**
     * データベースからデータを取得します。
     */
    fun <T> get(type: Class<T>, limit: Int = Int.MAX_VALUE, action: Select.() -> Unit): List<T>

    /**
     * 接続が既に閉じているかを返します。
     */
    val isClosed: Boolean
}