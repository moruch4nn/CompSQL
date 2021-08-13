package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.*
import dev.moru3.compsql.datatype.DataType

class MySQLWhere: Where {

    private val list = mutableListOf<Pair<Any, DataType<*, *>>>()

    private var string = ""

    private var order: MutableMap<String, OrderType?> = mutableMapOf()

    override fun add(string: String, vararg any: Any): FilteredWhere {
        this.string+=string
        any.forEach { list += any to checkNotNull(DataHub.getTypeListByAny(it).getOrNull(0)) }
        return MySQLFilteredWhere(this)
    }

    override fun key(key: String): KeyedWhere {
        TODO("Not yet implemented")
    }

    override fun orderBy(vararg values: Pair<String, OrderType>): Where {
        values.forEach { order[it.first] = it.second }
        return this
    }

    override fun orderBy(table: String, orderType: OrderType): Where {
        order[table] = orderType
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*, *>>>> {
        TODO("Not yet implemented")
    }
}

class MySQLKeyedWhere(private val data: Where): KeyedWhere {
    override fun equal(value: Any?): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun notEquals(value: Any?): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun greater(value: Any?): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun less(value: Any?): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun greaterOrEquals(value: Any?): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun lessOrEquals(value: Any?): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun isIn(value: List<Any?>): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun isIn(vararg value: Any?): FilteredWhere {
        TODO("Not yet implemented")
    }

}

class MySQLFilteredWhere(private val data: Where): FilteredWhere {
    override fun and(key: String): KeyedWhere {
        TODO("Not yet implemented")
    }

    override fun or(key: String): KeyedWhere {
        TODO("Not yet implemented")
    }

    override fun orderBy(table: String, orderType: OrderType): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun orderBy(vararg values: Pair<String, OrderType>): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun key(key: String): KeyedWhere {
        TODO("Not yet implemented")
    }

    override fun add(string: String, vararg any: Any): FilteredWhere {
        TODO("Not yet implemented")
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*, *>>>> {
        TODO("Not yet implemented")
    }
}