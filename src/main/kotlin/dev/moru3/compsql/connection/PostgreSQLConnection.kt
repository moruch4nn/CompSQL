package dev.moru3.compsql.connection

import dev.moru3.compsql.SQL
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

/**
 * 新しくSQLiteのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class PostgreSQLConnection(url: String, override val timeout: Int = 10, val action: PostgreSQLConnection.()->Unit = {}): SQL(url) {
    override fun init() {
        try { Class.forName("org.postgresql.Driver") } catch (_: Exception) { }
        // TODO
    }

    init { init() }

    constructor(file: File, timeout: Int = 10, action: PostgreSQLConnection.()->Unit = {}): this("jdbc:postgresql:${file.absolutePath}", timeout, action)

    override fun select(table: String, vararg columns: String): Select = TODO()

    override fun delete(table: String): Delete = TODO()

    override fun insert(name: String): Insert = TODO()

    override fun table(name: String): Table = TODO()

    override fun upsert(name: String): Upsert = TODO()

    override fun where(key: String): SelectKeyedWhere = TODO()

    init {  this.apply(action) }
}