package dev.moru3.compsql.table

import dev.moru3.compsql.DataType
import dev.moru3.compsql.interfaces.SendSyntax
import dev.moru3.compsql.table.column.Column

interface Table: SendSyntax {
    /**
     * テーブル名。
     */
    val name: String

    val columns: MutableList<Column>

    fun table(name: String, type: DataType<*>, action: (Column)->Unit): Table

    fun table(column: Column): Table

    /**
     * テーブルのAfter関連の関数が入ってます。
     */
    val after: AfterTable
}