package dev.moru3.compsql.mysql.query.select

import dev.moru3.compsql.abstracts.AbstractSelect
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.KeyedWhere
import dev.moru3.compsql.syntax.FirstWhere
import dev.moru3.compsql.syntax.table.Table

class MySQLSelect(table: Table, vararg columns: String): AbstractSelect(table, *columns) {

    override fun where(key: String): KeyedWhere = MySQLWhere().also { this.where = it }.key(key)


    override fun limit(limit: Int): FirstWhere = MySQLWhere().limit(limit).also { this.where = it }

    override var where: FirstWhere = MySQLWhere()

    constructor(table: Table, where: FirstWhere, vararg columns: String): this(table, *columns) {
        this.where = where
    }

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*,*>>>> {
        buildString {
            val raw = where.buildAsRaw()
            append("SELECT ${columns.joinToString(", ")} FROM ${table.name}")
            orderBy?.also { append(" ORDER BY ${it.first} ${it.second}") }
            append(raw.first)
            return this.toString() to raw.second
        }
    }
}