package dev.moru3.compsql

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.text.VARCHAR
import java.util.*

/**
 * データ型の管理を行うオブジェクトです。
 * このクラス内にある関数は目的がない限り使用しないことをおすすめします。
 */
object TypeHub: Set<DataType<*,*>> {
    private val typeCache = mutableMapOf<Class<*>, List<DataType<*,*>>>()

    private var dataTypeList = mutableSetOf<DataType<*,*>>()

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

    fun add(dataType: DataType<*,*>) { dataTypeList.add(dataType) }

    operator fun get(type: Class<*>): List<DataType<*,*>> {
        val result = typeCache[type]?:dataTypeList.filter { it.from == type }.sortedBy { it.priority }.also { typeCache[type]=it }
        if(result.isEmpty()) {
            if(type.isEnum) { return listOf(DataType.VARCHAR) }
        } else {
            return result
        }
        return listOf()
    }

    fun register(dataType: DataType<*,*>): DataType<*,*> {
        dataTypeList.add(dataType)
        return dataType
    }

    fun init() {}
}