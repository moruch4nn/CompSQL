package dev.moru3.compsql.syntax

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.SendSyntax
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

interface Insert: SendSyntax, NonCompleteSyntax {

    /**
     * テーブル名
     */
    val table: Table

    fun add(type: DataType<*>, key: String, value: Any): Insert

    fun add(key: String, value: Any): Insert

    fun build(force: Boolean): PreparedStatement

    fun buildAsRaw(force: Boolean): Pair<String, List<Pair<Any, DataType<*>>>>
}