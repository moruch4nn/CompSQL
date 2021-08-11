package dev.moru3.compsql

import dev.moru3.compsql.datatype.DataType
import java.io.InputStream
import java.math.BigDecimal
import java.sql.*

abstract class Database: SQL {

    override val isClosed: Boolean get() = connection.isClosed||!connection.isValid(timeout)

    override fun close() {
        connection.close()
    }

    private fun setParamsToPrepareStatement(ps: PreparedStatement, vararg params: Any): PreparedStatement {
        params.forEachIndexed { index, any ->
            when(any) {
                is Boolean -> ps.setBoolean(index+1, any)
                is Byte -> ps.setByte(index+1, any)
                is Short -> ps.setShort(index+1, any)
                is Int -> ps.setInt(index+1, any)
                is Long -> ps.setLong(index+1, any)
                is Float -> ps.setFloat(index+1, any)
                is Double -> ps.setDouble(index+1, any)
                is BigDecimal -> ps.setBigDecimal(index+1, any)
                is String -> ps.setString(index+1, any)
                is ByteArray -> ps.setBytes(index+1, any)
                is Date -> ps.setDate(index+1, any)
                is Time -> ps.setTime(index+1, any)
                is Timestamp -> ps.setTimestamp(index+1, any)
                is InputStream -> ps.setAsciiStream(index+1, any)
                else -> ps.setObject(index+1, any)
            }
        }
        return ps
    }

    override fun sendQuery(sql: String, vararg params: Any): ResultSet {
        safeConnection.prepareStatement(sql).also { ps -> return sendQuery(setParamsToPrepareStatement(ps, params)) }
    }

    override fun sendQuery(preparedStatement: PreparedStatement): ResultSet = preparedStatement.executeQuery().also { preparedStatement.close() }

    override fun sendUpdate(preparedStatement: PreparedStatement) {
        preparedStatement.also { ps -> ps.executeUpdate();ps.close() }
    }

    override fun sendUpdate(sql: String, vararg params: Any) {
        safeConnection.prepareStatement(sql).also { ps -> sendUpdate(setParamsToPrepareStatement(ps, params)) }
    }
}