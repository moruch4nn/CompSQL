package dev.moru3.compsql.connection

import dev.moru3.compsql.*
import dev.moru3.compsql.table.Table
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

/**
 * 新しくSQLiteのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class SQLiteConnection(private val url: String, override val timeout: Int = 10, action: SQLiteConnection.()->Unit = {}): SQL() {

    constructor(file: File, timeout: Int = 10): this("jdbc:sqlite:${file.absolutePath}", timeout)

    override var connection: Connection = DriverManager.getConnection(url)
        private set

    override val safeConnection: Connection get() = reconnect(false)

    override fun upsert(name: String): Upsert {
        TODO("Not yet implemented")
    }

    override fun table(name: String, force: Boolean, action: Table.() -> Unit): Table {
        TODO("Not yet implemented")
    }

    override fun insert(name: String, force: Boolean): Insert {
        TODO("Not yet implemented")
    }

    override fun upsert(name: String, action: Upsert.() -> Unit): Upsert {
        TODO("Not yet implemented")
    }

    override fun insert(name: String, force: Boolean, action: Insert.() -> Unit): Insert {
        TODO("Not yet implemented")
    }

    override fun select(table: String, vararg columns: String): Select {
        TODO("Not yet implemented")
    }

    override fun select(table: String, vararg columns: String, action: Select.() -> Unit): Select {
        TODO("Not yet implemented")
    }

    override fun table(name: String, force: Boolean): Table {
        TODO("Not yet implemented")
    }

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url)
        return connection
    }

    override fun putOrUpdate(instance: Any) {
        TODO("Not yet implemented")
    }

    override fun add(instance: Any, force: Boolean) {
        TODO("Not yet implemented")
    }

    override fun put(instance: Any, force: Boolean) {
        TODO("Not yet implemented")
    }

    override fun <T> get(type: Class<T>, limit: Int): List<T> {
        TODO("Not yet implemented")
    }

    override fun <T> get(type: Class<T>, where: Where, limit: Int): List<T> {
        TODO("Not yet implemented")
    }

    init { this.apply(action);DataHub.setConnection(this) }
}