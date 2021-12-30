package dev.moru3.compsql.abstracts

import dev.moru3.compsql.Connection
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.table.AfterTable
import dev.moru3.compsql.syntax.table.Table
import dev.moru3.compsql.syntax.table.column.Column
import java.sql.PreparedStatement

abstract class AbstractTable(override val connection: Connection, n: String): Table {

    final override var name: String = n
        set(value) {
            connection.sendUpdate("RENAME TABLE $field to $value")
            field = value
        }

    protected val columns: MutableList<Column> = mutableListOf()

    override fun column(name: String, type: DataType<*,*>, action: (Column) -> Unit): Column {
        return column(name, type).apply(action)
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*,*>>>>  = buildAsRaw(false)

    override fun build(): PreparedStatement = build(false)

    override fun build(force: Boolean): PreparedStatement {
        val result = buildAsRaw(force)
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun send(force: Boolean) = connection.sendUpdate(build(force))
}