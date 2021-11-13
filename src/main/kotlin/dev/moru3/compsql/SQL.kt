package dev.moru3.compsql

import dev.moru3.compsql.annotation.Column
import dev.moru3.compsql.annotation.IgnoreColumn
import dev.moru3.compsql.annotation.TableName
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.mysql.query.select.MySQLSelect
import dev.moru3.compsql.mysql.update.table.MySQLTable
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import java.io.InputStream
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.math.BigDecimal
import java.sql.*
import java.sql.Connection
import java.util.logging.Logger
import kotlin.concurrent.thread

abstract class SQL(final override var url: String): Database {

    init { init() }

    override var connection: Connection = DriverManager.getConnection(url)
        protected set

    val logger = Logger.getLogger(this::class.java.name)

    init {
        // load companion objects.
        DataType.VARCHAR
        Runtime.getRuntime().addShutdownHook(thread(start=false) { if(!isClosed) { close() } } )
    }

    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url)
        return connection
    }

    override val isClosed: Boolean get() = connection.isClosed||!connection.isValid(timeout)

    override fun close() {
        connection.close()
    }

    override val safeConnection: Connection get() = reconnect(false)

    protected open fun init() { }


    private fun setParamsToPrepareStatement(ps: PreparedStatement, vararg params: Any): PreparedStatement {
        params.forEachIndexed { index, any ->
            when(any) {
                is Boolean -> ps.setBoolean(index+1, any)
                is Byte -> ps.setByte(index+1, any)
                is Short -> ps.setShort(index+1, any)
                is Int -> ps.setInt(index+1, any)
                is Long -> ps.setLong(index+1, any)
                is Float -> ps.setFloat(index+1, any)
                is Double -> ps.setDouble(index+1, any)
                is BigDecimal -> ps.setBigDecimal(index+1, any)
                is String -> ps.setString(index+1, any)
                is ByteArray -> ps.setBytes(index+1, any)
                is Date -> ps.setDate(index+1, any)
                is Time -> ps.setTime(index+1, any)
                is Timestamp -> ps.setTimestamp(index+1, any)
                is InputStream -> ps.setAsciiStream(index+1, any)
                else -> ps.setObject(index+1, any)
            }
        }
        return ps
    }

    private fun p0(instance: Class<*>): String = instance.annotations.filterIsInstance<TableName>().getOrNull(0)?.name?:instance.simpleName

    fun p1(instance: Any): Map<String, Any> {
        return mutableMapOf<String, Any>().also { columns ->
            instance::class.java.declaredFields.forEach { field ->
                field.isAccessible = true
                if(Modifier.isStatic(field.modifiers)) { return@forEach }
                if(field.annotations.filterIsInstance<IgnoreColumn>().isNotEmpty()) { return@forEach }
                val name = field.annotations.filterIsInstance<Column>().getOrNull(0)?.name?:field.name
                check(!columns.containsKey(name)) { "The column name is duplicated." }
                columns[name] = field.get(instance)
            } }
    }

    override fun sendQuery(sql: String, vararg params: Any): ResultSet {
        safeConnection.prepareStatement(sql).also { ps -> return sendQuery(setParamsToPrepareStatement(ps, params)) }
    }

    override fun sendQuery(preparedStatement: PreparedStatement): ResultSet = ResultSet(preparedStatement.executeQuery())

    override fun sendUpdate(preparedStatement: PreparedStatement) {
        preparedStatement.also { ps -> ps.executeUpdate();ps.close() }
    }

    override fun sendUpdate(sql: String, vararg params: Any) {
        safeConnection.prepareStatement(sql).also { ps -> sendUpdate(setParamsToPrepareStatement(ps, params)) }
    }

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

    /**
     * 渡されたインスタンスに対応するテーブルを作成します。
     */
    override fun add(instance: Any): Table = add(instance::class.java)

    override fun add(cls: Class<*>): Table {
        return table(p0(cls)) {
            cls.declaredFields.forEach { field ->
                field.isAccessible = true
                if(Modifier.isStatic(field.modifiers)) { return@forEach }
                if(field.annotations.filterIsInstance<IgnoreColumn>().isNotEmpty()) { return@forEach }
                val annotation = field.annotations.filterIsInstance<Column>().getOrNull(0)
                column(annotation?.name?:field.name, TypeHub[field.type].getOrElse(0) {
                    if(field.type.isEnum) { DataType.VARCHAR } else { throw IllegalArgumentException() }
                }) {
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

    override fun <T> get(type: Class<T>): List<T> = get(type) { }

    override fun <T> get(type: Class<T>, selectWhere: SelectWhere): List<T> = get(type) { where = selectWhere }

    override fun <T> get(type: Class<T>, action: Select.() -> Unit): List<T> {
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
                val dataType = TypeHub[entry.value.type].getOrElse(0) {
                    if(entry.value.type.isEnum) {
                        entry.value.set(instance, entry.value.type::class.java.getMethod("valueOf").invoke(null, result.getString(entry.key)))
                    }
                    throw IllegalArgumentException()
                }
                entry.value.set(instance, dataType.get(result, entry.key))
            }
            res.add(instance)
        }
        return res
    }

    override fun select(table: String, vararg columns: String, action: Select.() -> Unit): Select = select(table, *columns).apply(action)

    override fun delete(table: String, action: Delete.() -> Unit): Delete = delete(table).apply(action)

    override fun insert(name: String, action: Insert.() -> Unit) = insert(name).apply(action)

    override fun table(name: String, action: Table.() -> Unit): Table = table(name).apply(action)

    override fun upsert(name: String, action: Upsert.() -> Unit): Upsert = upsert(name).apply(action)

    override fun put(instance: Any): Insert = this.insert(p0(instance::class.java)) { p1(instance).forEach { add(it.key, it.value) } }

    override fun putOrUpdate(instance: Any): Upsert = this.upsert(p0(instance::class.java)) { p1(instance).forEach { add(it.key, it.value) } }
}