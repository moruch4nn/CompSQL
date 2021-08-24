package dev.moru3.compsql.connection

import dev.moru3.compsql.*
import dev.moru3.compsql.DataHub.setConnection
import dev.moru3.compsql.annotation.Column
import dev.moru3.compsql.annotation.IgnoreColumn
import dev.moru3.compsql.mysql.query.select.MySQLSelect
import dev.moru3.compsql.mysql.query.select.MySQLWhere
import dev.moru3.compsql.mysql.update.insert.MySQLInsert
import dev.moru3.compsql.mysql.update.insert.MySQLUpsert
import dev.moru3.compsql.mysql.update.table.MySQLTable
import dev.moru3.compsql.table.Table
import java.lang.reflect.Field
import java.sql.*
import java.sql.Connection

/**
 * 新しくMySQLのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 * @param url jdbc:mysql://host/database
 */
open class MySQLConnection(private var url: String, private val username: String, private val password: String, private val properties: Map<String, Any>, override val timeout: Int = 5, action: MySQLConnection.()->Unit = {}): SQL() {

    val database: String = url.split("/").last()

    init {
        val burl = url.replaceFirst(Regex("/${database}\$"), "").also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
        val bacon = DriverManager.getConnection(burl, username, password)
        bacon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        bacon.close()
    }

    init { url = url.also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) } }

    constructor(host: String, database: String, username: String, password: String, properties: Map<String, Any>, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mysql://${host}/${database}", username, password, properties, timeout, action)

    override val safeConnection: Connection get() = reconnect(false)

    override var connection: Connection = DriverManager.getConnection(url, username, password)
        protected set

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url, username, password)
        return connection
    }

    override fun add(instance: Any): Table {
        return table(p0(instance::class.java)) {
            instance::class.java.declaredFields.forEach { field ->
                field.isAccessible = true
                if(field.annotations.filterIsInstance<IgnoreColumn>().isNotEmpty()) { return@forEach }
                val annotation = field.annotations.filterIsInstance<Column>().getOrNull(0)
                column(annotation?.name?:field.name, checkNotNull(DataHub.getTypeListByAny(field.get(instance)).getOrNull(0))) {
                    if(annotation==null) { return@column }
                    it.setZeroFill(annotation.isZeroFill)
                    it.setAutoIncrement(annotation.isAutoIncrement)
                    it.setNotNull(annotation.isNotNull)
                    it.setPrimaryKey(annotation.isPrimaryKey)
                    it.setUniqueIndex(annotation.isUniqueIndex)
                }
            }
        }
    }

    override fun select(table: String, vararg columns: String): Select = MySQLSelect(MySQLTable(this, table), *columns)

    override fun select(table: String, vararg columns: String, action: Select.() -> Unit): Select  = MySQLSelect(MySQLTable(this, table), *columns).apply(action)

    override fun <T> get(type: Class<T>, limit: Int): List<T> = get(type, MySQLWhere(), limit)

    override fun <T> get(type: Class<T>, where: Where, limit: Int): List<T> {
        val columns = mutableMapOf<String, Field>().also { columns -> type.declaredFields.forEach { field -> field.isAccessible = true;if(field.annotations.filterIsInstance<IgnoreColumn>().isNotEmpty()) { return@forEach } ;val name = field.annotations.filterIsInstance<Column>().getOrNull(0)?.name?:field.name;check(!columns.containsKey(name)) { "The column name is duplicated." };columns[name] = field } }
        val result = MySQLSelect(MySQLTable(this, p0(type)), where, *columns.keys.toTypedArray()).send()
        val res = mutableListOf<T>()
        while(result.next()) {
            val instance = type.newInstance()
            columns.forEach {
                val dataType = DataHub.getTypeListByAny(it.value.type).getOrNull(0)
                it.value.set(instance, dataType?.get(result, it.key))
            }
            res.add(instance)
        }
        return res
    }

    override fun insert(name: String, action: Insert.() -> Unit) = insert(name).apply(action)

    override fun insert(name: String): Insert = MySQLInsert(MySQLTable(this, name))

    override fun table(name: String, action: Table.() -> Unit): Table = table(name).apply(action)

    override fun table(name: String): Table = MySQLTable(this, name)

    override fun upsert(name: String): Upsert = MySQLUpsert(MySQLTable(this, name))

    override fun upsert(name: String, action: Upsert.() -> Unit): Upsert = upsert(name).apply(action)

    override fun put(instance: Any): Insert = this.insert(p0(instance::class.java)) { p1(instance).forEach { add(it.key, it.value) } }

    override fun putOrUpdate(instance: Any): Upsert = this.upsert(p0(instance::class.java)) { p1(instance).forEach { add(it.key, it.value) } }

    init { setConnection(this);this.apply(action) }
}