package dev.moru3.compsql.datatype.types.bool

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.TypeHub.add
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * BOOLEANとはtrue、falseの1bitを格納できるSQLの型です。
 */
open class BOOLEAN: DataType<Boolean> {

    final override val typeName: String = "BIT"
    override val from: Class<*> = Boolean::class.java
    final override val type: Class<Boolean> = Boolean::class.java
    final override val sqlType: Int = Types.BOOLEAN
    final override val allowPrimaryKey: Boolean = true
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = false
    final override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "1"
    override val priority: Int = 10

    override fun get(resultSet: ResultSet, id: String): Boolean? = resultSet.getBoolean(id)

    init { add(this) }
}