package dev.moru3.compsql.table.column

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface Column: NonCompleteSyntax {
    /**
     *
     */
    val type: DataType<*, *>

    var name: String

    var isZeroFill: Boolean

    var isPrimaryKey: Boolean

    var isNotNull: Boolean

    var isAutoIncrement: Boolean

    var defaultValue: Any?

    var property: Any?

    var isUniqueIndex: Boolean

    val isUnsigned: Boolean
}