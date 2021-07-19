package dev.moru3.compsql

interface Insert {

    /**
     * テーブル名
     */
    val name: String

    fun add(type: DataType<*>, key: String, value: Any): Insert

    fun add(key: String, value: Any): Insert

    fun build(force: Boolean): String

    /**
     * 変更を送信します。
     */
    fun send(force: Boolean)
}