package dev.moru3.compsql.sqlite.update.table.column

import dev.moru3.compsql.ObjectSerializer
import dev.moru3.compsql.abstracts.AbstractColumn
import dev.moru3.compsql.datatype.DataType

class SQLiteColumn(name: String, type: DataType<*>): AbstractColumn(name, type), ObjectSerializer {
    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> {
        val types = mutableListOf<Pair<Any, DataType<*>>>()
        val result = buildString {
            append("`").append(name).append("` ").append(type.typeName)
            if(isAutoIncrement) { return@buildString }
            if(isZeroFill&&type.allowZeroFill) { append(" ZEROFILL") }
            if(isUnsigned) { append(" UNSIGNED") }
            if(isNotNull&&type.allowNotNull) { append(" NOT NULL") }
            defaultValue?.takeIf{ type.allowDefault }?.also { append(" DEFAULT ${serializeObject(it)}") }
            // UniqueIndexはSQLiteTableで設定します。
        }
        return result to types
    }
}