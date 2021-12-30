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
        BINARY::class.java to "BLOB",
        LongBlobBase::class.java to "BLOB",
        VARBINARY::class.java to "BLOB",
        BOOLEAN::class.java to "INTEGER",
        DATETIME::class.java to "DATE",
        UBIGINT::class.java to "INTEGER",
        UINTEGER::class.java to "INTEGER",
        USMALLINT::class.java to "INTEGER",
        UTINYINT::class.java to "INTEGER",
        BIGINT::class.java to "INTEGER",
        SMALLINT::class.java to "INTEGER",
        TINYINT::class.java to "INTEGER",
        CHAR::class.java to "TEXT",
        LONGTEXT::class.java to "TEXT",
        VARCHAR::class.java to "TEXT"
    )


    override fun get(type: DataType<*,*>): String = nameMapping[type::class.java]?:type.typeName
}