package dev.moru3.compsql.sqlite.update.insert

import dev.moru3.compsql.abstracts.AbstractUpsert
import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.syntax.table.Table

class SQLiteUpsert(table: Table) : AbstractUpsert(table) {

    override fun buildAsRaw(): Pair<String, List<Pair<Any, BaseDataType<*,*>>>> {
        val insert = SQLiteInsert(table).also { values.forEach { (key, pair) -> it.add(pair.first, key, pair.second) }
        }.buildAsRaw(true)
        val keys = values.values.map { it.second to it.first }.toMutableList()
        val sql =
            "${insert.first} ON DUPLICATE KEY UPDATE ${values.map { (key, pair) -> keys.add(pair.second to pair.first);"${key}=?" }.joinToString(",")}"
        return sql to keys
    }
}