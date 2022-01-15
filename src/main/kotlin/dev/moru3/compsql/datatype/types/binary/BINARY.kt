package dev.moru3.compsql.datatype.types.binary

import dev.moru3.compsql.datatype.BaseDataType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * BINARYとは0から255までの固定長のバイナリを格納できます。
 * 256以上のサイズを格納する場合はBLOBを使用します。
 */
class BINARY(property: Int): BinaryBase<ByteArray>(property) {
    override val from: Class<ByteArray> = ByteArray::class.javaObjectType
    override fun get(resultSet: ResultSet, id: String): ByteArray? { return resultSet.getBytes(id) }
}

abstract class BinaryBase<F>(val property: Int): BaseDataType<F,ByteArray> {

    final override val typeName: String = "BINARY"
    final override val type: Class<ByteArray> = ByteArray::class.javaObjectType
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
}