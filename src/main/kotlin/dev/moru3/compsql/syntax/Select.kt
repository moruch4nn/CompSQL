package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.QuerySendable

// TODO.
interface Select: QuerySendable {
    var where: SelectWhere

    fun where(key: String): SelectKeyedWhere

    fun orderBy(table: String, orderType: OrderType): SelectWhere

    fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere
}