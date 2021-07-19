package dev.moru3.compsql.connection

import dev.moru3.compsql.DataHub.Companion.setConnection
import dev.moru3.compsql.Database
import dev.moru3.compsql.Insert
import dev.moru3.compsql.mysql.update.table.MySQLTable
import dev.moru3.compsql.table.Table
import java.sql.*
import java.util.*

/**
 * 新しくMySQLのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
open class MySQLConnection(private val url: String, private val username: String, private val password: String, private val properties: Properties, override val timeout: Int = 5, action: MySQLConnection.()->Unit = {}): Database() {

    init { url.also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) } }

    constructor(host: String, database: String, username: String, password: String, properties: Properties, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mysql://${host}/${database}", username, password, properties, timeout, action)

    override val safeConnection: Connection get() = reconnect(false)

    override var connection: Connection = DriverManager.getConnection(url, username, password)
        protected set

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url, username, password)
        return connection
    }

    override fun table(table: Table, force: Boolean) {
        table.send(force)
    }

    override fun table(name: String, force: Boolean, action: Table.() -> Unit) {
        table(MySQLTable(this, name).apply(action), force)
    }

    override fun insert(name: String, force: Boolean, action: Insert.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun insert(insert: Insert) {
        TODO("Not yet implemented")
    }

    override fun upsert(name: String, force: Boolean, action: Table.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun upsert(name: String, force: Boolean, vararg values: Pair<String, Any>, action: Insert.() -> Unit) {
        TODO("Not yet implemented")
    }

    init { this.apply(action);setConnection(this) }
}