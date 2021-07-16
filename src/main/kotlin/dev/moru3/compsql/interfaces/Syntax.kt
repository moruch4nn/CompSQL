package dev.moru3.compsql.interfaces

interface Syntax {
    /**
     * SQL構文に変換します
     */
    fun build(force: Boolean): String
}