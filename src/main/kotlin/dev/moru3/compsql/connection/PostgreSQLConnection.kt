package dev.moru3.compsql.connection

import dev.moru3.compsql.Database
import dev.moru3.compsql.table.Table
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

class PostgreSQLConnection(private val url: String, private val username: String, private val password: String, private val properties: Properties, override val timeout: Int = 5, action: PostgreSQLConnection.()->Unit = {}): Database() {
    init { url.also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) } }

    constructor(host: String, database: String, username: String, password: String, properties: Properties, timeout: Int = 5, action: PostgreSQLConnection.()->Unit = {}): this("jdbc:postgresql://${host}/${database}", username, password, properties, timeout, action)

    override var connection: Connection = DriverManager.getConnection(url, username, password)
        private set

    override fun reconnect(force: Boolean) {
        if(this.isClosed) connection.close()
        connection = DriverManager.getConnection(url, username, password)
    }

    override fun table(table: Table, force: Boolean) {
        TODO("Not yet implemented")
    }

    override fun table(name: String, force: Boolean, action: Table.() -> Unit) {
        TODO("Not yet implemented")
    }

    init { this.apply(action) }
}