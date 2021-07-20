package dev.moru3.compsql.datatype.types

import com.sun.org.apache.xpath.internal.operations.Bool
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.sql.PreparedStatement
import java.sql.Types

class BOOLEAN(): DataType<Boolean, Boolean> {

    override val typeName: String = "BOOLEAN"
    override val from: Class<Boolean> = Boolean::class.java
    override val type: Class<Boolean> = Boolean::class.java
    override val sqlType: Int = Types.VARCHAR
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val allowUnsigned: Boolean = false
    override val allowZeroFill: Boolean = false
    override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = false
    override val defaultProperty: String? = null
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, Boolean) -> Unit = { ps, i, a -> ps.setBoolean(i, a) }
    override val convert: (value: Boolean) -> Boolean = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any) {
        check(any is Boolean) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    init { addCustomType(this) }
}