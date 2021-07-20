package dev.moru3.compsql.table.column

import dev.moru3.compsql.IDataType
import dev.moru3.compsql.interfaces.NonCompleteSyntax

interface Column: NonCompleteSyntax {
    /**
     *
     */
    val type: IDataType<*, *>

    var name: String

    var isZeroFill: Boolean

    var isPrimaryKey: Boolean

    var isNotNull: Boolean

    var isAutoIncrement: Boolean

    var defaultValue: Any?

    var property: Any?

    var isUniqueIndex: Boolean

    var isUnsigned: Boolean
}