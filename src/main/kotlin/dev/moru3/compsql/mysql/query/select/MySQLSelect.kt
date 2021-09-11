package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.abstract.AbstractSelect
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.SelectWhere
import dev.moru3.compsql.syntax.table.Table

class MySQLSelect(table: Table, vararg columns: String): AbstractSelect(table, *columns) {

    override var where: SelectWhere = MySQLSelectWhere()

    constructor(table: Table, where: SelectWhere, vararg columns: String): this(table, *columns) {
        this.where = where
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*, *>>>> {
        buildString {
            val raw = where.buildAsRaw()
            append("SELECT ${columns.joinToString(", ")} FROM ${table.name}${raw.first}")
            return this.toString() to raw.second
        }
    }
}