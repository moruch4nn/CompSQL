package dev.moru3.compsql.syntax

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.ForceUpdateSendable
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

interface Insert: ForceUpdateSendable, NonCompleteSyntax {

    /**
     * テーブル
     */
    val table: Table

    /**
     * Insertで追加する値を追加します。
     * @param type 誤った処理を行わないようにtypeを指定します。DataType
     * @param key 追加する値のカラム名
     * @param value 追加する値
     */
    fun add(type: DataType<*,*>, key: String, value: Any): Insert

    /**
     * Insertで追加する値を追加します。
     * @param key 追加する値のカラム名
     * @param value 追加する値
     */
    fun add(key: String, value: Any): Insert

    fun build(force: Boolean): PreparedStatement

    fun buildAsRaw(force: Boolean): Pair<String, List<Pair<Any, DataType<*,*>>>>
}