package dev.moru3.compsql.interfaces

import dev.moru3.compsql.datatype.DataType

interface SQLProperties {
    fun getTypeName(type: DataType<*>): String
}