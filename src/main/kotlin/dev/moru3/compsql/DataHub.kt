package dev.moru3.compsql

import dev.moru3.compsql.datatype.DataType

object DataHub {
    private var connection1: Database? = null

    private val typeCache = mutableMapOf<Class<*>, List<DataType<*, *>>>()

    fun setConnection(connection: Database) {
        // load companion objects.
        DataType.VARCHAR
        this.connection1?.close()
        this.connection1 = connection
    }

    val connection: Database get() = checkNotNull(connection1) { "No connection has been created." }

    private var dataTypeList = mutableSetOf<DataType<*, *>>()

    fun addCustomType(dataType: DataType<*, *>) { if(!dataTypeList.map { it::class.java }.any { it == dataType::class.java }) { dataTypeList.add(dataType) } }

    fun getDataTypeList(): List<DataType<*, *>> = dataTypeList.toMutableList()

    fun getTypeListByAny(any: Any): List<DataType<*, *>> = getTypeListByClass(any::class.java)

    fun getTypeListByClass(type: Class<*>): List<DataType<*, *>> = typeCache[type]?:dataTypeList.filter { it.from == type }.sortedBy { it.priority }.also { typeCache[type]=it }
}