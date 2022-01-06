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
     * コネクションを返します。既に閉じている場合は自動的に再接続します。
     */
    val safeConnection: Connection

    /**
     * データベースに再接続します。
     * @param force 強制的に正接続を行うかどうか
     */
    fun reconnect(force: Boolean): Connection

    /**
     * コネクションを閉じます。
     */
    fun close()

    /**
     * 送信後に結果が必要なクエリ文を送信する際に使用します。例: SELECT
     * @param sql SQLクエリ文。SQLインジェクションを対策するため、値は?に置換することをおすすめします。
     * @param params sqlで?に置換した値を入れることによってエスケープ処理後に?を置換します。
     *
     * @return 結果
     */
    fun sendQuery(sql: String, vararg params: Any): ResultSet

    /**
     * 送信後に結果が必要なクエリ文を送信する際に使用します。例: SELECT
     * @param preparedStatement 送信するPreparedStatement
     *
     * @return 結果
     */
    fun sendQuery(preparedStatement: PreparedStatement): ResultSet

    /**
     * 送信後の結果いらない場合に使用します。。例: INSERT, DELETE, UPDATE, UPSERT
     * @param sql SQLクエリ文。SQLインジェクションを対策するため、値は?に置換することをおすすめします。
     * @param params sqlで?に置換した値を入れることによってエスケープ処理後に?を置換します。
     */
    fun sendUpdate(sql: String, vararg params: Any)

    /**
     * 送信後の結果いらない場合に使用します。。例: INSERT, DELETE, UPDATE, UPSERT
     * @param preparedStatement 送信するPreparedStatement
     */
    fun sendUpdate(preparedStatement: PreparedStatement)
}