package dev.moru3.compsql.datatype

import dev.moru3.compsql.datatype.DataType.Companion.addCustomType
import java.math.BigDecimal
import java.sql.*

class Range(val a: Int, val b: Int) {
    override fun toString(): String {
        return "$a, $b"
    }
}

/**
 * @param typeName RDBMSに格納される際に使用される型名です。複数のRDBMSで使用する場合はマッピングを作成する必要があります。
 * @param type
 */
class NativeDataType<F, T>(
    override val typeName: String,
    override val from: Class<F>,
    override val type: Class<T>,
    override val sqlType: Int,
    override val allowPrimaryKey: Boolean,
    override val allowNotNull: Boolean,
    override val allowUnique: Boolean,
    override val allowUnsigned: Boolean,
    override val allowZeroFill: Boolean,
    override val allowAutoIncrement: Boolean,
    override val allowDefault: Boolean,
    override val defaultProperty: String? = null,
    override val priority: Int,
    override val action: (PreparedStatement, Int, T)->Unit
): DataType<F, T> {

    override val convert = { value: F -> type.cast(value) }

    override fun set(ps: PreparedStatement, index: Int, any: Any) {
        check(from.isInstance(any)) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, type.cast(any))
    }

    init { addCustomType(this) }
}

interface DataType<F, T> {

    val typeName: String
    val from: Class<F>
    val type: Class<T>
    val sqlType: Int
    val allowPrimaryKey: Boolean
    val allowNotNull: Boolean
    val allowUnique: Boolean
    val allowUnsigned: Boolean
    val allowZeroFill: Boolean
    val allowAutoIncrement: Boolean
    val allowDefault: Boolean
    val defaultProperty: String?
    val priority: Int

    val action: (PreparedStatement, Int, T)->Unit

    val convert: (value: F)->T

    fun set(ps: PreparedStatement, index: Int, any: Any)

