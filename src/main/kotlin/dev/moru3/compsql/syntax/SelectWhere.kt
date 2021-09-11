package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface SelectWhere: NonCompleteSyntax {

    fun orderBy(table: String, orderType: OrderType): SelectWhere

    fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere


    fun key(key: String): SelectKeyedWhere

    fun add(string: String, vararg any: Any): SelectFilteredWhere

    fun limit(limit: Int): SelectWhere
}

interface SelectFilteredWhere: NonCompleteSyntax {
    fun orderBy(table: String, orderType: OrderType): SelectFilteredWhere

    fun orderBy(vararg values: Pair<String, OrderType>): SelectFilteredWhere

    fun add(string: String, vararg any: Any): SelectFilteredWhere

    fun and(key: String): SelectKeyedWhere

    fun or(key: String): SelectKeyedWhere

    fun limit(limit: Int): SelectFilteredWhere
}

interface SelectKeyedWhere {
    /**
     * イコール(=)
     */
    fun equal(value: Any?): SelectFilteredWhere

    /**
     * 等しくない(!=)
     */
    fun notEquals(value: Any?): SelectFilteredWhere

    /**
     * 大なり(>)
     */
    fun greater(value: Any?): SelectFilteredWhere

    /**
     * 小なり(<)
     */
    fun less(value: Any?): SelectFilteredWhere

    /**
     * 大なり(>=)
     */
    fun greaterOrEquals(value: Any?): SelectFilteredWhere

    /**
     * 小なり(<=)
     */
    fun lessOrEquals(value: Any?): SelectFilteredWhere
}

enum class OrderType {
    ASC,
    DESC
}