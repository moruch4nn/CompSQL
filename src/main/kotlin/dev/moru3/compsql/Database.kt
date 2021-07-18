package dev.moru3.compsql

abstract class Database: CompSQL {

    override val isClosed: Boolean get() = connection.isClosed||!connection.isValid(timeout)

    override fun close() {
        connection.close()
    }
}