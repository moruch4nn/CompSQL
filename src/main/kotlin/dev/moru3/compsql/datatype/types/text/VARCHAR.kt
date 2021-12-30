package dev.moru3.compsql.datatype.types.text

import dev.moru3.compsql.TypeHub.add
import dev.moru3.compsql.datatype.DataType
import java.sql.ResultSet
import java.sql.Types

/**
 * VARCHARとは 0 から 65535 文字までの文字を格納できます。
 * 65535文字以上を格納する場合はTEXT型を使用してください。
 * TEXTやLONGTEXTと違いPrimaryKeyとして利用可能です。
 */
open class VARCHAR(property: Int): VarcharBase<String>(property) {
    override val from: Class<String> = String::class.javaObjectType
    override fun get(resultSet: ResultSet, id: String): String? = resultSet.getNString(id)
}

abstract class VarcharBase<F>(val property: Int): DataType<F, String> {
    final override val typeName: String = "VARCHAR"
    final override val type: Class<String> = String::class.javaObjectType
    final override val sqlType: Int = Types.VARCHAR
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