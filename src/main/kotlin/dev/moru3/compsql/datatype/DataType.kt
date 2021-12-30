package dev.moru3.compsql.datatype
import dev.moru3.compsql.TypeHub
import dev.moru3.compsql.TypeHub.register
import dev.moru3.compsql.datatype.types.binary.*
import dev.moru3.compsql.datatype.types.bool.*
import dev.moru3.compsql.datatype.types.date.DATE
import dev.moru3.compsql.datatype.types.date.DATETIME
import dev.moru3.compsql.datatype.types.text.*
import dev.moru3.compsql.datatype.types.numeric.*
import dev.moru3.compsql.datatype.types.numeric.unsigned.*
import org.jetbrains.annotations.Contract
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

    /**
     * PreparedStatementの指定されたindexに対してanyをsetします。
     * anyがDataTypeのtypeと異なる場合はIllegalStateExceptionをthrowします。
     * @param ps setするPreparedStatement
     * @param index setする変数のindex
     * @param any setするオブジェクト。
     */
    @Contract("_, _, null -> !null")
    fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(from.isInstance(any)) { "The type of \"${if(any!=null) any::class.javaObjectType.simpleName else "null"}\" is different from \"${type::class.javaObjectType.simpleName}\"." }
        ps.setObject(index, any, sqlType)
    }

    /**
     * ResultSetからidに対応した値を変換して返します。
     */
    fun get(resultSet: ResultSet, id: String): F?

    companion object {
        // binary系
        val BINARY = register(BINARY(255))
        val LONGBLOB = register(LONGBLOB(1023))
        val VARBINARY = register(VARBINARY(255))

        // boolean系
        val BOOLEAN = register(BOOLEAN())

        // numeric系
        val BIGINT = register(BIGINT(19))
        val INTEGER = register(INTEGER(10))
        val SMALLINT = register(SMALLINT(5))
        val TINYINT = register(TINYINT(3))
        // unsigned
        val UBIGINT = register(UBIGINT(20))
        val UINTEGER = register(UINTEGER(10))
        val USMALLINT = register(USMALLINT(5))
        val UTINYINT = register(UTINYINT(3))

        // text系
        val CHAR = register(CHAR(255))
        val LONGTEXT = register(LONGTEXT(1023))
        val TEXT = register(TEXT(511))
        val VARCHAR = register(VARCHAR(255))

        // date型
        val DATE = register(DATE(null))
        val DATETIME = register(DATETIME(null))
    }
}