package dev.moru3.compsql.syntax

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.ForceUpdateSendable
import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.NonForceUpdateSendable
import dev.moru3.compsql.interfaces.QuerySendable
import dev.moru3.compsql.syntax.table.Table

interface Upsert: NonForceUpdateSendable {
    /**
     * テーブル名
     */
    val table: Table

    fun add(type: DataType<*>, key: String, value: Any): Upsert

    fun add(key: String, value: Any): Upsert
}