package dev.moru3.compsql

import java.io.InputStream
import java.math.BigDecimal
import java.sql.Date
import java.sql.ResultSet
import java.sql.Time
import java.sql.Timestamp

abstract class Database: SQL {

    override val isClosed: Boolean get() = connection.isClosed||!connection.isValid(timeout)

    override fun close() {
        connection.close()
    }

    override fun sendQuery(sql: String, vararg params: Any): ResultSet {
        safeConnection.prepareStatement(sql).also { ps ->
            params.forEachIndexed { index, any ->
                when(any) {
                    is Boolean -> ps.setBoolean(index, any)
                    is Byte -> ps.setByte(index, any)
                    is Short -> ps.setShort(index, any)
                    is Int -> ps.setInt(index, any)
                    is Long -> ps.setLong(index, any)
                    is Float -> ps.setFloat(index, any)
                    is Double -> ps.setDouble(index, any)
                    is BigDecimal -> ps.setBigDecimal(index, any)
                    is String -> ps.setString(index, any)
                    is ByteArray -> ps.setBytes(index, any)
                    is Date -> ps.setDate(index, any)
                    is Time -> ps.setTime(index, any)
                    is Timestamp -> ps.setTimestamp(index, any)
                    is InputStream -> ps.setAsciiStream(index, any)
                    else -> ps.setObject(index, any)
                }
            }
            return ps.executeQuery().also { ps.close() }
        }
    }

    override fun sendUpdate(sql: String, vararg params: Any) {
        safeConnection.prepareStatement(sql).also { ps ->
            params.forEachIndexed { index, any ->
                when(any) {
                    is Boolean -> ps.setBoolean(index, any)
                    is Byte -> ps.setByte(index, any)
                    is Short -> ps.setShort(index, any)
                    is Int -> ps.setInt(index, any)
                    is Long -> ps.setLong(index, any)
                    is Float -> ps.setFloat(index, any)
                    is Double -> ps.setDouble(index, any)
                    is BigDecimal -> ps.setBigDecimal(index, any)
                    is String -> ps.setString(index, any)
                    is ByteArray -> ps.setBytes(index, any)
                    is Date -> ps.setDate(index, any)
                    is Time -> ps.setTime(index, any)
                    is Timestamp -> ps.setTimestamp(index, any)
                    is InputStream -> ps.setAsciiStream(index, any)
                    else -> ps.setObject(index, any)
                }
            }
            ps.executeUpdate().also { ps.close() }
        }
    }
}