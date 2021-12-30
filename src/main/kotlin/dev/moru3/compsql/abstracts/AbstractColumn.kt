package dev.moru3.compsql.abstracts

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.table.column.Column

abstract class AbstractColumn(override val name: String, final override val type: DataType<*,*>): Column {
    override var isPrimaryKey: Boolean = false
        protected set
    override var isNotNull: Boolean = false
        protected set(value) { field = value&&type.allowNotNull }
    override var isAutoIncrement: Boolean = false
        protected set(value) { field = value&&type.allowAutoIncrement }
    override var defaultValue: Any? = null
        protected set(value) { if(type.allowDefault) { field = value } }
    override var property: Any? = null
        protected set
    override var isUniqueIndex: Boolean = false
        protected set(value) { field = value&&type.allowUnique }
    override var isZeroFill: Boolean = false
        protected set(value) { field = value&&type.allowZeroFill }
    final override val isUnsigned: Boolean = type.isUnsigned

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
}