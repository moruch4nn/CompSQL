package dev.moru3.compsql.connection

import dev.moru3.compsql.*
import dev.moru3.compsql.annotation.Column
import dev.moru3.compsql.annotation.IgnoreColumn
import dev.moru3.compsql.mysql.query.select.MySQLSelect
import dev.moru3.compsql.mysql.query.select.MySQLSelectWhere
import dev.moru3.compsql.mysql.update.delete.MySQLDelete
import dev.moru3.compsql.mysql.update.insert.MySQLInsert
import dev.moru3.compsql.mysql.update.insert.MySQLUpsert
import dev.moru3.compsql.mysql.update.table.MySQLTable
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.*
import java.sql.Connection

/**
 * 新しくMySQLのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 * @param url jdbc:mysql://host/database
 */
open class MySQLConnection(url: String, protected val username: String, protected val password: String, protected val properties: Map<String, Any>, override val timeout: Int = 5, protected val action: MySQLConnection.()->Unit = {}): SQL(url) {

    val database: String = url.split("/").last()
    
    override fun init() {
        try { Class.forName("com.mysql.jdbc.Driver") } catch (_: Exception) { }
        val burl = url.replaceFirst(Regex("/${database}\$"), "").also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
        val bacon = DriverManager.getConnection(burl, username, password)
        bacon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        bacon.close()
        url = url.also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
    }
    
    constructor(host: String, database: String, username: String, password: String, properties: Map<String, Any>? = null, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mysql://${host}/${database}", username, password, properties?: mapOf(), timeout, action)

    override fun select(table: String, vararg columns: String): Select = MySQLSelect(MySQLTable(this, table), *columns)

    override fun delete(table: String): Delete = MySQLDelete(MySQLTable(this, table))

    override fun insert(name: String): Insert = MySQLInsert(MySQLTable(this, name))

    override fun table(name: String): Table = MySQLTable(this, name)

    override fun upsert(name: String): Upsert = MySQLUpsert(MySQLTable(this, name))

    override fun where(key: String): SelectKeyedWhere = MySQLSelectWhere().key(key)

    init {  this.apply(action) }
}