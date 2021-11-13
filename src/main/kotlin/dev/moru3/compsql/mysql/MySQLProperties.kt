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

object MySQLProperties: SQLProperties {

    private val nameMapping = mapOf<Class<out DataType<*>>, String>(

    )


    override fun get(type: DataType<*>): String = nameMapping[type::class.java]?:type.typeName
}