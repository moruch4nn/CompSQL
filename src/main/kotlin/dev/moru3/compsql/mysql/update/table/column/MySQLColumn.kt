package dev.moru3.compsql.mysql.update.table.column

import dev.moru3.compsql.IDataType
import dev.moru3.compsql.table.column.Column

class MySQLColumn(override var name: String, override val type: IDataType<*, *>): Column {
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
    override var isUnsigned: Boolean = false
        set(value) { field = value&&type.allowUnsigned }
    override var isZeroFill: Boolean = false
        set(value) { field = value&&type.allowZeroFill }

    override fun buildAsRaw(): Pair<String, List<Any>> {
        val types = mutableListOf<Any>()
        val result = buildString {
            append("`").append(name).append("` ").append(type.typeName)
            property?.also { append("(").append(it.toString()).append(")") }?: type.defaultProperty?.also { append("(").append(it).append(")") }
            // MYSQL ONLY
            if(isZeroFill&&type.allowZeroFill) append(" ZEROFILL")
            if(isUnsigned&&type.allowUnsigned) append(" UNSIGNED")
            if((isNotNull||isPrimaryKey)&&type.allowNotNull) append(" NOT")
            append(" NULL")
            if(isAutoIncrement&&type.allowAutoIncrement) append(" AUTO_INCREMENT")
            defaultValue?.takeIf{ type.allowDefault }?.also {
                append(" DEFAULT ?")
                types+=it
            }
            // UniqueIndexはMySQLTableで設定します。
        }
        return result to types
    }
}