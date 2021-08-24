package dev.moru3.compsql.datatype.types.date

import dev.moru3.compsql.DataHub
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.date.property.DateDefaultProperty
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class Date(property: DateDefaultProperty?): DataType<Date, Date> {

    override val typeName: String = "DATE"
    override val from: Class<Date> = Date::class.javaObjectType
    override val type: Class<Date> = Date::class.javaObjectType
    override val sqlType: Int = Types.DATE
    override val allowPrimaryKey: Boolean = false
    override val allowNotNull: Boolean = true
    override val allowUnique: Boolean = false
    override val isUnsigned: Boolean = false
    override val allowZeroFill: Boolean = false
    override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String? = property?.toString()
    override val priority: Int = 10
    override val action: (PreparedStatement, Int, Date) -> Unit = { ps, i, a -> ps.setDate(i, a) }
    override val convert: (value: Date) -> Date = { it }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Date) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): Date? = resultSet.getDate(id)

    init {
        DataHub.addCustomType(this)
    }
}