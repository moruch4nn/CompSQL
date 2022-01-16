![GitHub commit activity](https://img.shields.io/github/commit-activity/m/moru348/CompSQL)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/moru348/CompSQL/Publish%20package/main)
![GitHub](https://img.shields.io/github/license/moru348/CompSQL)
![GitHub Repo stars](https://img.shields.io/github/stars/moru348/CompSQL?style=social)
# CompSQL - CURRENTLY ONLY NIGHTLY BUILDS
### With CompSQL, you can operate MySQL, MariaDB, SQLite, etc. easily and safely like Kotlin and Java.<br>
_※Currently, only MySQL and MariaDB are supported. Not all SQL syntax is supported._<br>
## Features<br>
+ Safe and intuitive SQL query operations.<br>
+ Clever queries that take advantage of object orientation.([SmartQuery](#SmartQuery "SmartQuery"))<br>

## SmartQuery
SmartQuery is a Beautiful and Useful CompSQL feature that takes full advantage of object orientation.<br>
※Constructor is not called.<br>
**This exmaple is use SmartQuery. [Here](#simple-example "Example") code is not use SmartQuery**<br>
### For Kotlin<br>
```kotlin
@TableName("cars")
class Car(
    @Column("id", isPrimaryKey = true, isAutoIncrement = true)
    val id: Int,
    @Column("name", isNotNull = true)
    val name: String,
    @Column("long_nameeeee", isNotNull = true)
    val longNameeeeee: String = "default value.",
    @Column("datetime")
    val date: Date = Date(java.util.Date().time),
)

fun main() {
    //Create connection and store
    val database = MariaDBConnection("loaclhost:3306", "cars", "moru", "password") {
        add(Car::class.java).send(false) //CREATE TABLE
    }
   
    val morucar = Car(1,"F1","Very cool and fast car.")
    
    // Note: Variables with AUTO_INCREMENT attribute during INSERT will be ignored. (In this case, id isAutoIncrement = true)
    database.put(morucar).send(false) //INSERT

    database.putOrUpdate(morucar).send() //UPSERT

    val result: List<Car> =database.get(Car::class.java) {
        // WHERE name = 'F1' AND long_nameeee LIKE "%fast%" AND id >= 3
        where("name").equal("F1").and("long_nameeee").like("%fast%").and("id").greaterOrEquals(3)
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
### For Kotlin<br>
```kotlin
fun main() {
    //Create connection and store
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
        add("name", "F1")
        add("long_nameeee", "Very cool and fast car.")
    }.send(false)

    //UPSERT
    database.upsert("cars") {
        add("id", 1)
        add("name", "F2")
        add("long_nameeee", "Very cool and fast fast fast fast car.")
    }.send(false)

    //SELECT
    val result: ResultSet = database.select("cars") {
        // WHERE name = 'F1' AND long_nameeee LIKE "%fast%" AND id >= 3
        where("name").equal("F1").and("long_nameeee").like("%fast fast fast%").and("id").greaterOrEquals(3)
    }.send()
}
```
### For Java<br>
```java
class Main {
    public static void main(String[] args) {
        // Create connection
        Database database = new MariaDBConnection("localhost:3306", "cars", "moru", "password", null);

        Table table = database.table("cars");//CREATE TABLE __START__
        table.column("id", DataType.INTEGER).setPrimaryKey(true).setAutoIncrement(true);
        table.column("name", DataType.VARCHAR).setNotNull(true);
        table.column("datetime", new DATE(DateDefaultProperty.CURRENT_TIMESTAMP)).setNotNull(true);
        table.column("long_nameeeeee", new VARCHAR(3480)).setNotNull(false).setDefaultValue("moru");
        table.send(false); //CREATE TABLE __END__

        Insert insert = database.insert("cars");//INSERT __START__
        insert.add("name", "F1");
        insert.add("long_nameeee", "Very cool and fast car.");
        insert.send(false);//INSERT __END__

        Upsert upsert = database.upsert("cars");//UPSERT __START__
        upsert.add("id", 1);
        upsert.add("name", "F2");
        upsert.add("long_nameeee", "Very cool and fast fast fast fast car.");
        upsert.send(false);//UPESRT __END__

        Select select = database.select("cars"); //SELECT __START__
        // WHERE name = 'F1' AND long_nameeee LIKE "%fast%" AND id >= 3
        select.where("name").equal("F1").and("long_nameeee").like("%fast fast fast%").and("id").greaterOrEquals(3);
        ResultSet result = select.send(); //SELECT __END__
    }
}
```
