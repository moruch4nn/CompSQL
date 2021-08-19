package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.*
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.NULL

class MySQLWhere: Where {

    var limit: Int? = null

    val list = mutableListOf<Pair<Any?, DataType<*,*>>>()

    var string = ""

    var order: MutableMap<String, OrderType> = mutableMapOf()

    override fun add(string: String, vararg any: Any): FilteredWhere {
        this.string+=string
        any.forEach { list += any to checkNotNull(DataHub.getTypeListByAny(it).getOrNull(0)) }
        return MySQLFilteredWhere(this)
    }

    override fun limit(limit: Int): Where {
        this.limit = limit
        return this
    }

    override fun key(key: String): KeyedWhere {
        string += " $key"
        return MySQLKeyedWhere(this)
    }

    override fun orderBy(vararg values: Pair<String, OrderType>): Where {
        values.forEach { order[it.first] = it.second }
        return this
    }

    override fun orderBy(table: String, orderType: OrderType): Where {
        order[table] = orderType
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*,*>>>> {
        if(order.isNotEmpty()) {
            string += " ORDER BY"
            val orders = mutableListOf<String>()
            order.forEach { orders += " ${it.key} ${it.value}" }
            string += orders.joinToString(", ")
        }
        if(limit!=null) { string+=" LIMIT $limit" }
        return (if(string.isEmpty()) "" else " WHERE $string") to list
    }
}

class MySQLKeyedWhere(private val data: MySQLWhere): KeyedWhere {
    override fun equal(value: Any?): FilteredWhere {
        data.string += " = ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun notEquals(value: Any?): FilteredWhere {
        data.string += " != ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun greater(value: Any?): FilteredWhere {
        data.string += " > ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun less(value: Any?): FilteredWhere {
        data.string += " < ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun greaterOrEquals(value: Any?): FilteredWhere {
        data.string += " >= ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun lessOrEquals(value: Any?): FilteredWhere {
        data.string += " <= ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }
}

class MySQLFilteredWhere(private val data: MySQLWhere): FilteredWhere {
    override fun limit(limit: Int): FilteredWhere {
        data.limit = limit
        return this
    }

    override fun and(key: String): KeyedWhere {
        data.string += " and $key"
        return MySQLKeyedWhere(data)
    }

    override fun or(key: String): KeyedWhere {
        data.string += " or $key"
        return MySQLKeyedWhere(data)
    }

    override fun orderBy(vararg values: Pair<String, OrderType>): FilteredWhere {
        values.forEach { data.order[it.first] = it.second }
        return this
    }

    override fun orderBy(table: String, orderType: OrderType): FilteredWhere {
        data.order[table] = orderType
        return this
    }

    override fun add(string: String, vararg any: Any): FilteredWhere {
        data.string+=string
        any.forEach { data.list += any to checkNotNull(DataHub.getTypeListByAny(it).getOrNull(0)) }
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*,*>>>> = data.buildAsRaw()
}