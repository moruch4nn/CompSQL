package dev.moru3.compsql.table.column

import dev.moru3.compsql.DataType
import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface Column: NonCompleteSyntax {
    /**
     *
     */
    val type: DataType<*, *>

    val name: String

    val isZeroFill: Boolean

    val isPrimaryKey: Boolean

    val isNotNull: Boolean

    val isAutoIncrement: Boolean

    val defaultValue: Any?

    val property: Any?

    val isUniqueIndex: Boolean

    val isUnsigned: Boolean
}