package dev.moru3.compsql.sqlite

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.binary.BINARY
import dev.moru3.compsql.datatype.types.binary.LONGBLOB
import dev.moru3.compsql.datatype.types.binary.VARBINARY
import dev.moru3.compsql.datatype.types.bool.BOOLEAN
import dev.moru3.compsql.datatype.types.date.DATE
import dev.moru3.compsql.datatype.types.date.DATETIME
import dev.moru3.compsql.datatype.types.numeric.BIGINT
import dev.moru3.compsql.datatype.types.numeric.INTEGER
import dev.moru3.compsql.datatype.types.numeric.SMALLINT
import dev.moru3.compsql.datatype.types.numeric.TINYINT
import dev.moru3.compsql.datatype.types.numeric.unsigned.UBIGINT
import dev.moru3.compsql.datatype.types.numeric.unsigned.UINTEGER
import dev.moru3.compsql.datatype.types.numeric.unsigned.USMALLINT
import dev.moru3.compsql.datatype.types.numeric.unsigned.UTINYINT
import dev.moru3.compsql.datatype.types.text.CHAR
import dev.moru3.compsql.datatype.types.text.LONGTEXT
import dev.moru3.compsql.datatype.types.text.TEXT
import dev.moru3.compsql.datatype.types.text.VARCHAR
import dev.moru3.compsql.interfaces.SQLProperties

class SQLiteProperties: SQLProperties {

    val nameMapping = mapOf<Class<out DataType<*>>, String>(
        BINARY::class.java to "BLOB",
        LONGBLOB::class.java to "BLOB",
        VARBINARY::class.java to "BLOB",
        BOOLEAN::class.java to "INTEGER",
        DATE::class.java to "REAL",
        DATETIME::class.java to "REAL",
        UBIGINT::class.java to "REAL",
        UINTEGER::class.java to "REAL",
        USMALLINT::class.java to "INTEGER",
        UTINYINT::class.java to "INTEGER",
        BIGINT::class.java to "REAL",
        INTEGER::class.java to "INTEGER",
        SMALLINT::class.java to "INTEGER",
        TINYINT::class.java to "INTEGER",
        CHAR::class.java to "TEXT",
        LONGTEXT::class.java to "BLOB",
        TEXT::class.java to "TEXT",
        VARCHAR::class.java to "TEXT"
    )


    override fun getTypeName(type: DataType<*>): String {
        TODO("Not yet implemented")
    }
}