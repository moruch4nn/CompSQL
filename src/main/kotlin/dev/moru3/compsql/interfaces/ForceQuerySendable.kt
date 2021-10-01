package dev.moru3.compsql.interfaces

import dev.moru3.compsql.Connection
import dev.moru3.compsql.ResultSet

interface ForceUpdateSendable: Syntax {
    val connection: Connection
    /**
     * SQLと同期します。
     */
    fun send(force: Boolean)
}

interface NonForceUpdateSendable: Syntax {
    val connection: Connection
    /**
     * SQLと同期します。
     */
    fun send()
}

interface QuerySendable: Syntax {
    val connection: Connection
    /**
     * SQLと同期します。
     */
    fun send(): ResultSet
}