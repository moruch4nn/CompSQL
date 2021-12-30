package dev.moru3.compsql.mysql.update.insert

import dev.moru3.compsql.abstracts.AbstractUpsert
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.syntax.table.Table

class MySQLUpsert(table: Table) : AbstractUpsert(table) {

    override fun buildAsRaw(): Pair<String, List<Pair<Any, DataType<*,*>>>> {
        val insert = MySQLInsert(table).also { values.forEach { (key, pair) -> it.add(pair.first, key, pair.second) }
        }.buildAsRaw(true)
        val keys = values.values.map { it.second to it.first }.toMutableList()
        val sql =
            "${insert.first} ON DUPLICATE KEY UPDATE ${values.map { (key, pair) -> keys.add(pair.second to pair.first);"${key}=?" }.joinToString(",")}"
        return sql to keys
    }
}