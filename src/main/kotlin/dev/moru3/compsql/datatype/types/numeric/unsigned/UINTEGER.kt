package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.sql.PreparedStatement
import java.sql.Types

/**
 * UInteger (Unsigned Integer)とは 0 から 4294967295 までの整数を格納できるSQLの型です。
 * これより大きな型が必要な場合はBIGINT、UBIGINTを使用します。
 * Unsigned: UINTEGER, Non-Unsigned: INTEGER
 *
 * 4294967295 = 42億9496万7295
 */
class UINTEGER(val property: Long): DataType<Long, Long> {

    constructor(): this(4294967295)

    override val typeName: String = "INT"
    override val from: Class<Long> = Long::class.java
    override val type: Class<Long> = Long::class.java
    override val sqlType: Int = Types.INTEGER
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = true
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