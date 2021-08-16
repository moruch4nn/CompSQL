package dev.moru3.compsql.connection

import dev.moru3.compsql.*
import dev.moru3.compsql.DataHub.setConnection
import dev.moru3.compsql.annotation.Column
import dev.moru3.compsql.annotation.IgnoreColumn
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
        val ndburl = url.replaceFirst(Regex("/${database}\$"), "").also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
        val ndbcon = DriverManager.getConnection(ndburl, username, password)
        ndbcon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        ndbcon.close()
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

    override fun table(table: Table, force: Boolean) {
        table.also { it.send(force) }
    }

    override fun table(name: String, force: Boolean, action: Table.() -> Unit) {
        table(MySQLTable(this, name).apply(action), force)
    }

    override fun add(instance: Any, force: Boolean) {
        table(p0(instance::class.java), force) {
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

    override fun <T> get(type: Class<T>, limit: Int): List<T> {
        return get(type, MySQLWhere(), limit)
    }

    override fun <T> get(type: Class<T>, where: Where, limit: Int): List<T> {
        val tableName = p0(type)
        val columns = mutableMapOf<String, Field>().also { columns -> type.declaredFields.forEach { field -> field.isAccessible = true;if(field.annotations.filterIsInstance<IgnoreColumn>().isNotEmpty()) { return@forEach } ;val name = field.annotations.filterIsInstance<Column>().getOrNull(0)?.name?:field.name;check(!columns.containsKey(name)) { "The column name is duplicated." };columns[name] = field } }
        val raw = where.buildAsRaw()
        val query = safeConnection.prepareStatement("SELECT ${columns.map{it.key}.joinToString(", ")} FROM $tableName${raw.first} LIMIT $limit")
        raw.second.forEachIndexed { index, it -> it.second.set(query, index+1, it.first) }
        val result = sendQuery(query)
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

    override fun insert(name: String, force: Boolean, action: Insert.() -> Unit) {
        insert(MySQLInsert(MySQLTable(this, name)).apply(action), force)
    }

    override fun insert(insert: Insert, force: Boolean) {
        insert.also { it.send(force) }
    }

    override fun upsert(name: String, action: Upsert.() -> Unit) {
        upsert(MySQLUpsert(MySQLTable(this, name)).apply(action))
    }

    override fun put(instance: Any, force: Boolean) {
        this.insert(p0(instance::class.java), force) { p1(instance).forEach { add(it.key, it.value) } }
    }

    override fun putOrUpdate(instance: Any) {
        this.upsert(p0(instance::class.java)) { p1(instance).forEach { add(it.key, it.value) } }
    }

    override fun upsert(upsert: Upsert) {
        upsert.also { it.send() }
    }

    init { setConnection(this);this.apply(action) }
}