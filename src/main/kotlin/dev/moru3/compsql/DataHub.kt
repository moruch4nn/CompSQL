package dev.moru3.compsql

class DataHub {
    companion object {
        private var connection1: Connection? = null

        fun setConnection(connection: Connection) { this.connection1 = connection }

        val connection: Connection get() = checkNotNull(connection1) { "No connection has been created." }
    }
}