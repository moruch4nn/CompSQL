package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.abstracts.AbstractSelect
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.OrderType
import dev.moru3.compsql.syntax.SelectKeyedWhere
import dev.moru3.compsql.syntax.SelectWhere
import dev.moru3.compsql.syntax.table.Table

class MySQLSelect(table: Table, vararg columns: String): AbstractSelect(table, *columns) {

    override fun where(key: String): SelectKeyedWhere = MySQLSelectWhere().also { this.where = it }.key(key)

    override fun orderBy(table: String, orderType: OrderType): SelectWhere = MySQLSelectWhere().orderBy(table, orderType).also { this.where = it }

    override fun orderBy(vararg values: Pair<String, OrderType>): SelectWhere = MySQLSelectWhere().orderBy(*values).also { this.where = it }

    override fun limit(limit: Int): SelectWhere = MySQLSelectWhere().limit(limit).also { this.where = it }

    override var where: SelectWhere = MySQLSelectWhere()

    constructor(table: Table, where: SelectWhere, vararg columns: String): this(table, *columns) {
        this.where = where
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*>>>> {
        buildString {
            val raw = where.buildAsRaw()
            append("SELECT ${columns.joinToString(", ")} FROM ${table.name}${raw.first}")
            return this.toString() to raw.second
        }
    }
}