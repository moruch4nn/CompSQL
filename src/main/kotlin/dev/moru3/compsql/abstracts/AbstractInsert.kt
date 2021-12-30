package dev.moru3.compsql.abstracts

import dev.moru3.compsql.Connection
import dev.moru3.compsql.TypeHub
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.Insert
import dev.moru3.compsql.syntax.table.Table
import java.sql.PreparedStatement

abstract class AbstractInsert(final override val table: Table): Insert {
    val values = mutableMapOf<String, Pair<DataType<*,*>, Any>>()

    override val connection: Connection = table.connection

    override fun add(type: DataType<*,*>, key: String, value: Any): Insert {
        check(type.type.isInstance(value)) { "The type of the specified value does not match the `type` in DataType." }
        values[key] = type to value
        return this
    }

    /**
     * valueから型を推論します。推論できない場合はIllegalStateExceptionが発生します。
     */
    override fun add(key: String, value: Any): Insert {
        return add(checkNotNull(TypeHub[value::class.java].getOrNull(0)) { "`${value::class.java.name}`に対応する型が見つかりません。" }, key, value)
    }

    override fun build(force: Boolean): PreparedStatement {
        val result = buildAsRaw(force)
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun build(): PreparedStatement = build(false)

    override fun send(force: Boolean) {
        connection.sendUpdate(build(force))
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*,*>>>> = buildAsRaw(false)
}