package dev.moru3.compsql.datatype
import dev.moru3.compsql.datatype.types.binary.*
import dev.moru3.compsql.datatype.types.bool.*
import dev.moru3.compsql.datatype.types.text.*
import dev.moru3.compsql.datatype.types.numeric.*
import dev.moru3.compsql.datatype.types.numeric.unsigned.*
import java.sql.*

class Range(val a: Int, val b: Int) {
    override fun toString(): String {
        return "$a, $b"
    }
}

/**
 * 新しいSQLTypeを作成するにはこれを使用してください。
 */
interface DataType<F, T> {

    val typeName: String
    val from: Class<F>
    val type: Class<T>
    val sqlType: Int
    val allowPrimaryKey: Boolean
    val allowNotNull: Boolean
    val allowUnique: Boolean
    val isUnsigned: Boolean
    val allowZeroFill: Boolean
    val allowAutoIncrement: Boolean
    val allowDefault: Boolean
    val defaultProperty: String?
    val priority: Int

    val action: (PreparedStatement, Int, T)->Unit

    val convert: (value: F)->T

    fun set(ps: PreparedStatement, index: Int, any: Any?)

    fun get(resultSet: ResultSet, id: String): F?

    companion object {
        // binary系
        val BINARY = BINARY(255)
        val LONGBLOB = LONGBLOB(1023)
        val VARBINARY = VARBINARY(255)

        // boolean系
        val BOOLEAN = BOOLEAN()

        // numeric系
        val BIGINT = BIGINT(19)
        val INTEGER = INTEGER(10)
        val SMALLINT = SMALLINT(5)
        val TINYINT = TINYINT(3)
        // unsigned
        val UBIGINT = UBIGINT(20)
        val UINTEGER = UINTEGER(10)
        val USMALLINT = USMALLINT(5)
        val UTINYINT = UTINYINT(3)

        // text系
        val CHAR = CHAR(255)
        val LONGTEXT = LONGTEXT(10230)
        val TEXT = TEXT(1023)
        val VARCHAR = VARCHAR(255)
    }
}