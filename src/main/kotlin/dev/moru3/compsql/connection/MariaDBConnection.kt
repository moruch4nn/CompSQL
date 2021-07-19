package dev.moru3.compsql.connection

import java.util.*

/**
 * 新しくMariaDBのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class MariaDBConnection(url: String, private val username: String, private val password: String, private val properties: Properties, override val timeout: Int = 5, action: MySQLConnection.()->Unit = {}): MySQLConnection(url, username, password, properties, timeout, action) {
    constructor(host: String, database: String, username: String, password: String, properties: Properties, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mariadb://${host}/${database}", username, password, properties, timeout, action)
}