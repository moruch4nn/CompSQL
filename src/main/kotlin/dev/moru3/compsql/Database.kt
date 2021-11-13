package dev.moru3.compsql

import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.io.Closeable

interface Database: Closeable, Connection {

    val url: String

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
     * PK、UIをキーに、データが存在する場合update、存在しない場合insertします。
     */
    fun upsert(name: String, action: Upsert.()->Unit): Upsert

    /**
     * PK、UIをキーに、データが存在する場合update、存在しない場合insertします。
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

    fun add(cls: Class<*>): Table

    fun remove(instance: Any): Delete

    /**
     * Whereを元にデータベースからデータを取得します。
     */
    fun <T> get(type: Class<T>): List<T>

    fun <T> get(type: Class<T>, selectWhere: SelectWhere): List<T>

    /**
     * データベースからデータを取得します。
     */
    fun <T> get(type: Class<T>, action: Select.() -> Unit): List<T>

    fun where(key: String): SelectKeyedWhere

    /**
     * 接続が既に閉じているかを返します。
     */
    val isClosed: Boolean
}