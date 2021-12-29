package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.*
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.FilteredWhere
import dev.moru3.compsql.syntax.KeyedWhere
import dev.moru3.compsql.syntax.Where

open class MySQLWhere: Where {

    var limit: Int? = null

    val list = mutableListOf<Pair<Any?, DataType<*>>>()

    var string = ""

    override fun add(string: String, vararg any: Any): FilteredWhere {
        this.string+=string
        any.forEach { list += any to checkNotNull(TypeHub[it::class.java].getOrNull(0)) }
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

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> {
        if(limit!=null) { string+=" LIMIT $limit" }
        return (if(string.isEmpty()) "" else " WHERE $string") to list
    }
}

class MySQLKeyedWhere(private val data: MySQLWhere): KeyedWhere {
    override fun equal(value: Any?): FilteredWhere {
        data.string += " = ?"
        data.list.add((value to (value?.run { checkNotNull(TypeHub[value::class.java].getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun notEquals(value: Any?): FilteredWhere {
        data.string += " != ?"
        data.list.add((value to (value?.run { checkNotNull(TypeHub[value::class.java].getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun greater(value: Any?): FilteredWhere {
        data.string += " > ?"
        data.list.add((value to (value?.run { checkNotNull(TypeHub[value::class.java].getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun less(value: Any?): FilteredWhere {
        data.string += " < ?"
        data.list.add((value to (value?.run { checkNotNull(TypeHub[value::class.java].getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun greaterOrEquals(value: Any?): FilteredWhere {
        data.string += " >= ?"
        data.list.add((value to (value?.run { checkNotNull(TypeHub[value::class.java].getOrNull(0)) }?: NULL())))
        return MySQLFilteredWhere(data)
    }

    override fun lessOrEquals(value: Any?): FilteredWhere {
        data.string += " <= ?"
        data.list.add((value to (value?.run { checkNotNull(TypeHub[value::class.java].getOrNull(0)) }?: NULL())))
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

    override fun add(string: String, vararg any: Any): FilteredWhere {
        data.string+=string
        any.forEach { data.list += any to checkNotNull(TypeHub[it::class.java].getOrNull(0)) }
        return this
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> = data.buildAsRaw()
}