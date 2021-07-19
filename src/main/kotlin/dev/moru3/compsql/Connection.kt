package dev.moru3.compsql

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

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

    /**
     * SQLにQueryを送信します。例: SELECT
     */
    fun sendQuery(sql: String, vararg params: Any): ResultSet

    /**
     * SQLにUpdateを送信します。例: TABLE, INSERT, UPSERT
     */
    fun sendUpdate(sql: String, vararg params: Any)

    /**
     * SQLにUpdateを送信します。例: TABLE, INSERT, UPSERT
     */
    fun sendUpdate(preparedStatement: PreparedStatement)
}