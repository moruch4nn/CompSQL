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
open class UTINYINT(val property: Byte): DataType<Short> {

    final override val typeName: String = "TINYINT"
    override val from: Class<*> = Short::class.javaObjectType
    final override val type: Class<Short> = Short::class.javaObjectType
    final override val sqlType: Int = Types.TINYINT
    final override val allowPrimaryKey: Boolean = true
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = true
    final override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    final override val action: (PreparedStatement, Int, Short) -> Unit = { ps, i, a -> ps.setShort(i, a) }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Number) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any.toShort())
    }

    override fun get(resultSet: ResultSet, id: String): Any? = resultSet.getShort(id)

    init { addCustomType(this) }
}