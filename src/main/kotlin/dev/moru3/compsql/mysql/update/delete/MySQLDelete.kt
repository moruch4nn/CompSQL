package dev.moru3.compsql.mysql.update.delete

import dev.moru3.compsql.abstract.AbstractDelete
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.table.Table

class MySQLDelete(table: Table): AbstractDelete(table) {
    override fun buildAsRaw(): Pair<String, List<Pair<Any?, DataType<*, *>>>> {
        buildString {
            val raw = where.buildAsRaw()
            append("DELETE FROM ${table.name}${raw.first}")
            return this.toString() to raw.second
        }
    }
}