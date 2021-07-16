package dev.moru3.compsql

import java.io.Closeable
import java.sql.Connection

interface CompSQL: Closeable {
    val connection: Connection

    val timeout: Int

    /**
     * データベースに再接続します。
     */
    fun reconnect(force: Boolean)

    /**
     * 接続が既に閉じているか返します。
     */
    val isClosed: Boolean
}