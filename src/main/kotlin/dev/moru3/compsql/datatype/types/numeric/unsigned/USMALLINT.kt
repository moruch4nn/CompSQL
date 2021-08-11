package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.sql.PreparedStatement
import java.sql.Types

/**
 * SmallInt 0 から 65535 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合は INTEGER, UINTEGER を使用します。
 * Unsigned: USMALLINT, Non-Unsigned: SMALLINT
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 */
open class USMALLINT(val property: Byte): DataType<Int, Int> {

    override val typeName: String = "SMALLINT"
    override val from: Class<Int> = Int::class.javaObjectType
    override val type: Class<Int> = Int::class.javaObjectType
    override val sqlType: Int = Types.SMALLINT
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = true
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