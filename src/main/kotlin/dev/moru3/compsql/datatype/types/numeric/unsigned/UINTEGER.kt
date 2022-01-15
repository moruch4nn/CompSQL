package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.BaseDataType
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
open class UINTEGER(property: Byte): UIntegerBase<Long>(property) {
    override val from: Class<Long> = Long::class.javaObjectType
    override fun get(resultSet: ResultSet, id: String): Long? = resultSet.getLong(id)
}

abstract class UIntegerBase<F>(val property: Byte): BaseDataType<F,Long> {

    final override val typeName: String = "INTEGER"
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

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Number?) { "The type of \"${if(any!=null) any::class.javaObjectType.simpleName else "null"}\" is different from \"Number\"." }
        super.set(ps, index, any?.toLong())
    }
}