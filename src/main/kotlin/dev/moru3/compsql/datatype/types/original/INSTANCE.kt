package dev.moru3.compsql.datatype.types.original

import dev.moru3.compsql.DataHub.addCustomType
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.binary.LONGBLOB
import java.io.*
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class INSTANCE: LONGBLOB(Int.MAX_VALUE) {
    override val from: Class<*> = Serializable::class.java
    override val allowDefault: Boolean = false
    override val priority: Int = 0

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Serializable) { "The type of \"any\" is different from \"type\"." }
        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).also { out -> out.writeObject(any) }.flush()
        action(ps, index, bos.toByteArray())
    }

    /**
     * 帰ってきたObjectをcastして使用してください。
     */
    override fun get(resultSet: ResultSet, id: String): Any {
        val ois = ObjectInputStream(ByteArrayInputStream(resultSet.getBytes(id)))
        return ois.readObject() as Serializable
    }

    init { addCustomType(this) }
}