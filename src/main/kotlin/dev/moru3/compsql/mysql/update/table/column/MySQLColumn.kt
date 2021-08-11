package dev.moru3.compsql.mysql.update.table.column

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.table.column.Column

class MySQLColumn(override var name: String, override val type: DataType<*, *>): Column {
    override var isPrimaryKey: Boolean = false

    override var isNotNull: Boolean = false
        set(value) { field = value&&type.allowNotNull }
    override var isAutoIncrement: Boolean = false
        set(value) { field = value&&type.allowAutoIncrement }
    override var defaultValue: Any? = null
        set(value) { if(type.allowDefault) { field = value } }
    override var property: Any? = null
    override var isUniqueIndex: Boolean = false
        set(value) { field = value&&type.allowUnique }
    override var isZeroFill: Boolean = false
        set(value) { field = value&&type.allowZeroFill }
    override val isUnsigned: Boolean = type.isUnsigned

    override fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*, *>>>> {
        val types = mutableListOf<Pair<Any, DataType<*, *>>>()
        val result = buildString {
            append("`").append(name).append("` ").append(type.typeName)
            property?.also { append("(").append(it.toString()).append(")") }?: type.defaultProperty?.also { append("(").append(it).append(")") }
            if(isZeroFill&&type.allowZeroFill) { append(" ZEROFILL") }
            if(isUnsigned) { append(" UNSIGNED") }
            if((isNotNull||isPrimaryKey)&&type.allowNotNull) { append(" NOT") }
            append(" NULL")
            if(isAutoIncrement&&type.allowAutoIncrement) { append(" AUTO_INCREMENT") }
            defaultValue?.takeIf{ type.allowDefault }?.also { append(" DEFAULT ?") }?.also { types.add(Pair(it, type)) }
            // UniqueIndexはMySQLTableで設定します。
        }
        return result to types
    }
}