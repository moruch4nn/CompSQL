package dev.moru3.compsql

interface Connection {
    val connection: java.sql.Connection

    fun reconnect(force: Boolean)
}