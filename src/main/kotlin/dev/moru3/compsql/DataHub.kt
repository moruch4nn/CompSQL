package dev.moru3.compsql

class DataHub {
    companion object {
        private var connection1: SQL? = null

        fun setConnection(connection: SQL) { this.connection1 = connection }

        val connection: SQL get() = checkNotNull(connection1) { "No connection has been created." }
    }
}