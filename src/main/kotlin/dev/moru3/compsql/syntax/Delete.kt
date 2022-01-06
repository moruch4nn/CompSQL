package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.NonCompleteSyntax
import dev.moru3.compsql.interfaces.NonForceUpdateSendable

/**
 * orderByなどの句は無視されます。
 */
interface Delete: NonCompleteSyntax, NonForceUpdateSendable {
    val where: FirstWhere

    /**
     * select文を使用する際に使用するwhere文を作成します。
     * 例: where("name").equal("moru")
     * @param key where文の最初のカラムの名前
     */
    fun where(key: String): KeyedWhere
}