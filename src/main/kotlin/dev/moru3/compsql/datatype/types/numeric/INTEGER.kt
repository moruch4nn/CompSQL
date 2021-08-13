package dev.moru3.compsql.datatype.types.numeric

import dev.moru3.compsql.DataHub.addCustomType
import dev.moru3.compsql.datatype.DataType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * Integerとは -2147483648 から 2147483647 までの整数を格納できるSQLの型です。
 * これより大きな型が必要な場合はBIGINT、UBIGINTを使用します。
 * Unsigned: UINTEGER, Non-Unsigned: INTEGER
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 *
 * -2147483648 = マイナス21億4748万3648
 * 2147483647 = 21億4748万3647
 */
open class INTEGER(val property: Byte): DataType<Int, Int> {

    override val typeName: String = "INT"
    override val from: Class<Int> = Int::class.javaObjectType
    override val type: Class<Int> = Int::class.javaObjectType
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

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Int) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): Int? = resultSet.getInt(id)

    init { addCustomType(this) }
}