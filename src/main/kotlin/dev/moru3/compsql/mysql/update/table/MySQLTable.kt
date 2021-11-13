package dev.moru3.compsql.mysql.update.table

import dev.moru3.compsql.Connection
import dev.moru3.compsql.abstracts.AbstractTable
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.mysql.update.table.column.MySQLColumn
import dev.moru3.compsql.syntax.table.AfterTable
import dev.moru3.compsql.syntax.table.column.Column

class MySQLTable(connection: Connection, n: String): AbstractTable(connection, n) {

    override val after: AfterTable = MySQLAfterTable(name, connection)

    override fun column(name: String, type: DataType<*>): Column {
        val column =  MySQLColumn(name, type)
        columns.add(column)
        return column
    }

    override fun buildAsRaw(force: Boolean): Pair<String, List<Pair<Any?, DataType<*>>>> {
        val valueList = mutableListOf<Pair<Any?, DataType<*>>>()
        val result = buildString {
            append("CREATE TABLE ");if(!force) append("IF NOT EXISTS $name") else append(name);append(" (")
            val primaryKeys: List<Column> = columns.filter(Column::isPrimaryKey)
            // val autoIncrements: List<Column> = columns.filter(Column::isAutoIncrement)
            val uniqueIndexes: List<Column> = columns.filter(Column::isUniqueIndex)
            val columnList: MutableMap<String, List<Pair<Any?, DataType<*>>>> = mutableMapOf()
            val primaryKeyList: MutableList<String> = mutableListOf()
            columns.map(Column::buildAsRaw).forEach{ columnList[it.first] = it.second }
            columns.filter(Column::isPrimaryKey).map(Column::name).forEach(primaryKeyList::add)
            if(primaryKeys.isNotEmpty()) columnList["PRIMARY KEY (${primaryKeyList.map { "`${it}`" }.joinToString(", ")})"] = listOf()
            if(uniqueIndexes.isNotEmpty()) uniqueIndexes.forEach { columnList["UNIQUE `${name}_UNIQUE` (`${it.name}` ASC) VISIBLE"] = listOf() }
            columnList.values.forEach(valueList::addAll)
            append(columnList.keys.joinToString(", ")).append(")")
        }
        return result to valueList
    }
}