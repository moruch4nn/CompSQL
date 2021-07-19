package dev.moru3.compsql.interfaces

import java.sql.PreparedStatement

interface Syntax {
    fun build(): PreparedStatement
}

interface NonCompleteSyntax {
    fun build(): Pair<String, List<Any>>
}