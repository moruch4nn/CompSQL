package dev.moru3.compsql

import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.Syntax
import java.sql.ResultSet

interface Select: NonCompleteSyntax, Syntax {
    fun where(key: String): KeyedWhere

    fun orderBy(table: String, orderType: OrderType): Where

    fun orderBy(vararg values: Pair<String, OrderType>): Where

    fun send(): ResultSet
}