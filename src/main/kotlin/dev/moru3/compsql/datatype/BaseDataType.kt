package dev.moru3.compsql.datatype
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
interface BaseDataType<F, T> {

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
}