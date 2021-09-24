package dev.moru3.compsql.connection

import java.sql.DriverManager

/**
 * 新しくMariaDBのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 */
class MariaDBConnection(url: String, username: String, password: String, properties: Map<String, Any>, override val timeout: Int = 5, action: MySQLConnection.()->Unit = {}): MySQLConnection(url, username, password, properties, timeout, action) {
    constructor(host: String, database: String, username: String, password: String, properties: Map<String, Any>? = null, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mariadb://${host}/${database}", username, password, properties?: mapOf(), timeout, action)

    override fun init() {
        val burl = url.replaceFirst(Regex("/${database}\$"), "").also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
        val bacon = DriverManager.getConnection(burl, username, password)
        bacon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        bacon.close()
        url = url.also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
    }
}