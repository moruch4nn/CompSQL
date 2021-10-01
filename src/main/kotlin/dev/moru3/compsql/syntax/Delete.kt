package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.ForceUpdateSendable
import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.NonForceUpdateSendable
import dev.moru3.compsql.interfaces.QuerySendable

/**
 * orderByなどの句は無視されます。
 */
interface Delete: NonCompleteSyntax, NonForceUpdateSendable {
    val where: Where

    fun where(key: String): KeyedWhere
}