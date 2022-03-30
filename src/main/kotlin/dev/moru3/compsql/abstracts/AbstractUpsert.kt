package dev.moru3.compsql.abstracts

import dev.moru3.compsql.Connection
import dev.moru3.compsql.TypeHub
import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.syntax.Upsert
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

abstract class AbstractUpsert(final override val table: Table): Upsert {

    override val connection: Connection = table.connection

    val values = mutableMapOf<String, Pair<BaseDataType<*,*>, Any>>()

    override fun add(type: BaseDataType<*,*>, key: String, value: Any): Upsert {
        check(type.type.isInstance(value)) { "The type of the specified value does not match the `${value::class.java.toString()}` in DataType.(key: ${key})" }
        values[key] = type to value
        return this
    }

    /**
     * valueから型を推論します。推論できない場合はVARCHARに変換されます。
     */
    override fun add(key: String, value: Any): Upsert {
        return add(checkNotNull(TypeHub[value::class.javaObjectType].getOrNull(0)) { "`${value}`に対応する型が見つかりません。" }, key, value)
    }

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun send() = connection.sendUpdate(build())
}