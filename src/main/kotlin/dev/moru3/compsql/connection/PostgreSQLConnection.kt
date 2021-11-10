package dev.moru3.compsql.connection

import dev.moru3.compsql.*
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.sql.Connection
import java.sql.DriverManager

/**
 * 新しくPostgreSQLのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class PostgreSQLConnection(private var url: String, private val username: String, private val password: String, private val properties: Map<String, Any>, override val timeout: Int = 5, val action: PostgreSQLConnection.()->Unit = {}): SQL() {
    override fun init() {
        try { Class.forName("org.postgresql.Driver") } catch (_: Exception) { }
        // create database
        url.also{ properties.keys.mapIndexed { index, key -> url+="${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
    }

    init { init() }

    constructor(host: String, database: String, username: String, password: String, properties: Map<String, Any>, timeout: Int = 5, action: PostgreSQLConnection.()->Unit = {}): this("jdbc:postgresql://${host}/${database}", username, password, properties, timeout, action)

    override fun table(name: String, action: Table.() -> Unit): Table {
        TODO("Not yet implemented")
    }

    override fun after() {
        this.apply(action)
    }

    override fun table(name: String): Table {
        TODO("Not yet implemented")
    }

    override fun insert(name: String, action: Insert.() -> Unit): Insert {
        TODO("Not yet implemented")
    }

    override fun insert(name: String): Insert {
        TODO("Not yet implemented")
    }

    override fun upsert(name: String, action: Upsert.() -> Unit): Upsert {
        TODO("Not yet implemented")
    }

    override fun upsert(name: String): Upsert {
        TODO("Not yet implemented")
    }

    override fun select(table: String, vararg columns: String): Select {
        TODO("Not yet implemented")
    }

    override fun select(table: String, vararg columns: String, action: Select.() -> Unit): Select {
        TODO("Not yet implemented")
    }

    override fun put(instance: Any): Insert {
        TODO("Not yet implemented")
    }

    override fun putOrUpdate(instance: Any): Upsert {
        TODO("Not yet implemented")
    }

    override fun add(instance: Any): Table {
        TODO("Not yet implemented")
    }

    override fun <T> get(type: Class<T>, limit: Int, action: Select.() -> Unit): List<T> {
        TODO("Not yet implemented")
    }

    override fun delete(table: String): Delete {
        TODO("Not yet implemented")
    }

    override fun delete(table: String, action: Delete.() -> Unit): Delete {
        TODO("Not yet implemented")
    }

    override fun <T> get(type: Class<T>, limit: Int): List<T> {
        TODO("Not yet implemented")
    }

    override fun where(key: String): SelectKeyedWhere {
        TODO("Not yet implemented")
    }

    override fun <T> get(type: Class<T>, selectWhere: SelectWhere, limit: Int): List<T> {
        TODO("Not yet implemented")
    }

    override fun remove(instance: Any): Delete {
        TODO("Not yet implemented")
    }

    override var connection: Connection = DriverManager.getConnection(url, username, password)
        private set

    override val safeConnection: Connection get() = reconnect(false)

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url, username, password)
        return connection
    }
    init { after() }
}