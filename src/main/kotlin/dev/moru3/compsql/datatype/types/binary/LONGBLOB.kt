package dev.moru3.compsql.datatype.types.binary

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * BINARYとは0から255までの固定長のバイナリを格納できます。
 */
open class LONGBLOB(val property: Long): DataType<ByteArray, ByteArray> {

    constructor(int: Int): this(int.toLong())

    override val typeName: String = "LONGBLOB"
    override val from: Class<ByteArray> = ByteArray::class.java
    override val type: Class<ByteArray> = ByteArray::class.java
    override val sqlType: Int = Types.BINARY
    override val allowPrimaryKey: Boolean = true
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = false
    override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, ByteArray) -> Unit = { ps, i, a -> ps.setBytes(i, a) }
    override val convert: (value: ByteArray) -> ByteArray = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is ByteArray) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): ByteArray? { return resultSet.getBytes(id) }

    init { addCustomType(this) }
}