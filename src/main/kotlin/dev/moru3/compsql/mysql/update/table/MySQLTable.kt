package dev.moru3.compsql.mysql.update.table

import dev.moru3.compsql.Connection
import dev.moru3.compsql.DataHub.Companion.connection
import dev.moru3.compsql.DataType
import dev.moru3.compsql.DataType.Companion.VARCHAR
import dev.moru3.compsql.NativeDataType
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

    override fun column(name: String, type: DataType<*, *>, action: (Column) -> Unit): Table {
        return column(MySQLColumn(name, type).apply(action))
    }

    override fun column(column: Column): Table {
        columns.add(column)
        return this
    }

    override fun build(): PreparedStatement = build(false)

    override fun build(force: Boolean): PreparedStatement {
        val keys = mutableListOf<Any>()
        val preparedStatement = connection.safeConnection.prepareStatement(
            buildString {
                append("CREATE TABLE ");if(!force) append("IF NOT EXISTS $name");append(" (")
                val primaryKeys: List<Column> = columns.filter(Column::isPrimaryKey)
                val autoIncrements: List<Column> = columns.filter(Column::isAutoIncrement)
                val uniqueIndexes: List<Column> = columns.filter(Column::isUniqueIndex)
                check(autoIncrements.size in 0..1) { "The maximum number of AI that can be set is 1." }
                val columnList: MutableMap<String, List<Any>> = mutableMapOf()
                val primaryKeyList: MutableList<String> = mutableListOf()
                columns.map(Column::build).forEach{ columnList[it.first] = it.second }
                columns.map(Column::name).forEach(primaryKeyList::add)
                if(primaryKeys.isNotEmpty()) columnList["PRIMARY KEY (`${primaryKeyList.joinToString(", ")}`)"] = listOf()
                if(uniqueIndexes.isNotEmpty()) uniqueIndexes.forEach { columnList["UNIQUE `${name}_UNIQUE` (`${it.name}` ASC) VISIBLE"] = listOf() }
                columnList.values.forEach(keys::addAll)
                append(columnList.keys.joinToString(", ")).append(")")
            }
        )
        keys.forEachIndexed { index, any -> (DataType.getTypeListByAny(any).getOrNull(0)?:VARCHAR).set(preparedStatement, index+1, any) }
        return preparedStatement
    }

    override fun send(force: Boolean) = connection.sendUpdate(build(force))
}