package dev.moru3.compsql.connection

import dev.moru3.compsql.SQL
import dev.moru3.compsql.sqlite.update.insert.SQLiteInsert
import dev.moru3.compsql.sqlite.update.table.SQLiteTable
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.io.File
import java.util.*

/**
 * 新しくMariaDBのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 * @param properties 接続する際に使用するプロパティ。
 * @param action Kotlin向けの高階関数。使用しない場合はnullを指定してください。
 */
class SQLiteConnection(path: String, properties: Properties, val action: SQLiteConnection.()->Unit = {}): SQL(path, properties) {
    override fun init(url: String, properties: Properties) {
        try { Class.forName("org.sqlite.JDBC") } catch (_: Exception) { }
    }

    constructor(file: File, properties: Properties?, action: SQLiteConnection.()->Unit = {}): this("jdbc:sqlite:${file.absolutePath}", properties?:Properties(), action)

    override fun select(table_name: String, vararg columns: String): Select = TODO()

    override fun delete(table_name: String): Delete = TODO()

    override fun insert(table_name: String): Insert = SQLiteInsert(SQLiteTable(this, table_name))

    override fun table(table_name: String): Table = SQLiteTable(this, table_name)

    override fun upsert(table_name: String): Upsert = TODO()

    override fun where(key: String): KeyedWhere = TODO()

    init {  this.apply(action) }
}