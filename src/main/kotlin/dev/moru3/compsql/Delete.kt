package dev.moru3.compsql

import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.NonForceSyntax
import dev.moru3.compsql.interfaces.SendSyntax
import dev.moru3.compsql.interfaces.Syntax
import java.sql.ResultSet

/**
 * orderByなどの句は無視されます。
 */
interface Delete: NonCompleteSyntax, NonForceSyntax {
    val where: Where

    fun where(key: String): KeyedWhere
}