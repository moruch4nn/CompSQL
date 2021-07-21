package dev.moru3.compsql

import dev.moru3.compsql.connection.MySQLConnection
import dev.moru3.compsql.datatype.DataType
import java.util.*

fun main() {
    MySQLConnection("127.0.0.1", "test", "test", "MySQL-0348", TreeMap<String, Any>().also { it["characterEncoding"] = "utf8" }) {
        table("test_table", false) {
            column("id", DataType.INTEGER) {
                it.isAutoIncrement = true
                it.isPrimaryKey = true
            }
            column("name", DataType.VARCHAR) {
                it.isNotNull = true
                it.isPrimaryKey = true
                it.property = 50
            }
            column("age", DataType.INTEGER) {
                it.isNotNull = true
            }
            println(build(false))
        }

        insert("test_table", false) { add("name", "moru3_48");add("age", 15) }

        insert("test_table", false) { add("name", "moru3_49");add("age", 16) }

        upsert("test_table") {
            add("name", "moru3_50")
            add("age", 20)
            println(build())
        }
    }
}

class Test {
    companion object {
        val test: ()->String = { "vaka" }
    }
}