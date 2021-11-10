package dev.moru3.compsql.syntax.table.column

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface Column: NonCompleteSyntax {
    /**
     *
     */
    val type: DataType<*>

    val name: String

    val isZeroFill: Boolean

    val isPrimaryKey: Boolean

    val isNotNull: Boolean

    val isAutoIncrement: Boolean

    val defaultValue: Any?

    val property: Any?

    val isUniqueIndex: Boolean

    val isUnsigned: Boolean

    fun setPrimaryKey(boolean: Boolean): Column

    fun setNotNull(boolean: Boolean): Column

    fun setAutoIncrement(bolean: Boolean): Column

    fun setDefaultValue(any: Any?): Column

    fun setProperty(any: Any?): Column

    fun setUniqueIndex(boolean: Boolean): Column

    fun setZeroFill(boolean: Boolean): Column
}