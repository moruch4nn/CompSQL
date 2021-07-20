package dev.moru3.compsql

import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.SendSyntax
import dev.moru3.compsql.table.Table
import java.sql.PreparedStatement

interface Insert: SendSyntax, NonCompleteSyntax {

    /**
     * テーブル名
     */
    val table: Table

    fun add(type: IDataType<*, *>, key: String, value: Any): Insert

    fun add(key: String, value: Any): Insert

    fun build(force: Boolean): PreparedStatement

    fun buildAsRaw(force: Boolean): Pair<String, List<Any>>
}