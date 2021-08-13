package dev.moru3.compsql.datatype.types.original

import dev.moru3.compsql.DataHub.addCustomType
import dev.moru3.compsql.datatype.DataType
import java.io.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class INSTANCE: DataType<Serializable, ByteArray> {
    override val typeName: String = "LONGBLOB"
    override val from: Class<Serializable> = Serializable::class.java
    override val type: Class<ByteArray> = ByteArray::class.java
    override val sqlType: Int = Types.LONGVARBINARY
    override val allowPrimaryKey: Boolean = false
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = false
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = false
    override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = false
    override val defaultProperty: String? = null
    override val priority: Int = Int.MAX_VALUE
    override val action: (PreparedStatement, Int, ByteArray) -> Unit = { ps, i, a -> ps.setBytes(i, a) }
    override val convert: (value: Serializable) -> ByteArray = {
        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).also { out -> out.writeObject(it) }.flush()
        bos.toByteArray()
    }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Serializable) { "The type of \"any\" is different from \"type\"." }
        action(ps, index, convert(any))
    }

    /**
     * 帰ってきたObjectをcastして使用してください。
     */
    override fun get(resultSet: ResultSet, id: String): Serializable {
        val ois = ObjectInputStream(ByteArrayInputStream(resultSet.getBytes(id)))
        return ois.readObject() as Serializable
    }

    init { addCustomType(this) }
}