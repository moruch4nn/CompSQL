package dev.moru3.compsql.abstract

import dev.moru3.compsql.Connection
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.mysql.update.table.MySQLAfterTable
import dev.moru3.compsql.mysql.update.table.column.MySQLColumn
import dev.moru3.compsql.syntax.table.AfterTable
import dev.moru3.compsql.syntax.table.Table
import dev.moru3.compsql.syntax.table.column.Column
import java.sql.PreparedStatement

abstract class AbstractTable(val connection: Connection, n: String): Table {

    final override var name: String = n
        set(value) {
            connection.sendUpdate("RENAME TABLE $field to $value")
            field = value
        }

    override val after: AfterTable = MySQLAfterTable(name)

    protected val columns: MutableList<Column> = mutableListOf()

    override fun column(name: String, type: DataType<*, *>, action: (Column) -> Unit): Column {
        return column(name, type).apply(action)
    }

    override fun column(name: String, type: DataType<*, *>): Column {
        val column =  MySQLColumn(name, type)
        columns.add(column)
        return column
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