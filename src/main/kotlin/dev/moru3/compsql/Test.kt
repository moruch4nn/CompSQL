package dev.moru3.compsql

import dev.moru3.compsql.annotation.Column
import dev.moru3.compsql.annotation.TableName
import dev.moru3.compsql.connection.MySQLConnection
import java.util.*

fun main() {

    /**MySQLConnection("127.0.0.1", "test", "root", "MariaDB-0348", TreeMap<String, Any>().also { it["characterEncoding"] = "utf8" }) {
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
    }**/

    MySQLConnection("127.0.0.1", "test", "root", "MariaDB-0348", mapOf()) {
        add(UserData(), false)
        val moru348 = UUID.randomUUID().toString()
        put(UserData(moru348, "moru3_48", 48), false)
        put(UserData(UUID.randomUUID().toString(), "moru3_64", 64), false)
        put(UserData(UUID.randomUUID().toString(), "moru3_10", 10), false)

        putOrUpdate(UserData(moru348, "moru3_48", 16))
    }
}

@TableName("userdata")
class UserData(uuid: String? = null, name: String? = null, age: Int? = null) {
    @Column("uuid", isPrimaryKey = true)
    val uuid: String = uuid?:""

    @Column("name")
    val name: String = name?:""

    @Column("age")
    val age: Int = age?:-1
}