package dev.moru3.compsql

import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.io.Closeable

interface Database: Closeable, Connection {

    val url: String

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

    /**
     * SQLのSelect文を作成する際に使用する関数です。
     * select文はSQLのrowを取得する際に使用されます。
     * @param table selectに使用するテーブル名。
     * @param columns selectに使用するカラム名。
     */
    fun select(table: String, vararg columns: String): Select

    /**
     * SQLのSelect文を作成する際に使用する関数です。
     * select文はSQLのrowを取得する際に使用されます。
     * @param table selectに使用するテーブル名。
     * @param columns selectに使用するカラム名。
     * @param action Kotlin向けの引数。高階関数を使う際に使用します。
     */
    fun select(table: String, vararg columns: String, action: Select.()->Unit): Select

    /**
     * SQLのSelect文を作成する際に使用する関数です。
     * select文はSQLのrowを取得する際に使用されます。
     * @param table selectに使用するテーブル名。
    */
    fun delete(table: String): Delete

    /**
     * SQLのdelete文を作成する際に使用する関数です。
     * delete文はテーブル内のrowの削除に使用されます。
     * 注意: 必ずwhere文を使用してください。しなかった場合はすべてのrowが削除されます。
     * @param table 対象のテーブル名
     * @param action Kotlin向けの引数。高階関数を使う際に使用します。
     */
    fun delete(table: String, action: Delete.()->Unit): Delete

    /**
     * データベースにインスタンス内に定義された情報をinsertします。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param instance データベースにinsertする情報を含んだインスタンス。
     */
    fun put(instance: Any): Insert

    /**
     * データベースにインスタンス内に定義された情報をinsertします。
     * put(..)とは違い、PrimaryKeyが重複する場合はinsertせず、情報をアップデートします。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param instance データベースにinsertする情報を含んだインスタンス。
     */
    fun putOrUpdate(instance: Any): Upsert

    /**
     * データベースにインスタンス内に定義された情報のテーブルを作成する。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param instance データベースにinsertする情報を含んだインスタンス。
     */
    @Deprecated("add(Class<?>)を使用することをおすすめします。")
    fun add(instance: Any): Table

    /**
     * データベースにインスタンス内に定義された情報のテーブルを作成します。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param cls データベースにinsertする情報を含んだクラス。
     */
    fun add(cls: Class<*>): Table

    /**
     * データベースにインスタンス内に定義された情報のテーブルを作成します。
     * @TableName や @Column アノテーションをクラス内に定義することでテーブル名やカラム名を先に定義できます。
     * @param cls データベースにinsertする情報を含んだクラス。
     */
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