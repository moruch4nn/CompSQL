package dev.moru3.compsql.datatype.types.text

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.TypeHub.add
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLType
import java.sql.Types

/**
 * CHARとは 0から255文字までの文字を格納できます。
 * 256文字以上を格納する場合はVARCHAR、TEXT型を使用してください。
 * TEXTやLONGTEXTと違いPrimaryKeyとして利用可能です。
 */
open class CHAR(property: Int): CharBase<String>(property) {
    override val from: Class<String> = String::class.java
    override fun get(resultSet: ResultSet, id: String): String? = resultSet.getNString(id)
}

abstract class CharBase<F>(val property: Int): DataType<F, String> {
    final override val typeName: String = "CHAR"
    final override val type: Class<String> = String::class.java
    final override val sqlType: Int = Types.CHAR
    final override val allowPrimaryKey: Boolean = true
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = false
    final override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10

    init { add(this) }
}