    companion object {

        /**
         * データタイプがlistで格納されています。
         */
        private var dataTypeList = mutableListOf<DataType<*,*>>()

        fun addCustomType(dataType: DataType<*,*>) {
            dataTypeList.add(dataType)
        }

        fun getDataTypeList(): List<DataType<*, *>> = dataTypeList.toMutableList()

        fun getTypeListByAny(any: Any): List<DataType<*,*>> {
            return dataTypeList.filter { it.type==any::class.java }
        }

        private inline fun <reified T> createDataType(typeName: String, sqlType: Int, allowPrimaryKey: Boolean, allowNotNull: Boolean, allowUnique: Boolean, allowUnsigned: Boolean, allowZeroFill: Boolean, allowAutoIncrement: Boolean, allowDefault: Boolean, defaultProperty: Any? = null, priority: Int, noinline setValue: (PreparedStatement, Int, T)->Unit): NativeDataType<T, T> {
            val dp: String? = if(defaultProperty is String) { "\"${defaultProperty}\"" } else defaultProperty?.toString()
            return NativeDataType(typeName, T::class.java, T::class.java, sqlType, allowPrimaryKey, allowNotNull, allowUnique, allowUnsigned, allowZeroFill, allowAutoIncrement, allowDefault, dp, priority, setValue)
        }
        // 文字列 注意: UTF8を使用する場合は一文字3byteになります。
        val VARCHAR = createDataType<String>("VARCHAR", Types.VARCHAR, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 65535, 10) { ps, i, a -> ps.setString(i, a) }
        val CHAR = createDataType<Char>("CHAR", Types.CHAR, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 255, 11) { ps, i, a -> ps.setString(i, a.toString()) }
        val TEXT = createDataType<String>("TEXT", Types.VARCHAR, allowPrimaryKey = false, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 65535, priority = 14) { ps, i, a -> ps.setString(i, a) }
        val LONGTEXT = createDataType<String>("LONGTEXT", Types.LONGVARCHAR, allowPrimaryKey = false, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 2147483647, priority = 14) { ps, i, a -> ps.setString(i, a) }

        // BOOLEAN
        val BOOLEAN = createDataType<Boolean>("BIT(1)", Types.BOOLEAN, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, priority = 10)  { ps, i, a -> ps.setBoolean(i, a) }

        // TODO バイナリバイト
        val BINARY = createDataType<ByteArray>("BINARY", Types.BINARY, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 255, 10) { ps, i, a -> ps.setBytes(i, a) }
        val VARBINARY = createDataType<ByteArray>("VARBINARY", Types.VARBINARY, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 65_535, 11) { ps, i, a -> ps.setBytes(i, a) }
        val BLOB = createDataType<ByteArray>("BLOB", Types.BLOB, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 255, 12) { ps, i, a -> ps.setBytes(i, a) }

        // 整数型 unsigned = property*2+1
        val TINYINT = createDataType<Byte>("TINYINT", Types.TINYINT, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, defaultProperty = 127, 5) { ps, i, a -> ps.setByte(i, a) }
        val SMALLINT = createDataType<Short>("SMALLINT", Types.SMALLINT, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, defaultProperty = 65_535, 6) { ps, i, a -> ps.setShort(i, a) }
        val INTEGER = createDataType<Int>("INT", Types.INTEGER, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, priority = 8) { ps, i, a -> ps.setInt(i, a) }
        // CompSQL does not support unsigned.
        val BIGINT = createDataType<Long>("BIGINT", Types.BIGINT, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, defaultProperty = 9_223_372_036_854_775_807, 9) { ps, i, a -> ps.setLong(i, a) }

        // 小数点型
        val DECIMAL = createDataType<BigDecimal>("DECIMAL", Types.DECIMAL, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = false, allowDefault = true, defaultProperty = Range(65, 0), 10) { ps, i, a -> ps.setBigDecimal(i, a) }
        val NUMERIC = createDataType<BigDecimal>("NUMERIC", Types.DECIMAL, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = false, allowDefault = true, defaultProperty = Range(65, 0), 10) { ps, i, a -> ps.setBigDecimal(i, a) }
        val REAL = createDataType<Float>("REAL", Types.REAL, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowDefault = true, allowAutoIncrement = false, priority = 11) { ps, i, a -> ps.setFloat(i, a) }
        val FLOAT = createDataType<Double>("FLOAT", Types.FLOAT, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowDefault = true, allowAutoIncrement = false, priority = 13) { ps, i, a -> ps.setDouble(i, a) }
        val DOUBLE = createDataType<Double>("DOUBLE", Types.DOUBLE, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowDefault = true, allowAutoIncrement = false, priority = 14) { ps, i, a -> ps.setDouble(i, a) }

        // 日付型、時間型
        val DATE = createDataType<Date>("DATE", Types.DATE, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowDefault = false, allowAutoIncrement = false, priority = 5) { ps, i, a -> ps.setDate(i, a) }
        val TIME = createDataType<Time>("TIME", Types.TIME, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowDefault = false, allowAutoIncrement = false, priority = 5) { ps, i, a -> ps.setTime(i, a) }
        val TIMESTAMP = createDataType<Timestamp>("TIMESTAMP", Types.TIMESTAMP, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false,allowDefault = false,  allowAutoIncrement = false, priority = 10) { ps, i, a -> ps.setTimestamp(i, a) }
        val DATETIME = createDataType<Timestamp>("DATETIME", Types.TIMESTAMP, allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowDefault = false, allowAutoIncrement = false, priority = 1) { ps, i, a -> ps.setTimestamp(i, a) }
    }
}

class CustomDataType<F, T>(base: DataType<F, T>, override val convert: (value: F) -> T): DataType<F, T> {
    override val typeName: String = base.typeName
    override val from: Class<F> = base.from
    override val type: Class<T> = base.type
    override val sqlType: Int = base.sqlType
    override val allowPrimaryKey: Boolean = base.allowPrimaryKey
    override val allowNotNull: Boolean = base.allowNotNull
    override val allowUnique: Boolean = base.allowUnique
    override val allowUnsigned: Boolean = base.allowUnsigned
    override val allowZeroFill: Boolean = base.allowZeroFill
    override val allowAutoIncrement: Boolean = base.allowAutoIncrement
    override val allowDefault: Boolean = base.allowDefault
    override val defaultProperty: String? = base.defaultProperty
    override val priority: Int = base.priority
    override val action: (PreparedStatement, Int, T) -> Unit = base.action

    override fun set(ps: PreparedStatement, index: Int, any: Any) { action.invoke(ps, index, convert.invoke(from.cast(any))) }
}