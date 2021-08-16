package dev.moru3.compsql.datatype.types

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.DataHub.addCustomType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * CHARとは 0から255文字までの文字を格納できます。
 * 256文字以上を格納する場合はVARCHAR、TEXT型を使用してください。
 * TEXTやLONGTEXTと違いPrimaryKeyとして利用可能です。
 */
open class NULL(): DataType<Any?, Any?> {

    override val typeName: String
        get() = throw Exception("NULL is not a type.")
    override val from: Class<Any?>
        get() = throw Exception("NULL is not a type.")
    override val type: Class<Any?>
        get() = throw Exception("NULL is not a type.")
    override val sqlType: Int = Types.NULL
    override val allowPrimaryKey: Boolean
        get() = throw Exception("NULL is not a type.")
    override val allowNotNull: Boolean
        get() = throw Exception("NULL is not a type.")
    override val allowUnique: Boolean
        get() = throw Exception("NULL is not a type.")
    override val isUnsigned: Boolean
        get() = throw Exception("NULL is not a type.")
    override val allowZeroFill: Boolean
        get() = throw Exception("NULL is not a type.")
    override val allowAutoIncrement: Boolean
        get() = throw Exception("NULL is not a type.")
    override val allowDefault: Boolean
        get() = throw Exception("NULL is not a type.")
    override val defaultProperty: String
        get() = throw Exception("NULL is not a type.")
    override val priority: Int = 0
    override val action: (PreparedStatement, Int, Any?) -> Unit = { ps, i, _ -> ps.setNull(i, 0) }
    override val convert: (value: Any?) -> Any?
        get() = throw Exception("NULL is not a type.")


    override fun get(resultSet: ResultSet, id: String): Any? = null
    override fun set(ps: PreparedStatement, index: Int, any: Any?) { action.invoke(ps, index, any) }

    init { addCustomType(this) }
}