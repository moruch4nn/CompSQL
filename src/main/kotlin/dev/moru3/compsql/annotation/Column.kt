package dev.moru3.compsql.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.CLASS)
annotation class Column(val name: String, val isZeroFill: Boolean = false, val isPrimaryKey: Boolean = false, val isNotNull: Boolean = false, val isAutoIncrement: Boolean = false, val isUniqueIndex: Boolean = false)