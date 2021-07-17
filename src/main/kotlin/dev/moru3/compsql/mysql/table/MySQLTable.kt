package dev.moru3.compsql.mysql.table

import dev.moru3.compsql.Connection
import dev.moru3.compsql.DataType
import dev.moru3.compsql.mysql.table.column.MySQLColumn
import dev.moru3.compsql.table.AfterTable
import dev.moru3.compsql.table.Table
import dev.moru3.compsql.table.column.Column

class MySQLTable(override val name: String): Table {

    override val after: AfterTable = MySQLAfterTable(name)

    private val columns: MutableList<Column> = mutableListOf()

    override fun column(name: String, type: DataType<*>, action: (Column) -> Unit): Table {
        return column(MySQLColumn(name, type).apply(action))
    }

    override fun column(column: Column): Table {
        columns.add(column)
        return this
    }

    override fun build(): String = build(false)

    override fun build(force: Boolean): String = buildString {
        append("CREATE TABLE ");if(!force) append("IF NOT EXISTS $name");append(" (")
        val primaryKeys: List<Column> = columns.filter(Column::isPrimaryKey)
        val autoIncrements: List<Column> = columns.filter(Column::isAutoIncrement)
        val uniqueIndexes: List<Column> = columns.filter(Column::isUniqueIndex)
        check(autoIncrements.size in 0..1) { "The maximum number of AI that can be set is 1." }
        val columnList: MutableList<String> = mutableListOf()
        val primaryKeyList: MutableList<String> = mutableListOf()
        columns.map(Column::build).forEach(columnList::add)
        columns.map(Column::name).forEach(primaryKeyList::add)
        if(primaryKeys.isNotEmpty()) columnList.add("PRIMARY KEY (`${primaryKeyList.joinToString(", ")}`)")
        if(uniqueIndexes.isNotEmpty()) uniqueIndexes.forEach { columnList.add("UNIQUE `${name}_UNIQUE` (`${it.name}` ASC) VISIBLE") }
        append(columnList.joinToString(", ")).append(")")
    }

    override fun send(connection: Connection,force: Boolean) {
        connection.reconnect(false) //接続が切れている場合に再接続する
        val ps = connection.connection.prepareStatement(build(force))
        ps.executeUpdate()
        ps.close()
    }
}