package dev.moru3.compsql.connection

import dev.moru3.compsql.Database
import dev.moru3.compsql.table.Table
import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class SQLiteConnection(private val url: String, override val timeout: Int = 10): Database() {

    constructor(file: File, timeout: Int = 10): this("jdbc:sqlite:${file.absolutePath}", timeout)

    override var connection: Connection = DriverManager.getConnection(url)
        private set

    override fun table(table: Table, force: Boolean) {
        TODO("Not yet implemented")
    }

    override fun table(name: String, force: Boolean, action: Table.() -> Unit) {
        TODO("Not yet implemented")
    }

    override fun reconnect(force: Boolean) {
        if(this.isClosed) connection.close()
        connection = DriverManager.getConnection(url)
    }
}