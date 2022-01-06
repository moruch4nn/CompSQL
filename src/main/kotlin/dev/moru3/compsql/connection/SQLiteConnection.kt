package dev.moru3.compsql.connection

import dev.moru3.compsql.SQL
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.sqlite.update.insert.SQLiteInsert
import dev.moru3.compsql.sqlite.update.table.SQLiteTable
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * 新しくMariaDBのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 * @param url JDBCのURL。 例:jdbc:sqlite:
 * @param properties 接続する際に使用するプロパティ。
 * @param action Kotlin向けの高階関数。使用しない場合はnullを指定してください。
 */
class SQLiteConnection(path: String, properties: Properties, val action: SQLiteConnection.()->Unit = {}): SQL(path, properties) {
    override fun init(url: String, properties: Properties) {
        try { Class.forName("org.sqlite.JDBC") } catch (_: Exception) { }
        // TODO
    }

    constructor(file: File, properties: Properties?, action: SQLiteConnection.()->Unit = {}): this("jdbc:sqlite:${file.absolutePath}", properties?:Properties(), action)

    override fun select(table: String, vararg columns: String): Select = TODO()

    override fun delete(table: String): Delete = TODO()

    override fun insert(name: String): Insert = SQLiteInsert(SQLiteTable(this, name))

    override fun table(name: String): Table = SQLiteTable(this, name)

    override fun upsert(name: String): Upsert = TODO()

    override fun where(key: String): KeyedWhere = TODO()

    init {  this.apply(action) }
}