package dev.moru3.compsql.sqlite.update.insert

import dev.moru3.compsql.abstracts.AbstractColumn
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.table.column.Column

class SQLiteColumn(name: String, type: DataType<*>): AbstractColumn(name, type) {
    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> {
        val types = mutableListOf<Pair<Any, DataType<*>>>()
        val result = buildString {
            // TODO
            append("`").append(name).append("` ").append(type.typeName)
            property?.also { append("(").append(it.toString()).append(")") }?: type.defaultProperty?.also { append("(").append(it).append(")") }
            if(isZeroFill&&type.allowZeroFill) { append(" ZEROFILL") }
            if(isUnsigned) { append(" UNSIGNED") }
            if((isNotNull||isPrimaryKey)&&type.allowNotNull) { append(" NOT") }
            append(" NULL")
            if(isAutoIncrement&&type.allowAutoIncrement) { append(" AUTO_INCREMENT") }
            defaultValue?.takeIf{ type.allowDefault }?.also { append(" DEFAULT ?") }?.also { types.add(it to type) }
            // UniqueIndexはSQLiteTableで設定します。
        }
        return result to types
    }
}