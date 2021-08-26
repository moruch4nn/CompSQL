package dev.moru3.compsql

import dev.moru3.compsql.connection.MariaDBConnection

fun main() {
    val con = MariaDBConnection("localhost", "friendsrv", "root", "MariaDB-0348", mapOf())
}