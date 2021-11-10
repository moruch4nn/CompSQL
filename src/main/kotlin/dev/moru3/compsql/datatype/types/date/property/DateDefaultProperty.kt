package dev.moru3.compsql.datatype.types.date.property

enum class DateDefaultProperty {
    CURRENT_TIMESTAMP;

    override fun toString(): String = super.toString().toUpperCase()
}