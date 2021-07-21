package dev.moru3.compsql.mysql.update.insert

import dev.moru3.compsql.DataHub.Companion.connection
import dev.moru3.compsql.Insert
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.table.Table
import java.sql.PreparedStatement

class MySQLInsert(override val table: Table) : Insert {

    val values = mutableMapOf<String, Pair<DataType<*, *>, Any>>()

    override fun add(type: DataType<*, *>, key: String, value: Any): Insert {
        check(type.type.isInstance(value)) { "The type of the specified value does not match the `type` in DataType." }
        values[key] = type to value
        return this
    }

    /**
     * valueから型を推論します。推論できない場合はVARCHARに変換されます。
     */
    override fun add(key: String, value: Any): Insert {
        return add(checkNotNull(DataType.getTypeListByAny(value).getOrNull(0)) { "`${value}`に対応する型が見つかりません。" }, key, value)
    }

    override fun build(force: Boolean): PreparedStatement {
        val result = buildAsRaw(force)
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, any -> checkNotNull(DataType.getTypeListByAny(any).getOrNull(0)) { "`${any::class.java.simpleName}`に対応する型が見つかりません。" }.set(preparedStatement, index+1, any) }
        return preparedStatement
    }

    override fun build(): PreparedStatement = build(false)

    override fun send(force: Boolean) {
        connection.sendUpdate(build(force))
    }

    override fun buildAsRaw(): Pair<String, List<Any>> = buildAsRaw(false)

    override fun buildAsRaw(force: Boolean): Pair<String, List<Any>> {
        val result = buildString {
            append("INSERT")
            if(!force) { append(" IGNORE") }
            append(" INTO ").append(table.name).append(" (").append(values.keys.joinToString(", ")).append(")")
                .append(" VALUES (")
                .append(MutableList(values.size){"?"}.joinToString(","))
                .append(")")
        }
        val valueList = values.map { it.value.second }
        return result to valueList.toList()
    }
}