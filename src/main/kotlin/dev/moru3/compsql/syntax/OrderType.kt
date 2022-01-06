package dev.moru3.compsql.syntax

/**
 * ORDER BYで使用する昇順や降順のenumです。
 */
enum class OrderType {
    /**
     * 昇順。
     * 例: 1,2,3,4,5
     */
    ASC,

    /**
     * 降順。
     * 例: 5,4,3,2,1
     */
    DESC
}