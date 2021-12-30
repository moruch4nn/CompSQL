package dev.moru3.compsql.datatype.types.original

import dev.moru3.compsql.TypeHub
import dev.moru3.compsql.datatype.types.binary.LongBlobBase
import java.io.*
import java.sql.PreparedStatement
import java.sql.ResultSet

class INSTANCE: LongBlobBase<Serializable>(Int.MAX_VALUE.toLong()) {
    override val from: Class<Serializable> = Serializable::class.javaObjectType
    override val allowDefault: Boolean = false
    override val priority: Int = 0

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Serializable) { "The type of \"any\" is different from \"type\"." }
        val bos = ByteArrayOutputStream()
        ObjectOutputStream(bos).also { out -> out.writeObject(any) }.flush()
        super.set(ps, index, bos.toByteArray())
    }

    /**
     * 帰ってきたObjectをcastして使用してください。
     */
    override fun get(resultSet: ResultSet, id: String): Serializable? {
        val ois = ObjectInputStream(ByteArrayInputStream(resultSet.getBytes(id)?:return null))
        return ois.readObject() as Serializable
    }
}