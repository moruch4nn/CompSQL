package dev.moru3.compsql.connection

import java.sql.DriverManager
import java.util.*

/**
 * 新しくMariaDBのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class MariaDBConnection(url: String, properties: Properties, override val timeout: Int = 5, action: MySQLConnection.()->Unit = {}): MySQLConnection(url, properties, timeout, action) {
    constructor(host: String, database: String, properties: Properties? = null, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mariadb://${host}/${database}", properties?: Properties(), timeout, action)

    constructor(host: String, database: String, username: String, password: String, properties: Properties? = null, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this(host, database, (properties?:Properties()).also { it["user"] = username;it["password"] = password }, timeout, action)

    override fun init(url: String, properties: Properties) {
        try { Class.forName("org.mariadb.jdbc.Driver") } catch (_: Exception) { }
        val bacon = DriverManager.getConnection(url, properties)
        bacon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        bacon.close()
    }
}