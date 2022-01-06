package dev.moru3.compsql.annotation

/**
 * スマートクエリに使用する型のテーブル名を指定できます。
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class TableName(val name: String)
