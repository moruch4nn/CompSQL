# CompSQL
###With CompSQL, you can operate MySQL, MariaDB, SQLite, etc. easily and safely like Kotlin and Java.<br>
_※Currently, only MySQL and MariaDB are supported. Not all SQL syntax is supported._<br>
## Features<br>
+ Safe and intuitive SQL query operations.<br>
+ Clever queries that take advantage of object orientation.([SmartQuery](https://github.com/moru348/CompSQL#SmartQuery "SmartQuery"))<br>

## SmartQuery
SmartQuery is a Beautiful and Useful CompSQL feature that takes full advantage of object orientation.<br>
※Constructor is not called.<br>
**This exmaple is use SmartQuery. [Here](https://github.com/moru348/CompSQL#SmartQuery "Example") code is not use SmartQuery**
### For Kotlin<br>
```kotlin
@TableName("cars")
class Car(
    // AutoIncrementが使用されている場合、putOrUpdate、get以外のスマートクエリではこのフィールドは無視されます。
    @Column("id", isPrimaryKey = true, isAutoIncrement = true)
    val id: Int,
    // Columnアノテーションをつけると変数の型からSQLの型が自動推測されます。自動推測を使用しない場合はテーブル作成のみDatabase.table(table_name)を使用してください。
    @Column("name", isNotNull = true)
    val name: String,
    // デフォルト値を設定する場合はコンストラクタにデフォルト値を設定すると格納する際に反映されますが、スマートクエリを使用しない場合は反映されないため、INSERTを使用する場合はテーブル作成のみDatabase.table(table_name)を使用してください。
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
    // send(),データの取得。取得したデータはCar::class.javaのインスタンスに変換されてリストに格納されます。一致するデータがない場合はからのリストを返します。
    }.send()

    // debug ([0]ではなくgetOrNullを使うべきですが、サンプルコードの複雑化を避けるため使用していません。)
    println("NAME=${result[0].name},ID=${result[0].id}")
}
```
<br>
<br>
<br>
<br>
<br>
<br>


## Simple example
**Thise xample is not use [SmartQuery](https://github.com/moru348/CompSQL#SmartQuery "SmartQuery")**<br>
### For Kotlin<br>
```kotlin
fun main() {
    // connection変数を作成して高階関数の外からでもクエリを実行。
    val database = MariaDBConnection("loaclhost:3306", "cars", "moru", "password") {
        // (CREATE TABLE)テーブルの作成
        table("cars") {
            // AUTO_INCREMENTとPRIMARY_KEYを有効にしてIDフィールドを作成
            column("id", DataType.INTEGER).setPrimaryKey(true).setAutoIncrement(true)
            // 名前のフィールドを作成(最大255文字)
            column("name", DataType.VARCHAR).setNotNull(true)
            // DATE(PROPERTY?)のプロパティに設定してデータ挿入時の日付を自動挿入。
            column("datetime", DATE(DateDefaultProperty.CURRENT_TIMESTAMP)).setNotNull(true)
            // VARCHAR(PROPERTY?)のプロパティを設定して最大サイズを3480に設定、デフォルトの値を"moru"に。
            column("long_nameeeeee", VARCHAR(3480)).setNotNull(false).setDefaultValue("moru")
            // send(force)でテーブルを作成。forceをfalseにすることで存在する場合はスルー。
        }.send(false)
    }

    // (INSERT)テーブルにデータを挿入する
    database.insert("cars") {
        // nameフィールドを "もる" に
        add("name", "もる")
        // long_nameeeeフィールドを "もるもるもるもるもるもるもる" に
        add("long_nameeee", "もるもるもるもるもるもるもる")
        // send(force)でデータを挿入。forceをfalseにすることでPrimaryKeyが重複する場合はスルー
    }.send(false)

    // (UPSERT)テーブルにデータを挿入、すでに存在する場合はデータを更新する
    database.upsert("cars") {
        // idが1
        add("id", 1)
        // nameフィールドを "もる" に
        add("name", "もる")
        // long_nameeeeフィールドを "もるもるもるもるもるもるもる" に
        add("long_nameeee", "もるもるもるもるもるもるもる")
        // send(force)でデータを挿入。PrimaryKeyが重複している場合はデータをクエリー文の情報にアップデートします。
    }.send(false)

    // (SELECT)selectでデータを取得
    val result: ResultSet = database.select("cars") {
        // nameフィールドが"もる"かつ、long_nameeeeフィールドに"もるる"が含まれるかつ、idが3以上のフィールドを取得
        where("name").equal("もる").and("long_nameeee").like("%もるる%").and("id").greaterOrEquals(3)
        // send()でResultSetを取得。
    }.send()
}
```
### For Java<br>
```java
class Main {
    public static void main(String[] args) {
        // connection変数を作成する。
        Database database = new MariaDBConnection("localhost:3306", "cars", "moru", "password", null);

        // (CREATE TABLE)テーブルの作成
        Table table = database.table("cars");
        // AUTO_INCREMENTとPRIMARY_KEYを有効にしてIDフィールドを作成
        table.column("id", DataType.INTEGER).setPrimaryKey(true).setAutoIncrement(true);
        // 名前のフィールドを作成(最大255文字)
        table.column("name", DataType.VARCHAR).setNotNull(true);
        // DATE(PROPERTY?)のプロパティに設定してデータ挿入時の日付を自動挿入。
        table.column("datetime", new DATE(DateDefaultProperty.CURRENT_TIMESTAMP)).setNotNull(true);
        // VARCHAR(PROPERTY?)のプロパティを設定して最大サイズを3480に設定、デフォルトの値を"moru"に。
        table.column("long_nameeeeee", new VARCHAR(3480)).setNotNull(false).setDefaultValue("moru");
        // send(force)でテーブルを作成。forceをfalseにすることで存在する場合はスルー。
        table.send(false);

        // (INSERT)テーブルにデータを挿入する Connection.insert(table_name)
        Insert insert = database.insert("cars");
        // nameフィールドを "もる" に
        insert.add("name", "もる");
        // long_nameeeeフィールドを "もるもるもるもるもるもるもる" に
        insert.add("long_nameeee", "もるもるもるもるもるもるもる");
        // send(force)でデータを挿入。forceをfalseにすることでPrimaryKeyが重複する場合はスルー
        insert.send(false);

        // (UPSERT)テーブルにデータを挿入、すでに存在する場合はデータを更新する
        Upsert upsert = database.upsert("cars");
        // idが1
        upsert.add("id", 1);
        // nameフィールドを "もる" に
        upsert.add("name", "もる");
        // long_nameeeeフィールドを "もるもるもるもるもるもるもる" に
        upsert.add("long_nameeee", "もるもるもるもるもるもるもる");
        // send(force)でデータを挿入。PrimaryKeyが重複している場合はデータをクエリー文の情報にアップデートします。
        upsert.send(false);

        // (SELECT)selectでデータを取得
        Select select = database.select("cars");
        // nameフィールドが"もる"かつ、long_nameeeeフィールドに"もるる"が含まれるかつ、idが3以上のフィールドを取得
        select.where("name").equal("もる").and("long_nameeee").like("%もるる%").and("id").greaterOrEquals(3);
        // send()でResultSetを取得。
        ResultSet result = select.send();
    }
}
```