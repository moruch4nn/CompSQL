package dev.moru3.compsql.syntax.table

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.ForceUpdateSendable
import dev.moru3.compsql.syntax.table.column.Column
import java.sql.PreparedStatement

interface Table: ForceUpdateSendable {
    /**
     * テーブル名。setするとRENAMEされます。
     */
    val name: String

    /**
     * 新しくColumnを作成し、追加します。
     */
    fun column(name: String, type: DataType<*>, action: (Column)->Unit = {}): Column

    /**
     * 新しくColumnを作成し、追加します。
     */
    fun column(name: String, type: DataType<*>): Column

    /**
     * テーブルのAfter関連の関数が入ってます。
     */
    val after: AfterTable

    fun build(force: Boolean): PreparedStatement

    fun buildAsRaw(force: Boolean): Pair<String, List<Pair<Any?, DataType<*>>>>
}