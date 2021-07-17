package dev.moru3.compsql

import java.sql.Date
import java.sql.Time
import java.sql.Timestamp

class Range(val a: Int, val b: Int) {
    override fun toString(): String {
        return "$a, $b"
    }
}

/**
 * @param typeName RDBMSに格納される際に使用される型名です。複数のRDBMSで使用する場合はマッピングを作成する必要があります。
 * @param type
 */
data class DataType<T>(
    val typeName: String,
    val type: Class<*>,
    val allowPrimaryKey: Boolean,
    val allowNotNull: Boolean,
    val allowUnique: Boolean,
    val allowUnsigned: Boolean,
    val allowZeroFill: Boolean,
    val allowAutoIncrement: Boolean,
    val allowDefault: Boolean,
    val defaultProperty: Any? = null,
    val anyConvert: (Any)->String
) {
    companion object {
        inline fun <reified T> createDataType(typeName: String, allowPrimaryKey: Boolean, allowNotNull: Boolean, allowUnique: Boolean, allowUnsigned: Boolean, allowZeroFill: Boolean, allowAutoIncrement: Boolean, allowDefault: Boolean, defaultProperty: Any? = null, noinline anyConvert: (Any)->String): DataType<T> {
            // TODO
            return DataType(typeName, T::class.java, allowPrimaryKey, allowNotNull, allowUnique, allowUnsigned, allowZeroFill, allowAutoIncrement, allowDefault, defaultProperty, anyConvert)
        }
        // 文字列
        val VARCHAR = createDataType<String>("VARCHAR", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 65_535) { "\"$it\"" }
        val CHAR = createDataType<Char>("CHAR", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 255) { "\"$it\"" }
        val TINYTEXT = createDataType<String>("TINYTEXT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 255) { "\"$it\"" }
        val TEXT = createDataType<String>("TEXT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 65_535) { "\"$it\"" }
        val MEDIUMTEXT = createDataType<String>("MEDIUMTEXT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 16_777_215) { "\"$it\"" }
        val LONGTEXT = createDataType<String>("LONGTEXT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = false, defaultProperty = 4_294_967_295) { "\"$it\"" }

        // TODO バイナリバイト
        val BINARY = createDataType<ByteArray>("BINARY", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 255) { "$it" }
        val VARBINARY = createDataType<ByteArray>("VARBINARY", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 65_535) { "$it" }
        val TINYBLOB = createDataType<ByteArray>("TINYBLOB", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 255) { "$it" }
        val BLOB = createDataType<ByteArray>("BLOB", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowAutoIncrement = false, allowDefault = true, defaultProperty = 65_535) { "$it" }

        // 整数型 unsigned = property*2+1
        val TINYINT = createDataType<Byte>("VARCHAR", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, defaultProperty = 127) { "$it" }
        val SMALLINT = createDataType<Short>("SMALLINT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, defaultProperty = 65_535) { "$it" }
        val MEDIUMINT = createDataType<Int>("MEDIUMINT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, defaultProperty = 8_388_607) { "$it" }
        val INTEGER = createDataType<Int>("INT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true) { "$it" }
        val INT = INTEGER.copy()
        // CompSQL does not support unsigned.
        val BIGINT = createDataType<Long>("BIGINT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = true, allowAutoIncrement = true, allowDefault = true, defaultProperty = 9_223_372_036_854_775_807) { "$it" }

        // 小数点型
        val DECIMAL = createDataType<Float>("DECIMAL", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowAutoIncrement = false, allowDefault = true, defaultProperty = Range(65, 0)) { "$it" }
        val REAL = createDataType<Float>("REAL", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowDefault = true, allowAutoIncrement = false) { "$it" }
        val FLOAT = createDataType<Double>("FLOAT", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowDefault = true, allowAutoIncrement = false) { "$it" }
        val DOUBLE = createDataType<Double>("DOUBLE", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = true, allowZeroFill = true, allowDefault = true, allowAutoIncrement = false) { "$it" }

        // 日付型、時間型
        val DATE = createDataType<Date>("DATE", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowDefault = false, allowAutoIncrement = false) { "\"$it\"" }
        val TIME = createDataType<Time>("TIME", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowDefault = false, allowAutoIncrement = false) { "\"$it\"" }
        val TIMESTAMP = createDataType<Timestamp>("TIMESTAMP", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false,allowDefault = false,  allowAutoIncrement = false) { "\"$it\"" }
        val DATETIME = createDataType<Timestamp>("DATETIME", allowPrimaryKey = true, allowNotNull = true, allowUnique = true, allowUnsigned = false, allowZeroFill = false, allowDefault = false, allowAutoIncrement = false) { "\"$it\"" }
    }
}