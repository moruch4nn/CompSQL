# CompSQL
### With CompSQL, you can operate MySQL, MariaDB, SQLite, etc. easily and safely like Kotlin and Java.<br>
_※Currently, only MySQL and MariaDB are supported. Not all SQL syntax is supported._<br>
## Features<br>
+ Safe and intuitive SQL query operations.<br>
+ Clever queries that take advantage of object orientation.([SmartQuery](#SmartQuery "SmartQuery"))<br>

## SmartQuery
SmartQuery is a Beautiful and Useful CompSQL feature that takes full advantage of object orientation.<br>
※Constructor is not called.<br>
**This exmaple is use SmartQuery. [Here](#simple-example "Example") code is not use SmartQuery**<br>
[Click here for the version of the code with detailed notes commented.](README_COMMENT.md)
### For Kotlin<br>
```kotlin
@TableName("cars")
class Car(
    @Column("id", isPrimaryKey = true, isAutoIncrement = true)
    val id: Int,
    @Column("name", isNotNull = true)
    val name: String,
    @Column("long_nameeeee", isNotNull = true)
    val longNameeeeee: String = "moru",
    @Column("datetime")
    val date: Date = Date(java.util.Date().time),
)

fun main() {
    //コネクションの作成、格納
    val database = MariaDBConnection("loaclhost:3306", "cars", "moru", "password") {
        add(Car::class.java).send(false) //CREATE TABLE
    }
   
    val morucar = Car(1,"もるかー","もるもるもるもる")
    
    // 注意事項:INSERT時のAUTO_INCREMENT属性が付与された変数は無視されます。(今回の場合idがisAutoIncrement = true)
    database.put(morucar).send(false) //INSERT

    database.putOrUpdate(morucar).send() //UPSERT

    val result: List<Car> =database.get(Car::class.java) {
        where("name").equal("もる").and("long_nameeee").like("%もるる%").and("id").greaterOrEquals(3)
    }.send() //SELCT

    println("NAME=${result[0].name},ID=${result[0].id}")
}
```
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>
<br>

## Simple example
**Thise xample is not use [SmartQuery](#SmartQuery "SmartQuery")**<br><br>
[Click here for the version of the code with detailed notes commented.](README_COMMENT.md)
### For Kotlin<br>
```kotlin
fun main() {
    //コネクションの作成、格納。
    val database = MariaDBConnection("loaclhost:3306", "cars", "moru", "password") {
        //CREATE TABLE
        table("cars") {
            column("id", DataType.INTEGER).setPrimaryKey(true).setAutoIncrement(true)
            column("name", DataType.VARCHAR).setNotNull(true)
            column("datetime", DATE(DateDefaultProperty.CURRENT_TIMESTAMP)).setNotNull(true)
            column("long_nameeeeee", VARCHAR(3480)).setNotNull(false).setDefaultValue("moru")
        }.send(false)
    }

    //INSERT
    database.insert("cars") {
        add("name", "もる")ttps://github.com/moru348/CompSQL/R
        add("long_nameeee", "もるもるもるもるもるもるもる")
    }.send(false)

    //UPSERT
    database.upsert("cars") {
        add("id", 1)
        add("name", "もる")
        add("long_nameeee", "もるもるもるもるもるもるもる")
    }.send(false)

    //SELECT
    val result: ResultSet = database.select("cars") {
        where("name").equal("もる").and("long_nameeee").like("%もるる%").and("id").greaterOrEquals(3)
    }.send()
}
```
### For Java<br>
[Click here for the version of the code with detailed notes commented.](README_COMMENT.md)
```java
class Main {
    public static void main(String[] args) {
        // コネクションの作成
        Database database = new MariaDBConnection("localhost:3306", "cars", "moru", "password", null);

        Table table = database.table("cars");//CREATE TABLE __START__
        table.column("id", DataType.INTEGER).setPrimaryKey(true).setAutoIncrement(true);
        table.column("name", DataType.VARCHAR).setNotNull(true);
        table.column("datetime", new DATE(DateDefaultProperty.CURRENT_TIMESTAMP)).setNotNull(true);
        table.column("long_nameeeeee", new VARCHAR(3480)).setNotNull(false).setDefaultValue("moru");
        table.send(false); //CREATE TABLE __END__

        Insert insert = database.insert("cars");//INSERT __START__
        insert.add("name", "もる");
        insert.add("long_nameeee", "もるもるもるもるもるもるもる");
        insert.send(false);//INSERT __END__

        Upsert upsert = database.upsert("cars");//UPSERT __START__
        upsert.add("id", 1);
        upsert.add("name", "もる");
        upsert.add("long_nameeee", "もるもるもるもるもるもるもる");
        upsert.send(false);//UPESRT __END__

        Select select = database.select("cars"); //SELECT __START__
        select.where("name").equal("もる").and("long_nameeee").like("%もるる%").and("id").greaterOrEquals(3);
        ResultSet result = select.send(); //SELECT __END__
    }
}
```
