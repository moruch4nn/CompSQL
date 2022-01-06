package dev.moru3.compsql.interfaces

import dev.moru3.compsql.Connection
import java.sql.ResultSet

interface ForceUpdateSendable: Syntax {
    val connection: Connection
    /**
     * 変更内容をSQLと同期します。
     */
    fun send(force: Boolean)
}

interface NonForceUpdateSendable: Syntax {
    val connection: Connection
    /**
     * 変更内容をSQLと同期します。
     */
    fun send()
}

interface QuerySendable: Syntax {
    val connection: Connection
    /**
     * 変更内容をSQLと同期します。
     */
    fun send(): ResultSet
}