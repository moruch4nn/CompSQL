package dev.moru3.compsql.datatype.types.binary

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.TypeHub.add
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * LONGBLOBとは0から4000000000(40億)までの固定長のバイナリを格納できます。
 */
open class BLOB(val property: Long): DataType<ByteArray> {

    constructor(int: Int): this(int.toLong())

    final override val typeName: String = "BLOB"
    override val from: Class<*> = ByteArray::class.java
    final override val type: Class<ByteArray> = ByteArray::class.java
    final override val sqlType: Int = Types.BINARY
    final override val allowPrimaryKey: Boolean = true
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = false
    final override val allowAutoIncrement: Boolean = true
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    final override val action: (PreparedStatement, Int, ByteArray) -> Unit = { ps, i, a -> ps.setBytes(i, a) }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        if(any is String) {
            action.invoke(ps, index, any.toByteArray())
        } else {
            check(any is ByteArray) { "The type of \"any\" is different from \"type\"." }
            action.invoke(ps, index, any)
        }
    }

    override fun get(resultSet: ResultSet, id: String): Any? { return resultSet.getBytes(id) }

    init { add(this) }
}