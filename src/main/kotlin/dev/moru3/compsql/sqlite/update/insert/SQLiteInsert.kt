package dev.moru3.compsql.sqlite.update.insert

import dev.moru3.compsql.abstracts.AbstractInsert
import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.syntax.table.Table

class SQLiteInsert(table: Table) : AbstractInsert(table) {
    override fun buildAsRaw(force: Boolean): Pair<String, List<Pair<Any, BaseDataType<*,*>>>> {
        if(values.isEmpty()) { throw IllegalStateException("values is empty.") }
        val result = buildString {
            append("INSERT")
            if(!force) { append(" OR IGNORE") }
            append(" INTO ").append(table.name).append("(").append(values.keys.joinToString(", ")).append(")")
                .append(" VALUES(")
                .append(MutableList(values.size){"?"}.joinToString(","))
                .append(")")
        }
        val valueList = values.values.map { it.second to it.first }
        return result to valueList
    }
}