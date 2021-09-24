package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.*
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.NULL
import dev.moru3.compsql.syntax.OrderType
import dev.moru3.compsql.syntax.SelectFilteredWhere
import dev.moru3.compsql.syntax.SelectKeyedWhere
import dev.moru3.compsql.syntax.SelectWhere

class MySQLSelectWhere: SelectWhere {
    var order: MutableMap<String, OrderType> = mutableMapOf()

    var limit: Int? = null

    val list = mutableListOf<Pair<Any?, DataType<*>>>()

    var string = ""

    override fun add(string: String, vararg any: Any): SelectFilteredWhere {
        this.string+=string
        any.forEach { list += any to checkNotNull(DataHub.getTypeListByAny(it).getOrNull(0)) }
        return MySQLSelectFilteredWhere(this)
    }

    override fun limit(limit: Int): SelectWhere {
        this.limit = limit
        return this
    }

    override fun key(key: String): SelectKeyedWhere {
        string += " $key"
        return MySQLSelectKeyedWhere(this)
    }

    override fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere {
        values.forEach { order[it.first] = it.second }
        return this
    }

    override fun orderBy(table: String, orderType: OrderType): SelectWhere {
        order[table] = orderType
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> {
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

class MySQLSelectKeyedWhere(private val data: MySQLSelectWhere): SelectKeyedWhere {
    override fun equal(value: Any?): SelectFilteredWhere {
        data.string += " = ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLSelectFilteredWhere(data)
    }

    override fun notEquals(value: Any?): SelectFilteredWhere {
        data.string += " != ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLSelectFilteredWhere(data)
    }

    override fun greater(value: Any?): SelectFilteredWhere {
        data.string += " > ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLSelectFilteredWhere(data)
    }

    override fun less(value: Any?): SelectFilteredWhere {
        data.string += " < ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLSelectFilteredWhere(data)
    }

    override fun greaterOrEquals(value: Any?): SelectFilteredWhere {
        data.string += " >= ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLSelectFilteredWhere(data)
    }

    override fun lessOrEquals(value: Any?): SelectFilteredWhere {
        data.string += " <= ?"
        data.list.add((value to (value?.run { checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) }?: NULL())))
        return MySQLSelectFilteredWhere(data)
    }
}

class MySQLSelectFilteredWhere(private val data: MySQLSelectWhere): SelectFilteredWhere {
    override fun limit(limit: Int): SelectFilteredWhere {
        data.limit = limit
        return this
    }

    override fun and(key: String): SelectKeyedWhere {
        data.string += " and $key"
        return MySQLSelectKeyedWhere(data)
    }

    override fun or(key: String): SelectKeyedWhere {
        data.string += " or $key"
        return MySQLSelectKeyedWhere(data)
    }

    override fun orderBy(vararg values: Pair<String, OrderType>): SelectFilteredWhere {
        values.forEach { data.order[it.first] = it.second }
        return this
    }

    override fun orderBy(table: String, orderType: OrderType): SelectFilteredWhere {
        data.order[table] = orderType
        return this
    }

    override fun add(string: String, vararg any: Any): SelectFilteredWhere {
        data.string+=string
        any.forEach { data.list += any to checkNotNull(DataHub.getTypeListByAny(it).getOrNull(0)) }
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> = data.buildAsRaw()
}