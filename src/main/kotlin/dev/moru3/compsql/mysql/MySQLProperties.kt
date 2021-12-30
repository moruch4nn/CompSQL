package dev.moru3.compsql.sqlite

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.SQLProperties

object MySQLProperties: SQLProperties {

    private val nameMapping = mapOf<Class<out DataType<*,*>>, String>(

    )


    override fun get(type: DataType<*,*>): String = nameMapping[type::class.java]?:type.typeName
}