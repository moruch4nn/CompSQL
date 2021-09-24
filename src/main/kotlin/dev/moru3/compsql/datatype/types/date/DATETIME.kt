package dev.moru3.compsql.datatype.types.date

import dev.moru3.compsql.DataHub
import dev.moru3.compsql.datatype.DataType
import dev.moru3.compsql.datatype.types.date.property.DateDefaultProperty
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

class DATETIME(property: DateDefaultProperty?): DataType<Date> {

    final override val typeName: String = "DATETIME"
    override val from: Class<*> = Date::class.javaObjectType
    final override val type: Class<Date> = Date::class.javaObjectType
    final override val sqlType: Int = Types.DATE
    final override val allowPrimaryKey: Boolean = false
    final override val allowNotNull: Boolean = true
    final override val allowUnique: Boolean = false
    final override val isUnsigned: Boolean = false
    final override val allowZeroFill: Boolean = false
    final override val allowAutoIncrement: Boolean = false
    override val allowDefault: Boolean = true
    override val defaultProperty: String? = property?.toString()
    override val priority: Int = 10
    final override val action: (PreparedStatement, Int, Date) -> Unit = { ps, i, a -> ps.setDate(i, a) }

    override fun set(ps: PreparedStatement, index: Int, any: Any?) {
        check(any is Date) { "The type of \"any\" is different from \"type\"." }
        action.invoke(ps, index, any)
    }

    override fun get(resultSet: ResultSet, id: String): Any? = resultSet.getDate(id)

    init {
        DataHub.addCustomType(this)
    }
}