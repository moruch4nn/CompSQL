package dev.moru3.compsql.mysql.update.insert

import dev.moru3.compsql.Connection
import dev.moru3.compsql.DataHub.Companion.connection
import dev.moru3.compsql.DataType
import dev.moru3.compsql.NativeDataType
import dev.moru3.compsql.Insert
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
        return add(DataType.getTypeListByAny(value).getOrNull(0)?: DataType.VARCHAR, key, value)
    }

    override fun build(force: Boolean): PreparedStatement =
        connection.safeConnection.prepareStatement(
            buildString {
                append("INSERT")
                if(!force) { append(" IGNORE") }
                append(" INTO ").append(table.name).append(" (").append(values.keys.joinToString(", ")).append(")")
                    .append(" VALUES (")
                    .append(MutableList(values.size){"?"})
                    .append(")")
            }
        ).also { values.values.forEachIndexed { index, pair -> pair.first.set(it, index, pair.second) } }

    override fun send(force: Boolean) {
        connection.sendUpdate(build(force))
    }
}