package dev.moru3.compsql

import dev.moru3.compsql.interfaces.NonForceSyntax

interface Select: NonForceSyntax {
    fun orderBy(table: String, orderType: OrderType): Select

    fun orderBy(vararg values: Pair<String, OrderType>): Select

    fun key(key: String): KeyedSelect
}

interface FilteredSelect: Select {
    fun and(key: String): KeyedSelect
}

interface KeyedSelect {
    /**
     * イコール(=)
     */
    fun equal(value: Any?): FilteredSelect

    /**
     * 等しくない(!=)
     */
    fun notEquals(value: Any?): FilteredSelect

    /**
     * 大なり(>)
     */
    fun greater(value: Any?): FilteredSelect

    /**
     * 小なり(<)
     */
    fun less(value: Any?): FilteredSelect

    /**
     * 大なり(>=)
     */
    fun greaterOrEquals(value: Any?): FilteredSelect

    /**
     * 小なり(<=)
     */
    fun lessOrEquals(value: Any?): FilteredSelect

    fun isIn(value: List<Any?>): FilteredSelect

    fun isIn(vararg value: Any?): FilteredSelect
}

enum class OrderType {
    ASC,
    DESC
}

interface Where {

}

interface WhereKey {
    fun equals(any: Any): Select


}

class TestYo() {
    fun main() {

    }
}