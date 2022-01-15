import dev.moru3.compsql.annotation.Column
import dev.moru3.compsql.annotation.IgnoreColumn
import dev.moru3.compsql.annotation.TableName
import dev.moru3.compsql.connection.MariaDBConnection
import dev.moru3.compsql.datatype.BaseDataType
import dev.moru3.compsql.datatype.types.date.DATE
import dev.moru3.compsql.datatype.types.date.property.DateDefaultProperty
import dev.moru3.compsql.datatype.types.text.VARCHAR
import java.sql.Date
import java.sql.ResultSet

@TableName("cars")
class Car(
    // AutoIncrementが使用されている場合、putOrUpdate、get以外のスマートクエリではこのフィールドは無視されます。
    @Column("id", isPrimaryKey = true, isAutoIncrement = true)
    val id: Int,
    // Columnアノテーションをつけると変数の型からSQLの型が自動推測されます。自動推測を使用しない場合はテーブル作成のみDatabase.table(table_name)を使用してください。
    @Column("name", isNotNull = true)
    val name: String,
    // デフォルト値を設定する場合はコンストラクタにデフォルト値を設定すると格納する際に反映されますが、INSERT文を使用すると反映されないため、INSERTを使用する場合はテーブル作成のみDatabase.table(table_name)を使用してください。
    @Column("long_nameeeee", isNotNull = true)
    val longNameeeeee: String = "moru",
    // 上に同じく。
    @Column("datetime")
    val date: Date = Date(java.util.Date().time),
)

fun main() {
    val database = MariaDBConnection("loaclhost:3306", "cars", "moru", "password") {
        // (CREATE TABLE)テーブルの作成 Car::classで定義されたフィールドやアノテーションから自動的にテーブルを作成します。
        add(Car::class.java).send(false)
    }

    // 格納したいデータのインスタンスを作成します。
    val morucar = Car(-1,"もるかー","もるもるもるもる")
    // (INSERT)作成したインスタンスをデータベースに格納します。
    database.put(morucar).send(false)

    // (UPSERT)作成したインスタンスをデータベースに格納します。PrimaryKeyが重複する場合はデータをアップデートします。
    database.putOrUpdate(morucar).send()

    val result: List<Car> =database.get(Car::class.java) {
        // nameフィールドが"もる"かつ、long_nameeeeフィールドに"もるる"が含まれるかつ、idが3以上のフィールドを取得
        where("name").equal("もる").and("long_nameeee").like("%もるる%").and("id").greaterOrEquals(3)
    // データの取得。取得したデータはCar::class.javaのインスタンスに変換されてリストに格納されます。一致するデータがない場合はからのリストを返します。
    }.send()

    // debug ([0]ではなくgetOrNullを使うべきですが、サンプルコードの複雑化を避けるため使用していません。)
    println("NAME=${result[0].name},ID=${result[0].id}")
}