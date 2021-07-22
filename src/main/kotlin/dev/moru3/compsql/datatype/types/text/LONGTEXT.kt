package dev.moru3.compsql.datatype.types.text

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.sql.PreparedStatement
import java.sql.Types

/**
 * LONGTEXT とは 0KBから4294967295KB(4GiB) までの文字を格納できます。
 * 文字数ではないためUTF8を使用する場合は一文字に3KB使用します。
 * CharやVarcharとは違い、PrimaryKeyとしては利用できません。
 */
class LONGTEXT(property: Int): DataType<String, String> {

    constructor(): this(2147483647)

    override val typeName: String = "LONGTEXT"
    override val from: Class<String> = String::class.java
    override val type: Class<String> = String::class.java
    override val sqlType: Int = Types.LONGVARCHAR
    override val allowPrimaryKey: Boolean = false
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = true
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = false
    override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String = "$property"
    override val priority: Int = 13
    override val action: (PreparedStatement, Int, String) -> Unit = { ps, i, a -> ps.setString(i, a) }
    override val convert: (value: String) -> String = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any) {
        check(any is String) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    init { addCustomType(this) }
}