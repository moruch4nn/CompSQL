package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.TypeHub.add
import java.math.BigDecimal
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * UBigInt(Unsigned BigInt)とは 0 から 18,446,744,073,709,551,615 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合はDECIMAL、NUMERICを使用します。
 * Unsigned: UBIGINT, Non-Unsigned: BIGINT
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 *
 * 18446744073709551615 = 1844京6744兆737億955万1615
 */
open class UBIGINT(property: Byte): UBigIntBase<BigDecimal>(property) {
    override val from: Class<BigDecimal> = BigDecimal::class.javaObjectType
    override fun get(resultSet: ResultSet, id: String): BigDecimal? = resultSet.getBigDecimal(id)
}

abstract class UBigIntBase<F>(val property: Byte): DataType<F,BigDecimal> {

    final override val typeName: String = "BIGINT"
    final override val type: Class<BigDecimal> = BigDecimal::class.javaObjectType
    final override val sqlType: Int = Types.BIGINT
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
        if(any is Number?) {
            super.set(ps, index, BigDecimal(any.toString()))
        }
    }

    init { add(this) }

    companion object { private val max = BigDecimal("18446744073709551615") }
}