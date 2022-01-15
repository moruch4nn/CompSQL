package dev.moru3.compsql.sqlite

import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.interfaces.SQLProperties

object MySQLProperties: SQLProperties {

    private val nameMapping = mapOf<Class<out BaseDataType<*,*>>, String>(

    )


    override fun get(type: BaseDataType<*,*>): String = nameMapping[type::class.javaObjectType]?:type.typeName
}