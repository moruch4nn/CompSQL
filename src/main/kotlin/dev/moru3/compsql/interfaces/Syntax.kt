package dev.moru3.compsql.interfaces

import dev.moru3.compsql.datatype.DataType
import java.sql.PreparedStatement

interface Syntax {
    fun build(): PreparedStatement
}

interface NonCompleteSyntax {
    /**
     * Pair<String1, List<Pair<Any1, DataType<*, *>>>>
     * String: ビルドした構文です。インジェクション対策のためvalueは?に置換されています。
     * Any1: ?を置換するオブジェクトです。
     * DataType: Any1のデータタイプです。
     */
    fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*,*>>>>
}