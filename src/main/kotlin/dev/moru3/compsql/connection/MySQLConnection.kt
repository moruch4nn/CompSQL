package dev.moru3.compsql.connection

import dev.moru3.compsql.SQL
import dev.moru3.compsql.mysql.query.select.MySQLSelect
import dev.moru3.compsql.mysql.query.select.MySQLSelectWhere
import dev.moru3.compsql.mysql.update.delete.MySQLDelete
import dev.moru3.compsql.mysql.update.insert.MySQLInsert
import dev.moru3.compsql.mysql.update.insert.MySQLUpsert
import dev.moru3.compsql.mysql.update.table.MySQLTable
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.sql.DriverManager
import java.util.*

/**
 * 新しくMySQLのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 * @param url jdbc:mysql://host/database
 */
open class MySQLConnection(url: String, properties: Properties, override val timeout: Int = 5, protected val action: MySQLConnection.()->Unit = {}): SQL(url, properties) {

    val database: String = url.split("/").last()

    override fun init(url: String, properties: Properties) {
        try { Class.forName("com.mysql.jdbc.Driver") } catch (_: Exception) { }
        val bacon = DriverManager.getConnection(url, properties)
        bacon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        bacon.close()
    }
    
    constructor(host: String, database: String, properties: Properties? = null, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mysql://${host}/${database}", properties?: Properties(), timeout, action)

    constructor(host: String, database: String, username: String, password: String, properties: Properties? = null, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this(host, database, (properties?:Properties()).also { it["user"] = username;it["password"] = password }, timeout, action)

    override fun select(table: String, vararg columns: String): Select = MySQLSelect(MySQLTable(this, table), *columns)

    override fun delete(table: String): Delete = MySQLDelete(MySQLTable(this, table))

    override fun insert(name: String): Insert = MySQLInsert(MySQLTable(this, name))

    override fun table(name: String): Table = MySQLTable(this, name)

    override fun upsert(name: String): Upsert = MySQLUpsert(MySQLTable(this, name))

    override fun where(key: String): SelectKeyedWhere = MySQLSelectWhere().key(key)

    init {  this.apply(action) }
}