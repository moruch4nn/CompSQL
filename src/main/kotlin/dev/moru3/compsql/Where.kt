package dev.moru3.compsql

import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface Where: NonCompleteSyntax {
    fun orderBy(table: String, orderType: OrderType): Where

    fun orderBy(vararg values: Pair<String, OrderType>): Where

    fun key(key: String): KeyedWhere

    fun add(string: String, vararg any: Any): FilteredWhere
}

interface FilteredWhere: NonCompleteSyntax {

    fun orderBy(table: String, orderType: OrderType): FilteredWhere

    fun orderBy(vararg values: Pair<String, OrderType>): FilteredWhere

    fun add(string: String, vararg any: Any): FilteredWhere

    fun and(key: String): KeyedWhere

    fun or(key: String): KeyedWhere
}

interface KeyedWhere {
    /**
     * イコール(=)
     */
    fun equal(value: Any?): FilteredWhere

    /**
     * 等しくない(!=)
     */
    fun notEquals(value: Any?): FilteredWhere

    /**
     * 大なり(>)
     */
    fun greater(value: Any?): FilteredWhere

    /**
     * 小なり(<)
     */
    fun less(value: Any?): FilteredWhere

    /**
     * 大なり(>=)
     */
    fun greaterOrEquals(value: Any?): FilteredWhere

    /**
     * 小なり(<=)
     */
    fun lessOrEquals(value: Any?): FilteredWhere
}

enum class OrderType {
    ASC,
    DESC
}