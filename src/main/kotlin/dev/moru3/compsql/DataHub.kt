package dev.moru3.compsql

import dev.moru3.compsql.datatype.DataType

object DataHub {
    private val typeCache = mutableMapOf<Class<*>, List<DataType<*>>>()

    private var dataTypeList = mutableSetOf<DataType<*>>()

    fun addCustomType(dataType: DataType<*>) { if(!dataTypeList.map { it::class.java }.any { it == dataType::class.java }) { dataTypeList.add(dataType) } }

    fun getDataTypeList(): List<DataType<*>> = dataTypeList.toMutableList()

    fun getTypeListByAny(any: Any): List<DataType<*>> = getTypeListByFromClass(any::class.java)

    fun getTypeListByFromClass(type: Class<*>): List<DataType<*>> = typeCache[type]?:dataTypeList.filter { it.from == type }.sortedBy { it.priority }.also { typeCache[type]=it }

    fun getTypeListByTypeClass(type: Class<*>): List<DataType<*>> = typeCache[type]?:dataTypeList.filter { it.type == type }.sortedBy { it.priority }.also { typeCache[type]=it }
}