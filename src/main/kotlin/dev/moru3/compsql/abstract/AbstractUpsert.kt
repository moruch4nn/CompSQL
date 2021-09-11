package dev.moru3.compsql.abstract

import dev.moru3.compsql.DataHub
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.Upsert
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

abstract class AbstractUpsert(override val table: Table): Upsert {
    val values = mutableMapOf<String, Pair<DataType<*, *>, Any>>()

    override fun add(type: DataType<*, *>, key: String, value: Any): Upsert {
        check(type.type.isInstance(value)) { "The type of the specified value does not match the `type` in DataType." }
        values[key] = type to value
        return this
    }

    /**
     * valueから型を推論します。推論できない場合はVARCHARに変換されます。
     */
    override fun add(key: String, value: Any): Upsert {
        return add(checkNotNull(DataHub.getTypeListByAny(value).getOrNull(0)) { "`${value}`に対応する型が見つかりません。" }, key, value)
    }

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = DataHub.connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun send() {
        DataHub.connection.sendUpdate(build())
    }
}