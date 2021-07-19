package dev.moru3.compsql.table

import dev.moru3.compsql.DataType
import dev.moru3.compsql.NativeDataType
import dev.moru3.compsql.interfaces.SendSyntax
import dev.moru3.compsql.table.column.Column
import java.sql.PreparedStatement

interface Table: SendSyntax {
    /**
     * テーブル名。setするとRENAMEされます。
     */
    val name: String

    /**
     * 新しくColumnを作成し、追加します。
     */
    fun column(name: String, type: DataType<*, *>, action: (Column)->Unit): Table

    /**
     * Columnを追加します。
     */
    fun column(column: Column): Table

    /**
     * テーブルのAfter関連の関数が入ってます。
     */
    val after: AfterTable

    fun build(force: Boolean): PreparedStatement
}