package dev.moru3.compsql.mysql.update.insert

import dev.moru3.compsql.DataHub.Companion.connection
import dev.moru3.compsql.DataType
import dev.moru3.compsql.Insert

class MySQLInsert(override val name: String) : Insert {

    val values = mutableMapOf<String, String>()

    override fun add(type: DataType<*>, key: String, value: Any): Insert {
        check(type.type.isInstance(value)) { "The type of the specified value does not match the `type` in DataType." }
        values[key] = type.anyConvert(type.type.cast(value))
        return this
    }

    /**
     * valueから型を推論します。推論できない場合はVARCHARに変換されます。
     */
    @Deprecated("型推論が正確とは限りません。")
    override fun add(key: String, value: Any): Insert {
        return add(DataType.getTypeListByAny(value).getOrNull(0)?: DataType.VARCHAR, key, value)
    }

    override fun build(force: Boolean): String {
        TODO("Not yet implemented")
    }

    override fun send(force: Boolean) {
        connection.sendUpdate(build(force))
    }
}