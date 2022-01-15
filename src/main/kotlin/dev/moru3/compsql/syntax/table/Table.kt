package dev.moru3.compsql.syntax.table

import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.interfaces.ForceUpdateSendable
import dev.moru3.compsql.syntax.table.column.Column
import java.sql.PreparedStatement

interface Table: ForceUpdateSendable {
    /**
     * テーブル名。setするとRENAMEされます。
     */
    val name: String

    /**
     * 新しくColumnを作成し、追加します。
     */
    fun column(name: String, type: BaseDataType<*,*>, action: (Column)->Unit = {}): Column

    /**
     * 新しくColumnを作成し、追加します。
     */
    fun column(name: String, type: BaseDataType<*,*>): Column

    /**
     * テーブルのAfter関連の関数が入ってます。
     */
    val after: AfterTable

    /**
     * SQL構文をビルドします。
     * @param force 強制的に実行すかどうか。trueにした場合はエラーが発生する可能性があります。
     * @return PrepareStatement
     */
    fun build(force: Boolean): PreparedStatement

    fun buildAsRaw(force: Boolean): Pair<String, List<Pair<Any?, BaseDataType<*,*>>>>
}