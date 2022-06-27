package dev.moru3.compsql

import dev.moru3.compsql.annotation.Column
import dev.moru3.compsql.annotation.IgnoreColumn
import dev.moru3.compsql.annotation.TableName
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.mysql.query.select.MySQLSelect
import dev.moru3.compsql.mysql.update.table.MySQLTable
import dev.moru3.compsql.syntax.*
import dev.moru3.compsql.syntax.table.Table
import sun.reflect.ReflectionFactory
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*
import java.util.logging.Logger
import kotlin.concurrent.thread

abstract class SQL(final override var url: String, val properties: Properties): Database {
    init { init(url, properties) }

    /**
     * interfaceを参照。
     */
    override var connection: Connection = DriverManager.getConnection(url, properties)
        protected set
    private val reflectionFactory = ReflectionFactory.getReflectionFactory()

    val logger = Logger.getLogger(this::class.javaObjectType.name)

    init {
        // load companion objects.
        TypeHub.init()
        Runtime.getRuntime().addShutdownHook(thread(start=false) { if(!isClosed) { close() } } )
    }

    /**
     * interfaceを参照。
     */
    override fun reconnect(force: Boolean): Connection {
        if(!this.isClosed) if(force) connection.close() else return connection
        connection = DriverManager.getConnection(url, properties)
        return connection
    }

    /**
     * interfaceを参照。
     */
    override val isClosed: Boolean get() = connection.isClosed||!connection.isValid(connection.networkTimeout)

    /**
     * interfaceを参照。
     */
    override fun close() {
        connection.close()
    }

    /**
     * interfaceを参照。
     */
    override val safeConnection: Connection get() = reconnect(false)

    /**
     * connectionが作成される前に呼び出される関数です。
     * databaseの初期化、作成などに使用されます。
     * 新しくコネクションクラスを作成する場合はこれをoverrideしてください。
     */
    protected open fun init(url: String, properties: Properties) { }


    /**
     * PRIVATE
     * PreparedStatementにparamsの方に応じた方法でsetを行います。
     * nullを指定できないため使用する場合はDataType.
     */
    private fun setParamsToPrepareStatement(ps: PreparedStatement, vararg params: Any): PreparedStatement {
        params.forEachIndexed { index, any ->
            ps.setObject(index+1, any)
        }
        return ps
    }

    private fun p0(instance: Class<*>): String = instance.annotations.filterIsInstance<TableName>().getOrNull(0)?.name?:instance.simpleName

    fun p1(instance: Any): Map<String, Any> {
        return mutableMapOf<String, Any>().also { columns ->
            instance::class.javaObjectType.declaredFields.forEach { field ->
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

    override fun sendQuery(preparedStatement: PreparedStatement): ResultSet = preparedStatement.executeQuery()

    override fun sendUpdate(preparedStatement: PreparedStatement) {
        preparedStatement.also { ps -> ps.executeUpdate();ps.close() }
    }

    override fun sendUpdate(sql: String, vararg params: Any) {
        safeConnection.prepareStatement(sql).also { ps -> sendUpdate(setParamsToPrepareStatement(ps, params)) }
    }

    override fun remove(instance: Any): Delete {
        return delete(p0(instance::class.javaObjectType)) {
            var filteredWhere: FilteredWhere? = null
            instance::class.javaObjectType.declaredFields.forEach { field ->
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
    override fun add(instance: Any): Table = add(instance::class.javaObjectType)

    override fun add(cls: Class<*>): Table {
        return table(p0(cls)) {
            cls.declaredFields.forEach { field ->
                field.isAccessible = true
                if(Modifier.isStatic(field.modifiers)) { return@forEach }
                if(field.annotations.filterIsInstance<IgnoreColumn>().isNotEmpty()) { return@forEach }
                val annotation = field.annotations.filterIsInstance<Column>().getOrNull(0)
                column(annotation?.name?:field.name, TypeHub[field.type.kotlin.javaObjectType].getOrElse(0) {
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

    override fun <T> get(type: Class<T>): Database.GetQuery<T> = get(type) { }

    override fun <T> get(type: Class<T>, where: FirstWhere): Database.GetQuery<T> = get(type) { this.where = where }

    override fun <T> get(type: Class<T>, action: Select.() -> Unit): Database.GetQuery<T> {
        return object: Database.GetQuery<T> {
            override fun send(): List<T> {
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
                val result = MySQLSelect(MySQLTable(this@SQL, p0(type)), *columns.keys.toTypedArray()).apply(action).send()
                val res = mutableListOf<T>()
                while(result.next()) {
                    val instance = type.cast(reflectionFactory.newConstructorForSerialization(type,Any::class.java.getDeclaredConstructor()).newInstance())
                    columns.forEach { entry ->
                        if(entry.value.type.isEnum) {
                            entry.value.set(instance, entry.value.type.getMethod("valueOf", String::class.java).invoke(null, result.getString(entry.key)))
                        } else if(entry.value.type==UUID::class.javaObjectType) {
                            entry.value.set(instance, UUID.fromString(result.getString(entry.key)))
                        } else {
                            val dataType =
                                TypeHub[entry.value.type.kotlin.javaObjectType].getOrElse(0) { throw IllegalArgumentException() }
                            entry.value.set(instance, dataType.get(result, entry.key))
                        }
                    }
                    res.add(instance)
                }
                return res
            }
        }
    }

    override fun select(table_name: String, vararg columns: String, action: Select.() -> Unit): Select = select(table_name, *columns).apply(action)

    override fun delete(table_name: String, action: Delete.() -> Unit): Delete = delete(table_name).apply(action)

    override fun insert(table_name: String, action: Insert.() -> Unit) = insert(table_name).apply(action)

    override fun table(table_name: String, action: Table.() -> Unit): Table = table(table_name).apply(action)

    override fun upsert(table_name: String, action: Upsert.() -> Unit): Upsert = upsert(table_name).apply(action)

    override fun put(instance: Any): Insert = this.insert(p0(instance::class.javaObjectType)) { p1(instance).forEach { add(it.key, it.value) } }

    override fun putOrUpdate(instance: Any): Upsert = this.upsert(p0(instance::class.javaObjectType)) { p1(instance).forEach { add(it.key, it.value) } }
}