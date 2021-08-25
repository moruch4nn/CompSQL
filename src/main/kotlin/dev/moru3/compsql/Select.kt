package dev.moru3.compsql

import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.Syntax
import java.sql.Date
import java.sql.ResultSet

// TODO.
interface Select: NonCompleteSyntax, Syntax {
    val where: SelectWhere

    fun where(key: String): SelectKeyedWhere

    fun orderBy(table: String, orderType: OrderType): SelectWhere

    fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere

    fun send(): ResultSet
}