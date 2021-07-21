package dev.moru3.compsql.datatype.types.numeric

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.sql.PreparedStatement
import java.sql.Types

/**
 * SmallInt -32768 から 32767 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合は INTEGER, UINTEGER を使用します。
 * Unsigned: USMALLINT, Non-Unsigned: SMALLINT
 */
class SMALLINT(val property: Int): DataType<Short, Short> {

    constructor(): this(32767)

    override val typeName: String = "SMALLINT"
    override val from: Class<Short> = Short::class.java
    override val type: Class<Short> = Short::class.java
    override val sqlType: Int = Types.SMALLINT
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = true
    override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, Short) -> Unit = { ps, i, a -> ps.setShort(i, a) }
    override val convert: (value: Short) -> Short = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any) {
        check(any is Short) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    init { addCustomType(this) }
}