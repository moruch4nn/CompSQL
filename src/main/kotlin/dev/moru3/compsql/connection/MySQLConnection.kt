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
open class MySQLConnection(protected var url: String, protected val username: String, protected val password: String, protected val properties: Map<String, Any>, override val timeout: Int = 5, protected val action: MySQLConnection.()->Unit = {}): SQL() {

    val database: String = url.split("/").last()
    
    override fun init() {
        try { Class.forName("com.mysql.jdbc.Driver") } catch (_: Exception) { }
        val burl = url.replaceFirst(Regex("/${database}\$"), "").also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
        val bacon = DriverManager.getConnection(burl, username, password)
        bacon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        bacon.close()
        url = url.also{ properties.keys.mapIndexed { index, key -> "${if(index==0) '?' else '&'}${key}=${properties[key]}" }.forEach(it::plus) }
    }

    override fun after() {
        this.apply(action)
    }

    init { init() }
    
    constructor(host: String, database: String, username: String, password: String, properties: Map<String, Any>? = null, timeout: Int = 5, action: MySQLConnection.()->Unit = {}): this("jdbc:mysql://${host}/${database}", username, password, properties?: mapOf(), timeout, action)

    override val safeConnection: Connection get() = reconnect(false)

    override var connection: Connection = DriverManager.getConnection(url, username, password)
        protected set

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url, username, password)
        return connection
    }

    /**
     * 渡されたインスタンスに対応するテーブルを作成します。
     */
    override fun add(instance: Any): Table {
        return table(p0(instance::class.java)) {
            instance::class.java.declaredFields.forEach { field ->
                field.isAccessible = true
                if(Modifier.isStatic(field.modifiers)) { return@forEach }
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

    override fun select(table: String, vararg columns: String, action: Select.() -> Unit): Select = MySQLSelect(MySQLTable(this, table), *columns).apply(action)

    override fun delete(table: String): Delete = MySQLDelete(MySQLTable(this, table))

    override fun delete(table: String, action: Delete.() -> Unit): Delete = delete(table).apply(action)

    override fun remove(instance: Any): Delete {
        return delete(p0(instance::class.java)) {
            var filteredWhere: FilteredWhere? = null
            instance::class.java.declaredFields.forEach { field ->
                field.isAccessible = true
                if(field.annotations.filterIsInstance<IgnoreColumn>().isNotEmpty()) { return@forEach }
                val annotation = field.annotations.filterIsInstance<Column>().getOrNull(0)
                filteredWhere = filteredWhere?.and(annotation?.name?:field.name)?.equal(field.get(instance))?:this.where.key(annotation?.name?:field.name).equal(field.get(instance))
            }
        }
    }

    override fun <T> get(type: Class<T>, limit: Int): List<T> = get(type, limit) { }

    override fun <T> get(type: Class<T>, limit: Int, action: Select.() -> Unit): List<T> {
        val columns = mutableMapOf<String, Field>().also { columns ->
            type.declaredFields.forEach { field ->
                field.isAccessible = true
                if(Modifier.isStatic(field.modifiers)) { return@forEach }
                val annotations = field.annotations.filterIsInstance<IgnoreColumn>()
                if(annotations.isNotEmpty()&&annotations[0].ignoreGet) { return@forEach }
                val name = field.annotations.filterIsInstance<Column>().getOrNull(0)?.name?:field.name
                check(!columns.containsKey(name)) { "The column name is duplicated." }
                columns[name] = field
            }
        }
        val result = MySQLSelect(MySQLTable(this, p0(type)), *columns.keys.toTypedArray()).apply(action).send()
        val res = mutableListOf<T>()
        while(result.next()) {
            val instance = type.getConstructor().newInstance()?:throw Exception()
            columns.forEach { entry ->
                val dataType = DataHub.getTypeListByFromClass(entry.value.type).getOrNull(0)
                entry.value.set(instance, dataType?.get(result, entry.key))
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

    override fun where(key: String): SelectKeyedWhere = MySQLSelectWhere().key(key)

    override fun <T> get(type: Class<T>, selectWhere: SelectWhere, limit: Int): List<T> = get(type, limit) { where = selectWhere }

    init { after() }
}