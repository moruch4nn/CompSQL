package dev.moru3.compsql.mysql.update.table.column

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.table.column.Column

class MySQLColumn(override val name: String, override val type: DataType<*, *>): Column {
    override var isPrimaryKey: Boolean = false
        private set
    override var isNotNull: Boolean = false
        private set(value) { field = value&&type.allowNotNull }
    override var isAutoIncrement: Boolean = false
        private set(value) { field = value&&type.allowAutoIncrement }
    override var defaultValue: Any? = null
        private set(value) { if(type.allowDefault) { field = value } }
    override var property: Any? = null
        private set
    override var isUniqueIndex: Boolean = false
        private set(value) { field = value&&type.allowUnique }
    override var isZeroFill: Boolean = false
        private set(value) { field = value&&type.allowZeroFill }
    override val isUnsigned: Boolean = type.isUnsigned

    override fun setNotNull(boolean: Boolean): Column {
        this.isNotNull = boolean
        return this
    }

    override fun setAutoIncrement(bolean: Boolean): Column {
        this.isAutoIncrement = bolean
        return this
    }

    override fun setDefaultValue(any: Any?): Column {
        this.defaultValue = any
        return this
    }

    override fun setProperty(any: Any?): Column {
        this.property = any
        return this
    }

    override fun setUniqueIndex(boolean: Boolean): Column {
        this.isUniqueIndex = boolean
        return this
    }

    override fun setPrimaryKey(boolean: Boolean): Column {
        isPrimaryKey = boolean
        return this
    }

    override fun setZeroFill(boolean: Boolean): Column {
        isZeroFill = boolean
        return this
    }

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
            defaultValue?.takeIf{ type.allowDefault }?.also { append(" DEFAULT ?") }?.also { types.add(it to type) }
            // UniqueIndexはMySQLTableで設定します。
        }
        return result to types
    }
}