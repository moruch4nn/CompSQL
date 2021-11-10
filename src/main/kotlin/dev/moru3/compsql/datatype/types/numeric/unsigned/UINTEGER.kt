package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * UInteger (Unsigned Integer)とは 0 から 4294967295 までの整数を格納できるSQLの型です。
 * これより大きな型が必要な場合はBIGINT、UBIGINTを使用します。
 * Unsigned: UINTEGER, Non-Unsigned: INTEGER
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 *
 * 4294967295 = 42億9496万7295
 */
open class UINTEGER(val property: Byte): DataType<Long> {

    final override val typeName: String = "INT"
    override val from: Class<*> = Long::class.javaObjectType
    final override val type: Class<Long> = Long::class.javaObjectType
    final override val sqlType: Int = Types.INTEGER
    final override val allowPrimaryKey: Boolean = true
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = true
    final override val allowZeroFill: Boolean = true
    final override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    final override val action: (PreparedStatement, Int, Long) -> Unit = { ps, i, a -> ps.setLong(i, a) }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Number) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any.toLong())
    }

    override fun get(resultSet: ResultSet, id: String): Any? = resultSet.getLong(id)

    init { addCustomType(this) }
}