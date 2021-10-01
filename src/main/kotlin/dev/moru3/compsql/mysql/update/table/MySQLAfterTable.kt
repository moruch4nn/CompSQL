package dev.moru3.compsql.mysql.update.table

import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.interfaces.QuerySendable
import dev.moru3.compsql.mysql.update.table.column.MySQLColumn
import dev.moru3.compsql.syntax.table.AfterTable
import dev.moru3.compsql.syntax.table.column.Column
import java.sql.PreparedStatement

class MySQLAfterTable(override val name: String): AfterTable {
    override fun add(name: String, type: DataType<*>, action: (Column) -> Unit): QuerySendable {
        return add(MySQLColumn(name, type).apply(action))
    }

    override fun delete(name: String): QuerySendable {
        return object: QuerySendable {
            override fun build(): PreparedStatement { return connection.safeConnection.prepareStatement(buildAsRaw().first) }
            override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> { return "ALTER TABLE $name DROP COLUMN $name" to mutableListOf() }
            override fun send() { connection.sendUpdate(build()) }
        }
    }

    override fun rename(old: String, new: String): QuerySendable {
        return object: QuerySendable {
            override fun build(): PreparedStatement { return connection.safeConnection.prepareStatement(buildAsRaw().first) }
            override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> { return "ALTER TABLE $name RENAME COLUMN $old TO $new" to mutableListOf() }
            override fun send() { connection.sendUpdate(build()) }
        }
    }

    override fun add(column: Column): QuerySendable {
        return object: QuerySendable {
            override fun build(): PreparedStatement { return connection.safeConnection.prepareStatement(buildAsRaw().first) }
            override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> { return "ALTER TABLE $name ADD (${column.buildAsRaw()})" to mutableListOf() }
            override fun send() { connection.sendUpdate(build()) }
        }
    }
}