package dev.moru3.compsql.mysql.update.table

import dev.moru3.compsql.Connection
import dev.moru3.compsql.DataType
import dev.moru3.compsql.IDataType
import dev.moru3.compsql.mysql.update.table.column.MySQLColumn
import dev.moru3.compsql.table.AfterTable
import dev.moru3.compsql.table.Table
import dev.moru3.compsql.table.column.Column
import java.sql.PreparedStatement

class MySQLTable(val connection: Connection, n: String): Table {

    override var name: String = n
        set(value) {
            connection.sendUpdate("RENAME TABLE $field to $value")
            field = value
        }

    override val after: AfterTable = MySQLAfterTable(name)

    private val columns: MutableList<Column> = mutableListOf()

    override fun column(name: String, type: IDataType<*, *>, action: (Column) -> Unit): Table {
        return column(MySQLColumn(name, type).apply(action))
    }

    override fun column(column: Column): Table {
        columns.add(column)
        return this
    }

    override fun build(): PreparedStatement = build(false)

    override fun buildAsRaw(): Pair<String, List<Any>>  = buildAsRaw(false)

    override fun buildAsRaw(force: Boolean): Pair<String, List<Any>> {
        val valueList = mutableListOf<Any>()
        val result = buildString {
            append("CREATE TABLE ");if(!force) append("IF NOT EXISTS $name");append(" (")
            val primaryKeys: List<Column> = columns.filter(Column::isPrimaryKey)
            // val autoIncrements: List<Column> = columns.filter(Column::isAutoIncrement)
            val uniqueIndexes: List<Column> = columns.filter(Column::isUniqueIndex)
            val columnList: MutableMap<String, List<Any>> = mutableMapOf()
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

    override fun build(force: Boolean): PreparedStatement {
        val result = buildAsRaw(force)
        val preparedStatement = connection.safeConnection.prepareStatement(result.first)
        val keys = result.second
        keys.forEachIndexed { index, any -> checkNotNull(DataType.getTypeListByAny(any).getOrNull(0)) { "`${any}`に対応する型が見つかりません。" }.set(preparedStatement, index+1, any) }
        return preparedStatement
    }

    override fun send(force: Boolean) = connection.sendUpdate(build(force))
}