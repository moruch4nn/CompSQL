package dev.moru3.compsql.interfaces

import dev.moru3.compsql.datatype.DataType
import java.sql.PreparedStatement

interface Syntax {
    fun build(): PreparedStatement
}

interface NonCompleteSyntax {
    fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*,*>>>>
}