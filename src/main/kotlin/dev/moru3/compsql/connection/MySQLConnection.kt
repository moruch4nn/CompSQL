package dev.moru3.compsql.connection

import dev.moru3.compsql.Database
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

open class MySQLConnection(private val url: String, private val username: String, private val password: String, private val properties: Properties, override val timeout: Int = 5, action: MySQLConnection.()->Unit = {}): Database() {

    init { url.also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) } }

    constructor(host: String, database: String, username: String, password: String, properties: Properties, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mysql://${host}/${database}", username, password, properties, timeout, action)

    override var connection: Connection = DriverManager.getConnection(url, username, password)
        protected set

    override fun reconnect(force: Boolean) {
        if(this.isClosed) connection.close()
        connection = DriverManager.getConnection(url, username, password)
    }

    init { this.apply(action) }
}