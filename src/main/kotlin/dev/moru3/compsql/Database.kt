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
     * TODO 説明文書いといて
     */
    fun upsert(name: String, action: Upsert.()->Unit): Upsert

    /**
     * TODO 説明文書いといて
     */
    fun upsert(name: String): Upsert

    fun select(table: String, vararg columns: String): Select

    fun select(table: String, vararg columns: String, action: Select.()->Unit): Select

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