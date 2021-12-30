package dev.moru3.compsql

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.text.VARCHAR
import java.util.*

object TypeHub: Set<DataType<*,*>> {
    private val typeCache = mutableMapOf<Class<*>, List<DataType<*,*>>>()

    private var dataTypeList = mutableSetOf<DataType<*,*>>()

    fun add(dataType: DataType<*,*>) { dataTypeList.add(dataType) }

    operator fun get(type: Class<*>): List<DataType<*,*>> {
        return typeCache[type]?:dataTypeList.filter { it.from == type }.sortedBy { it.priority }.also { typeCache[type]=it }
    }

    override val size: Int
        get() = typeCache.size

    override fun contains(element: DataType<*,*>): Boolean = dataTypeList.contains(element)

    override fun containsAll(elements: Collection<DataType<*,*>>): Boolean = dataTypeList.containsAll(elements)

    override fun isEmpty(): Boolean = dataTypeList.isEmpty()

    override fun iterator(): Iterator<DataType<*,*>> = dataTypeList.iterator()

    init{
        typeCache[UUID::class.javaObjectType] = listOf(VARCHAR(36))
        typeCache[Enum::class.javaObjectType] = listOf(DataType.VARCHAR)
    }
}