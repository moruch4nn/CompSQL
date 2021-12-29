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
 * 新しくSQLiteのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class SQLiteConnection(path: String, override val timeout: Int = 10, val action: SQLiteConnection.()->Unit = {}): SQL(path, Properties()) {
    override fun init(url: String, properties: Properties) {
        try { Class.forName("org.sqlite.JDBC") } catch (_: Exception) { }
        // TODO
    }

    constructor(file: File, timeout: Int = 10, action: SQLiteConnection.()->Unit = {}): this("jdbc:sqlite:${file.absolutePath}", timeout, action)

    override fun select(table: String, vararg columns: String): Select = TODO()

    override fun delete(table: String): Delete = TODO()

    override fun insert(name: String): Insert = SQLiteInsert(SQLiteTable(this, name))

    override fun table(name: String): Table = SQLiteTable(this, name)

    override fun upsert(name: String): Upsert = TODO()

    override fun where(key: String): SelectKeyedWhere = TODO()

    init {  this.apply(action) }
}

fun main(args: Array<String>) {
    SQLiteConnection(File("database.db")) {
        table("test") {
            column("id", DataType.INTEGER).setPrimaryKey(true).setAutoIncrement(true).setNotNull(true).setZeroFill(true)
            column("name", DataType.TEXT).setNotNull(true).setDefaultValue("Non name")
            column("is_admin", DataType.BOOLEAN).setNotNull(true).setDefaultValue(false)
            column("passport_id", DataType.INTEGER).setNotNull(true).setDefaultValue(3480)
        }.send(false)

        insert("test") {
            add("name", "moru3_48")
        }.send(true)
    }
}