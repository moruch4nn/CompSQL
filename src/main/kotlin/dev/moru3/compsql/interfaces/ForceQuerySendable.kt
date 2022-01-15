package dev.moru3.compsql.interfaces

import dev.moru3.compsql.Connection
import java.sql.ResultSet

interface ForceUpdateSendable: Syntax, ConnectionValue {
    /**
     * 変更内容をSQLと同期します。
     */
    fun send(force: Boolean)
}

interface NonForceUpdateSendable: Syntax, ConnectionValue {
    /**
     * 変更内容をSQLと同期します。
     */
    fun send()
}

interface QuerySendable: Syntax, ConnectionValue {
    /**
     * 変更内容をSQLと同期します。
     */
    fun send(): ResultSet
}

interface ConnectionValue {
    /**
     * JDBCドライバのコネクションが格納されています。
     */
    val connection: Connection
}