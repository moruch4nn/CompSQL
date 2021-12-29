package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface SelectWhere: NonCompleteSyntax {

    fun orderBy(table: String, orderType: OrderType): SelectWhere

    fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere


    fun key(column: String): SelectKeyedWhere

    fun add(string: String, vararg any: Any): SelectFilteredWhere

    fun limit(limit: Int): SelectWhere
}

interface SelectFilteredWhere: NonCompleteSyntax {
    fun orderBy(table: String, orderType: OrderType): SelectFilteredWhere

    fun orderBy(vararg values: Pair<String, OrderType>): SelectFilteredWhere

    fun add(string: String, vararg any: Any): SelectFilteredWhere

    fun and(column: String): SelectKeyedWhere

    fun or(column: String): SelectKeyedWhere

    fun limit(limit: Int): SelectFilteredWhere
}

interface SelectKeyedWhere {
    /**
     * イコール(=)
     */
    fun equal(value: Any): SelectFilteredWhere

    /**
     * 等しくない(!=)
     */
    fun notEquals(value: Any): SelectFilteredWhere

    /**
     * 大なり(>)
     */
    fun greater(value: Any): SelectFilteredWhere

    /**
     * 小なり(<)
     */
    fun less(value: Any): SelectFilteredWhere

    /**
     * 大なり(>=)
     */
    fun greaterOrEquals(value: Any): SelectFilteredWhere

    /**
     * 小なり(<=)
     */
    fun lessOrEquals(value: Any): SelectFilteredWhere

    /**
     * IS NULL
     */
    fun isNull(): SelectFilteredWhere

    /**
     * IS NOT NULL
     */
    fun isNotNull(): SelectFilteredWhere

    /**
     * IS TRUE
     */
    fun isTrue(): SelectFilteredWhere

    /**
     * IS FALSE
     */
    fun isFalse(): SelectFilteredWhere

    /**
     * IS UNKNOWN
     */
    fun isUnknown(): SelectFilteredWhere

    /**
     * BETWEEN ? AND ?
     */
    fun between(from: Any, to: Any): SelectFilteredWhere

    /**
     * NOT BETWEEN ? AND ?
     */
    fun notBetween(from: Any, to: Any): SelectFilteredWhere

    /**
     * IN (values)
     */
    fun isIn(vararg values: Any): SelectFilteredWhere

    /**
     * NOT IN (values)
     */
    fun isNotIn(vararg values: Any): SelectFilteredWhere

    /**
     * LIKE 'regex'
     */
    fun like(regex: String): SelectFilteredWhere

    /**
     * NOT LIKE 'regex'
     */
    fun notLike(regex: String): SelectFilteredWhere
}

enum class OrderType {
    ASC,
    DESC
}