package dev.moru3.compsql.datatype.types.numeric

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.sql.PreparedStatement
import java.sql.Types

/**
 * Integerとは -2147483648 から 2147483647 までの整数を格納できるSQLの型です。
 * これより大きな型が必要な場合はBIGINT、UBIGINTを使用します。
 * Unsigned: UINTEGER, Non-Unsigned: INTEGER
 *
 * -2147483648 = マイナス21億4748万3648
 * 2147483647 = 21億4748万3647
 */
class INTEGER(val property: Int): DataType<Int, Int> {

    constructor(): this(2_147_483_647)

    override val typeName: String = "INT"
    override val from: Class<Int> = Int::class.java
    override val type: Class<Int> = Int::class.java
    override val sqlType: Int = Types.INTEGER
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = true
    override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, Int) -> Unit = { ps, i, a -> ps.setInt(i, a) }
    override val convert: (value: Int) -> Int = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any) {
        check(any is Int) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    init { addCustomType(this) }
}