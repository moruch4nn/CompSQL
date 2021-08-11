package dev.moru3.compsql

import dev.moru3.compsql.connection.MySQLConnection
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.numeric.INTEGER
import dev.moru3.compsql.datatype.types.text.VARCHAR
import java.util.*

fun main() {
    println("Int::class.java.isInstance(100) ::: ${Int::class.java.isInstance(100)}")
    println("String::class.java.isInstance(\"こんにちは\") ::: ${String::class.java.isInstance("こんにちは")}")

    println()

    MySQLConnection("127.0.0.1", "test", "root", "MariaDB-0348", TreeMap<String, Any>().also { it["characterEncoding"] = "utf8" }) {
        table("test_table", false) {
            column("id", INTEGER(40)) {
                it.isAutoIncrement = true
                it.isPrimaryKey = true
            }
            column("name", VARCHAR(30)) {
                it.isNotNull = true
                it.isPrimaryKey = true
                it.property = 50
            }
            column("age", DataType.INTEGER) {
                it.isNotNull = true
            }
            println(build(false).toString())
        }

        insert("test_table", false) { add("name", "moru3_48");add("age", 15) }

        insert("test_table", false) { add("name", "moru3_49");add("age", 16) }

        upsert("test_table") {
            add("name", "moru3_50")
            add("age", 20)
            println(build().toString())
        }
    }
}