package dev.moru3.compsql.datatype.types.text

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
open class CHAR(property: Int): DataType<String> {

    final override val typeName: String = "CHAR"
    override val from: Class<*> = String::class.java
    final override val type: Class<String> = String::class.java
    final override val sqlType: Int = Types.CHAR
    final override val allowPrimaryKey: Boolean = true
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = true
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = false
    final override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 10
    final override val action: (PreparedStatement, Int, String) -> Unit = { ps, i, a -> ps.setString(i, a) }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is String) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): Any? = resultSet.getNString(id)

    init { addCustomType(this) }
}