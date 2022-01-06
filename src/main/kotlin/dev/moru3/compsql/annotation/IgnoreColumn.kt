package dev.moru3.compsql.annotation

/**
 * スマートクエリに含まない変数を定義する際に使用します。
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class IgnoreColumn(val ignoreGet: Boolean = true)
