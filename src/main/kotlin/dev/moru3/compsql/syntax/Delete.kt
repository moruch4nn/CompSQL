package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.NonForceSyntax

/**
 * orderByなどの句は無視されます。
 */
interface Delete: NonCompleteSyntax, NonForceSyntax {
    val where: Where

    fun where(key: String): KeyedWhere
}