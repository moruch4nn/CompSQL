package dev.moru3.compsql.mysql.update.insert

import dev.moru3.compsql.DataHub.connection
import dev.moru3.compsql.DataHub.getTypeListByAny
import dev.moru3.compsql.Upsert
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.table.Table
import java.sql.PreparedStatement

class MySQLUpsert(override val table: Table) : Upsert {

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
        return add(checkNotNull(getTypeListByAny(value).getOrNull(0)) { "`${value}`に対応する型が見つかりません。" }, key, value)
    }

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*, *>>>> {
        val insert = MySQLInsert(table).also { values.forEach { (key, pair) -> it.add(pair.first, key, pair.second) }
        }.buildAsRaw(true)
        val keys = values.values.map { it.second to it.first }.toMutableList()
        val sql =
            "${insert.first} ON DUPLICATE KEY UPDATE ${values.map { (key, pair) -> keys.add(pair.second to pair.first);"${key}=?" }.joinToString(",")}"
        return sql to keys
    }

    override fun send() {
        connection.sendUpdate(build())
    }

}