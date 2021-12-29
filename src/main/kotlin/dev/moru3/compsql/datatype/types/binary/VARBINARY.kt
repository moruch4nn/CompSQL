package dev.moru3.compsql.datatype.types.binary

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.TypeHub.add
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * BINARYとは0から255までの固定長のバイナリを格納できます。
 * 256以上のサイズを格納する場合あ￥はVARBINARYを使用します。
 */
open class VARBINARY(val property: Int): DataType<ByteArray> {

    final override val typeName: String = "VARBINARY"
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

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        super.set(ps, index, if(any is String) any.toByteArray() else any)
    }

    override fun get(resultSet: ResultSet, id: String): ByteArray? { return resultSet.getBytes(id) }

    init { add(this) }
}