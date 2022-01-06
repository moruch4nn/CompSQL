package dev.moru3.compsql.syntax

import dev.moru3.compsql.interfaces.QuerySendable

// TODO.
interface Select: QuerySendable {
    /**
     * where文。別で作成したwhere文に置き換える必要がある場合はsetWhereを行います。
     */
    var where: FirstWhere

    /**
     * select文を使用する際に使用するwhere文を作成します。
     * 例: where("name").equal("moru")
     * @param key where文の最初のカラムの名前
     */
    fun where(key: String): KeyedWhere

    /**
     * selectの結果を昇順、もしくは降順に切り替えます。
     * @param column カラム名
     * @param orderType オーダータイプ
     */
    fun orderBy(column: String, orderType: OrderType): Select

    /**
     * selectの最大取得数。
     */
    fun limit(limit: Int): FirstWhere
}