package dev.moru3.compsql

import dev.moru3.compsql.datatype.DataType

object DataHub {
    private var connection1: SQL? = null

    fun setConnection(connection: SQL) {

        // load companion objects.
        DataType.VARCHAR

        this.connection1 = connection
    }

    val connection: SQL get() = checkNotNull(connection1) { "No connection has been created." }

    private var dataTypeList = mutableSetOf<DataType<*, *>>()

    fun addCustomType(dataType: DataType<*, *>) { if(!dataTypeList.map { it::class.java }.any { it == dataType::class.java }) { dataTypeList.add(dataType) } }

    fun getDataTypeList(): List<DataType<*, *>> = dataTypeList.toMutableList()

    fun getTypeListByAny(any: Any): List<DataType<*, *>> {
        return dataTypeList.filter { it.type.isInstance(any) }.sortedBy { it.priority }
    }
}