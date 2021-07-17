package dev.moru3.compsql.interfaces

import dev.moru3.compsql.Connection

interface SendSyntax: Syntax {
    /**
     * SQLと同期します。
     */
    fun send(connection: Connection, force: Boolean)
}