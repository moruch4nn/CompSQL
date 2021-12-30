package dev.moru3.compsql.sqlite

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.binary.BINARY
import dev.moru3.compsql.datatype.types.binary.LongBlobBase
import dev.moru3.compsql.datatype.types.binary.VARBINARY
import dev.moru3.compsql.datatype.types.bool.BOOLEAN
import dev.moru3.compsql.datatype.types.date.DATETIME
import dev.moru3.compsql.datatype.types.numeric.BIGINT
import dev.moru3.compsql.datatype.types.numeric.SMALLINT
import dev.moru3.compsql.datatype.types.numeric.TINYINT
import dev.moru3.compsql.datatype.types.numeric.unsigned.UBIGINT
import dev.moru3.compsql.datatype.types.numeric.unsigned.UINTEGER
import dev.moru3.compsql.datatype.types.numeric.unsigned.USMALLINT
import dev.moru3.compsql.datatype.types.numeric.unsigned.UTINYINT
import dev.moru3.compsql.datatype.types.text.CHAR
import dev.moru3.compsql.datatype.types.text.LONGTEXT
import dev.moru3.compsql.datatype.types.text.VARCHAR
import dev.moru3.compsql.interfaces.SQLProperties

object SQLiteProperties: SQLProperties {

    private val nameMapping = mapOf<Class<out DataType<*,*>>, String>(
        BINARY::class.javaObjectType to "BLOB",
        LongBlobBase::class.javaObjectType to "BLOB",
        VARBINARY::class.javaObjectType to "BLOB",
        BOOLEAN::class.javaObjectType to "INTEGER",
        DATETIME::class.javaObjectType to "DATE",
        UBIGINT::class.javaObjectType to "INTEGER",
        UINTEGER::class.javaObjectType to "INTEGER",
        USMALLINT::class.javaObjectType to "INTEGER",
        UTINYINT::class.javaObjectType to "INTEGER",
        BIGINT::class.javaObjectType to "INTEGER",
        SMALLINT::class.javaObjectType to "INTEGER",
        TINYINT::class.javaObjectType to "INTEGER",
        CHAR::class.javaObjectType to "TEXT",
        LONGTEXT::class.javaObjectType to "TEXT",
        VARCHAR::class.javaObjectType to "TEXT"
    )


    override fun get(type: DataType<*,*>): String = nameMapping[type::class.javaObjectType]?:type.typeName
}