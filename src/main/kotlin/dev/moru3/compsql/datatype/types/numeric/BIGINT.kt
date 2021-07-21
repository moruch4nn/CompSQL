package dev.moru3.compsql.datatype.types.numeric

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.sql.PreparedStatement
import java.sql.Types


/**
 * BigIntとは -9223372036854775808 から 9223372036854775807 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合はDECIMAL、NUMERICを使用します。
 * Unsigned: UBIGINT, Non-Unsigned: BIGINT
 *
 * -9223372036854775808 = マイナス922京3372兆368億5477万5808
 * 9223372036854775807 = 922京3372兆368億5477万5807
 */
class BIGINT(val property: Long): DataType<Long, Long> {

    constructor(): this(9_223_372_036_854_775_807)

    override val typeName: String = "BIGINT"
    override val from: Class<Long> = Long::class.java
    override val type: Class<Long> = Long::class.java
    override val sqlType: Int = Types.BIGINT
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = true
    override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, Long) -> Unit = { ps, i, a -> ps.setLong(i, a) }
    override val convert: (value: Long) -> Long = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any) {
        check(any is Long) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    init { addCustomType(this) }
}