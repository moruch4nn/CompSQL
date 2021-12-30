package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.*
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.OrderType
import dev.moru3.compsql.syntax.SelectFilteredWhere
import dev.moru3.compsql.syntax.SelectKeyedWhere
import dev.moru3.compsql.syntax.SelectWhere

class MySQLSelectWhere: SelectWhere {
    var order: MutableMap<String, OrderType> = mutableMapOf()

    var limit: Int? = null

    val list = mutableListOf<Pair<Any?, DataType<*,*>>>()

    var string = ""

    override fun add(string: String, vararg any: Any): SelectFilteredWhere {
        this.string+=string
        any.forEach { list += any to checkNotNull(TypeHub[it::class.java].getOrNull(0)) }
        return MySQLSelectFilteredWhere(this)
    }

    override fun limit(limit: Int): SelectWhere {
        this.limit = limit
        return this
    }

    override fun key(column: String): SelectKeyedWhere {
        string += " $column"
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

class MySQLSelectKeyedWhere(private val data: MySQLSelectWhere): SelectKeyedWhere {

    fun end(string: String, value: Any): SelectFilteredWhere {
        data.string += " $string ?"
        data.list.add((value to checkNotNull(TypeHub[value::class.java].getOrNull(0))))
        return MySQLSelectFilteredWhere(data)
    }

    fun end(string: String): SelectFilteredWhere {
        data.string += " $string"
        return MySQLSelectFilteredWhere(data)
    }

    override fun equal(value: Any): SelectFilteredWhere = end("<=>", value)

    override fun notEquals(value: Any): SelectFilteredWhere = end("<>", value)

    override fun greater(value: Any): SelectFilteredWhere = end(">", value)

    override fun less(value: Any): SelectFilteredWhere = end("<", value)

    override fun greaterOrEquals(value: Any): SelectFilteredWhere = end(">=", value)

    override fun lessOrEquals(value: Any): SelectFilteredWhere = end("<=", value)
    override fun isNull(): SelectFilteredWhere = end("IS NULL")

    override fun isNotNull(): SelectFilteredWhere = end("IS NOT NULL")

    override fun isTrue(): SelectFilteredWhere = end("IS TRUE")

    override fun isFalse(): SelectFilteredWhere = end("IS FALSE")

    override fun isUnknown(): SelectFilteredWhere = end("IS UNKNOWN")

    override fun between(from: Any, to: Any): SelectFilteredWhere {
        data.string += " BETWEEN ? AND ?"
        data.list.add((from to checkNotNull(TypeHub[from::class.java].getOrNull(0))))
        data.list.add((to to checkNotNull(TypeHub[to::class.java].getOrNull(0))))
        return MySQLSelectFilteredWhere(data)
    }

    override fun notBetween(from: Any, to: Any): SelectFilteredWhere {
        data.string += " NOT"
        return between(from, to)
    }

    override fun isIn(vararg values: Any): SelectFilteredWhere {
        data.string += " IN (${MutableList(values.size) { "?" }.joinToString(", ")})"
        values.forEach { data.list.add((it to checkNotNull(TypeHub[it::class.java].getOrNull(0)))) }
        return MySQLSelectFilteredWhere(data)
    }

    override fun isNotIn(vararg values: Any): SelectFilteredWhere {
        data.string += " NOT"
        return isIn(values)
    }

    override fun like(regex: String): SelectFilteredWhere {
        return end("LIKE", regex)
    }

    override fun notLike(regex: String): SelectFilteredWhere {
        data.string += " NOT"
        return like(regex)
    }
}

class MySQLSelectFilteredWhere(private val data: MySQLSelectWhere): SelectFilteredWhere {

    override fun limit(limit: Int): SelectFilteredWhere {
        data.limit = limit
        return this
    }

    override fun and(column: String): SelectKeyedWhere {
        data.string += " and $column"
        return MySQLSelectKeyedWhere(data)
    }

    override fun or(column: String): SelectKeyedWhere {
        data.string += " or $column"
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
        any.forEach { data.list += any to checkNotNull(TypeHub[it::class.java].getOrNull(0)) }
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*,*>>>> = data.buildAsRaw()
}