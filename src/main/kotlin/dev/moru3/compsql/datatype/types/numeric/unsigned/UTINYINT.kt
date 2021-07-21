package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.sql.PreparedStatement
import java.sql.Types

/**
 * UTINYINT 0 から 255 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合は SMALLINT, USMALLINT を使用します。
 * Unsigned: UTINYINT, Non-Unsigned: TINYINT
 */
class UTINYINT(val property: Long): DataType<Byte, Byte> {

    constructor(): this(127)

    constructor(property: Int): this(property.toLong())

    override val typeName: String = "TINYINT"
    override val from: Class<Byte> = Byte::class.java
    override val type: Class<Byte> = Byte::class.java
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

    override fun set(ps: PreparedStatement, index: Int, any: Any) {
        check(any is Byte) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    init { addCustomType(this) }
}