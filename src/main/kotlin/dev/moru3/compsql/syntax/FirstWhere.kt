package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface FirstWhere: Where {
    /**
     * WHERE文
     */
    fun key(key: String): KeyedWhere
}

interface Where: NonCompleteSyntax {
    /**
     * Warning: This function is not SQL injection protected
     * key(String)内に使用したい演算子がない場合にWHERE文内にオリジナルのテキストを埋め込めます。
     *
     * @param string WHERE文内に埋め込むテキスト
     */
    @Deprecated("Warning: This function is not SQL injection protected and should only be used if there are no operators in ke(string) that you want to use.")
    fun add(string: String): FilteredWhere

    fun limit(limit: Int): Where

    /**
     * selectの結果を昇順、もしくは降順に切り替えます。
     * @param key カラム名
     * @param orderType オーダータイプ
     */
    fun orderBy(key: String, orderType: OrderType): Where
}

interface FilteredWhere: Where {
    /**
     * and
     */
    fun and(key: String): KeyedWhere

    /**
     * or
     */
    fun or(key: String): KeyedWhere
}

interface KeyedWhere {
    /**
     * イコール(=)
     */
    fun equal(value: Any): FilteredWhere

    /**
     * 等しくない(!=)
     */
    fun notEquals(value: Any): FilteredWhere

    /**
     * 大なり(>)
     */
    fun greater(value: Any): FilteredWhere

    /**
     * 小なり(<)
     */
    fun less(value: Any): FilteredWhere

    /**
     * 大なり(>=)
     */
    fun greaterOrEquals(value: Any): FilteredWhere

    /**
     * 小なり(<=)
     */
    fun lessOrEquals(value: Any): FilteredWhere

    /**
     * IS NULL
     */
    fun isNull(): FilteredWhere

    /**
     * IS NOT NULL
     */
    fun isNotNull(): FilteredWhere

    /**
     * IS TRUE
     */
    fun isTrue(): FilteredWhere

    /**
     * IS FALSE
     */
    fun isFalse(): FilteredWhere

    /**
     * IS UNKNOWN
     */
    fun isUnknown(): FilteredWhere

    /**
     * BETWEEN ? AND ?
     */
    fun between(from: Any, to: Any): FilteredWhere

    /**
     * NOT BETWEEN ? AND ?
     */
    fun notBetween(from: Any, to: Any): FilteredWhere

    /**
     * IN (values)
     */
    fun isIn(vararg values: Any): FilteredWhere

    /**
     * NOT IN (values)
     */
    fun isNotIn(vararg values: Any): FilteredWhere

    /**
     * LIKE 'regex'
     */
    fun like(regex: String): FilteredWhere

    /**
     * NOT LIKE 'regex'
     */
    fun notLike(regex: String): FilteredWhere
}