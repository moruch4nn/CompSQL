package dev.moru3.compsql.mysql.update.table.column

import dev.moru3.compsql.abstracts.AbstractColumn
import dev.moru3.compsql.datatype.BaseDataType

class MySQLColumn(name: String, type: BaseDataType<*,*>): AbstractColumn(name, type) {
    override fun buildAsRaw(): Pair<String, List<Pair<Any, BaseDataType<*,*>>>> {
        val types = mutableListOf<Pair<Any, BaseDataType<*,*>>>()
        val result = buildString {
            append("`").append(name).append("` ").append(type.typeName)
            property?.also { append("(").append(it.toString()).append(")") }?: type.defaultProperty?.also { append("(").append(it).append(")") }
            if(isZeroFill&&type.allowZeroFill) { append(" ZEROFILL") }
            if(isUnsigned) { append(" UNSIGNED") }
            if((isNotNull||isPrimaryKey)&&type.allowNotNull) { append(" NOT") }
            append(" NULL")
            if(isAutoIncrement&&type.allowAutoIncrement) { append(" AUTO_INCREMENT") }
            defaultValue?.takeIf{ type.allowDefault }?.also { append(" DEFAULT ?") }?.also { types.add(it to type) }
            // UniqueIndexはMySQLTableで設定します。
        }
        return result to types
    }
}