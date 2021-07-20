package dev.moru3.compsql

import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.NonForceSyntax
import dev.moru3.compsql.table.Table
import java.sql.PreparedStatement

interface Upsert: NonForceSyntax, NonCompleteSyntax {
    /**
     * テーブル名
     */
    val table: Table

    fun add(type: DataType<*, *>, key: String, value: Any): Upsert

    fun add(key: String, value: Any): Upsert
}