package dev.moru3.compsql

import java.sql.Connection

interface Connection {
    /**
     * コネクションを返します。
     */
    val connection: Connection

    /**
     * コネクションを返します。既に閉じている場合は再接続します。
     */
    val safeConnection: Connection

    /**
     * リコネクト。
     */
    fun reconnect(force: Boolean): Connection

    /**
     * クローズ
     */
    fun close()
}