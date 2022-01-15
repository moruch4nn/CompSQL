package dev.moru3.compsql.sqlite.update.table.column

import dev.moru3.compsql.abstracts.AbstractColumn
import dev.moru3.compsql.datatype.BaseDataType

class SQLiteColumn(name: String, type: BaseDataType<*,*>): AbstractColumn(name, type) {
    override fun buildAsRaw(): Pair<String, List<Pair<Any?, BaseDataType<*,*>>>> {
        val types = mutableListOf<Pair<Any, BaseDataType<*,*>>>()
        val result = buildString {
            append("`").append(name).append("` ").append(type.typeName)
            if(isAutoIncrement) { return@buildString }
            if(isZeroFill&&type.allowZeroFill) { append(" ZEROFILL") }
            if(isUnsigned) { append(" UNSIGNED") }
            if(isNotNull&&type.allowNotNull) { append(" NOT NULL") }
            defaultValue?.takeIf{ type.allowDefault }?.also { append(" DEFAULT ?") }?.also { types.add(it to type) }
            // UniqueIndexはSQLiteTableで設定します。
        }
        return result to types
    }
}