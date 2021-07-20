package dev.moru3.compsql.mysql.update.insert

import dev.moru3.compsql.DataHub.Companion.connection
import dev.moru3.compsql.DataType
import dev.moru3.compsql.Upsert
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
        return add(checkNotNull(DataType.getTypeListByAny(value).getOrNull(0)) { "`${value}`に対応する型が見つかりません。" }, key, value)
    }

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, any -> checkNotNull(DataType.getTypeListByAny(any).getOrNull(0)) { "`${any}`に対応する型が見つかりません。" }.set(preparedStatement, index+1, any) }
        return preparedStatement
    }

    override fun buildAsRaw(): Pair<String, List<Any>> {
        val insert = MySQLInsert(table).also {
            values.forEach { (key, any) -> it.add(any.first, key, any.second) }
        }.buildAsRaw(true)
        val keys = insert.second.toMutableList()
        val sql =
            "${insert.first} ON CONFLICT(${values.keys.joinToString(",")}) DO UPDATE SET ${values.map { (key, any) -> keys.add(any.second);"${key}=?" }.joinToString(",")}"
        return sql to keys
    }

    override fun send() {
        connection.sendUpdate(build())
    }

}