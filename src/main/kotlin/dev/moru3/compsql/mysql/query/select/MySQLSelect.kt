package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.*
import dev.moru3.compsql.DataHub.connection
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.table.Table
import java.sql.PreparedStatement
import java.sql.ResultSet

class MySQLSelect(val table: Table, vararg columns: String): Select {

    override var where: SelectWhere = MySQLSelectWhere()

    constructor(table: Table, where: SelectWhere, vararg columns: String): this(table, *columns) {
        this.where = where
    }

    private val columns = columns.toList()

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*, *>>>> {
        buildString {
            val raw = where.buildAsRaw()
            append("SELECT ${columns.joinToString(", ")} FROM ${table.name}${raw.first}")
            return this.toString() to raw.second
        }
    }

    override fun build(): PreparedStatement {
        val result = buildAsRaw()
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, pair -> pair.second.set(preparedStatement, index+1, pair.first) }
        return preparedStatement
    }

    override fun where(key: String): SelectKeyedWhere = MySQLSelectWhere().also { this.where = it }.key(key)

    override fun orderBy(table: String, orderType: OrderType): SelectWhere = MySQLSelectWhere().orderBy(table, orderType).also { this.where = it }

    override fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere = MySQLSelectWhere().orderBy(*values).also { this.where = it }

    override fun send(): ResultSet = connection.sendQuery(build())
}