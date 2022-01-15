package dev.moru3.compsql.syntax

import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.interfaces.NonForceUpdateSendable
import dev.moru3.compsql.syntax.table.Table

/**
 * Upsertを使用するとInsertを行う際にすでに存在するPrimaryKeyとデータが競合する場合、
 * Insertの変わりにUpdateを行いデータの競合をなくします。
 *
 * ユーザー情報の更新などに使用するとすでにメールアドレス(やID)が存在する場合には情報を更新し、
 * 存在しない場合には情報を追加という処理を簡潔に書けます。
 */
interface Upsert: NonForceUpdateSendable {
    /**
     * テーブル名
     */
    val table: Table

    /**
     * Upsertにカラムを追加します。
     * @param type カラムの型(DataType)
     * @param key カラムの名前
     * @param value カラムの値
     */
    fun add(type: BaseDataType<*,*>, key: String, value: Any): Upsert

    /**
     * Upsertにカラムを追加します。
     * @param key カラムの名前
     * @param value カラムの値
     */
    fun add(key: String, value: Any): Upsert
}