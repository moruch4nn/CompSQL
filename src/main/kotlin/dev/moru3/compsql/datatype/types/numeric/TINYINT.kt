package dev.moru3.compsql.datatype.types.numeric

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * TINYINT -128 から 127 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合は SMALLINT, USMALLINT を使用します。
 * Unsigned: UTINYINT, Non-Unsigned: TINYINT
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 */
open class TINYINT(val property: Byte): DataType<Byte, Byte> {

    override val typeName: String = "TINYINT"
    override val from: Class<Byte> = Byte::class.javaObjectType
    override val type: Class<Byte> = Byte::class.javaObjectType
    override val sqlType: Int = Types.TINYINT
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = true
    override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, Byte) -> Unit = { ps, i, a -> ps.setByte(i, a) }
    override val convert: (value: Byte) -> Byte = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Byte) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): Byte? = resultSet.getByte(id)

    init { addCustomType(this) }
}