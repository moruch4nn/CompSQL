package dev.moru3.compsql.mysql.table.column

import dev.moru3.compsql.DataType
import dev.moru3.compsql.table.column.Column

class MySQLColumn(override val name: String, override val type: DataType<*>): Column {
    override var isPrimaryKey: Boolean = false

    override var isNotNull: Boolean = false
    override var isAutoIncrement: Boolean = false
    override var defaultValue: Any? = null
    override var property: Any? = null
    override var isUniqueIndex: Boolean = false
    override var isUnsigned: Boolean = false
    override val isZeroFill: Boolean = false

    override fun build(): String = buildString {
        append("`").append(name).append("` ").append(type.typeName)
        property?.also { append("(").append(it.toString()).append(")") }?: type.defaultProperty?.also { append("(").append(it.toString()).append(")") }
        // MYSQL ONLY
        if(isZeroFill&&type.allowZeroFill) append(" ZEROFILL")
        if(isUnsigned&&type.allowUnsigned) append(" UNSIGNED")
        if((isNotNull||isPrimaryKey)&&type.allowNotNull) append(" NOT")
        append(" NULL")
        if(isPrimaryKey&&type.allowPrimaryKey) append(" PRIMARY KEY")
        if(isAutoIncrement&&type.allowAutoIncrement) append(" AUTO_INCREMENT")
        defaultValue?.takeIf{ type.allowDefault }?.also {
            append(" DEFAULT ${type.anyConvert.invoke(it)}")
        }
        // UniqueIndexはMySQLTableで設定します。
    }
}