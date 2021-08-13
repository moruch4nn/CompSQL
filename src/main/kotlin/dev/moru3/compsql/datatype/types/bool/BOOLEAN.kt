package dev.moru3.compsql.datatype.types.bool

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * BOOLEANとはtrue、falseの1bitを格納できるSQLの型です。
 */
open class BOOLEAN: DataType<Boolean, Boolean> {

    override val typeName: String = "BIT"
    override val from: Class<Boolean> = Boolean::class.java
    override val type: Class<Boolean> = Boolean::class.java
    override val sqlType: Int = Types.BOOLEAN
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = false
    override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "1"
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, Boolean) -> Unit = { ps, i, a -> ps.setBoolean(i, a) }
    override val convert: (value: Boolean) -> Boolean = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Boolean) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): Boolean? = resultSet.getBoolean(id)

    init { addCustomType(this) }
}