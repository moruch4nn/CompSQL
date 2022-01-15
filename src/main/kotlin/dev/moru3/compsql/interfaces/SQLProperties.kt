package dev.moru3.compsql.interfaces

import dev.moru3.compsql.datatype.BaseDataType

interface SQLProperties {
    operator fun get(type: BaseDataType<*,*>): String?
}