package dev.moru3.compsql

import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.date.DATETIME
import dev.moru3.compsql.datatype.types.text.VARCHAR
import java.util.*

/**
 * データ型の管理を行うオブジェクトです。
 * このクラス内にある関数は目的がない限り使用しないことをおすすめします。
 */
object TypeHub: Set<BaseDataType<*,*>> {
    private val typeCache = mutableMapOf<Class<*>, List<BaseDataType<*,*>>>()

    private var dataTypeList = mutableSetOf<BaseDataType<*,*>>()

    override val size: Int
        get() = typeCache.size

    override fun contains(element: BaseDataType<*,*>): Boolean = dataTypeList.contains(element)

    override fun containsAll(elements: Collection<BaseDataType<*,*>>): Boolean = dataTypeList.containsAll(elements)

    override fun isEmpty(): Boolean = dataTypeList.isEmpty()

    override fun iterator(): Iterator<BaseDataType<*,*>> = dataTypeList.iterator()

    fun add(dataType: BaseDataType<*,*>) { dataTypeList.add(dataType) }

    operator fun get(type: Class<*>): List<BaseDataType<*,*>> {
        val result = typeCache[type]?:dataTypeList.filter { it.from == type }.sortedBy { it.priority }.also { typeCache[type]=it }
        if(result.isEmpty()) {
            if(type.isEnum) { return listOf(DataType.VARCHAR) }
        } else {
            return result
        }
        return listOf()
    }

    fun <T: BaseDataType<*,*>> register(dataType: T): T {
        dataTypeList.add(dataType)
        return dataType
    }

    init{
        Class.forName("dev.moru3.compsql.datatype.DataType").fields
        typeCache[UUID::class.javaObjectType] = listOf(VARCHAR(36))
        typeCache[Enum::class.javaObjectType] = listOf(DataType.VARCHAR)
    }

    fun init() {}
}