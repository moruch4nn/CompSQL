package dev.moru3.compsql.mysql.update.delete

import dev.moru3.compsql.abstracts.AbstractDelete
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.mysql.query.select.MySQLWhere
import dev.moru3.compsql.syntax.KeyedWhere
import dev.moru3.compsql.syntax.FirstWhere
import dev.moru3.compsql.syntax.table.Table

class MySQLDelete(table: Table): AbstractDelete(table) {

    override var where: FirstWhere = MySQLWhere()

    override fun where(key: String): KeyedWhere = MySQLWhere().also { this.where = it }.key(key)

    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*,*>>>> {
        buildString {
            val raw = where.buildAsRaw()
            append("DELETE FROM ${table.name}${raw.first}")
            return this.toString() to raw.second
        }
    }
}