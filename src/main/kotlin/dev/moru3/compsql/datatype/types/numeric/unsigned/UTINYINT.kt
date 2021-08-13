package dev.moru3.compsql.datatype.types.numeric.unsigned

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * UTINYINT 0 から 255 までの整数を格納できるSQLの型です。
 * これ以上大きな型が必要な場合は SMALLINT, USMALLINT を使用します。
 * Unsigned: UTINYINT, Non-Unsigned: TINYINT
 * 注意: numeric系のプロパティは"最大数"ではなく"最大桁数"なのでお間違えなく。
 */
open class UTINYINT(val property: Byte): DataType<Short, Short> {

    override val typeName: String = "TINYINT"
    override val from: Class<Short> = Short::class.javaObjectType
    override val type: Class<Short> = Short::class.javaObjectType
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
    override val action: (PreparedStatement, Int, Short) -> Unit = { ps, i, a -> ps.setShort(i, a) }
    override val convert: (value: Short) -> Short = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Short) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): Short? = resultSet.getShort(id)

    init { addCustomType(this) }
}