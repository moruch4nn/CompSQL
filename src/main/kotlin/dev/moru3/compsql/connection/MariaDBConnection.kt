package dev.moru3.compsql.connection

import java.sql.DriverManager
import java.util.*

/**
 * 新しくMariaDBのコネクションを開きます。すでに開いているコネクションがある場合はそのコネクションをcloseします。
 * @param url JDBCのURL。 例:jdbc:mysql://host/database
 * @param properties 接続する際に使用するプロパティ。
 * @param action Kotlin向けの高階関数。使用しない場合はnullを指定してください。
 */
class MariaDBConnection(url: String, properties: Properties, action: MySQLConnection.()->Unit = {}): MySQLConnection(url, properties, action) {
    /**
     * MariaDBのコネクションを作成する。
     * このコネクションを作成することにより、データベースに対してSQL文を実行できるようになります。
     * パスワード認証場合、プロパティにユーザー名、パスワードを含める必要があります。
     *
     * @param host データベースのホスト。ポート情報も含める必要があります。 例: 127.0.0.1:3306
     * @param database 使用するデータベースの名前。存在しない場合は自動で作成します。
     * @param properties 接続する際に使用するプロパティ。使用しない場合はnull(kotlinの場合は何もなし)を指定してください。
     * @param action Kotlin向けの高階関数。使用しない場合はnullを指定してください。
     */
    constructor(host: String, database: String, properties: Properties? = null, action: MySQLConnection.()->Unit = {}): this("jdbc:mariadb://${host}/${database}", properties?: Properties(), action)

    /**
     * MariaDBのコネクションを作成する。
     * このコネクションを作成することにより、データベースに対してSQL文を実行できるようになります。
     *
     * @param host データベースのホスト。ポート情報も含める必要があります。 例: 127.0.0.1:3306
     * @param database 使用するデータベースの名前。存在しない場合は自動で作成します。
     * @param username 接続するユーザーのユーザー名。
     * @param password 接続するユーザーのパスワード。
     * @param properties 接続する際に使用するプロパティ。使用しない場合はnull(kotlinの場合は何もなし)を指定してください。
     * @param action Kotlin向けの高階関数。使用しない場合はnullを指定してください。
     */
    constructor(host: String, database: String, username: String, password: String, properties: Properties? = null, action: MySQLConnection.()->Unit = {}): this(host, database, (properties?:Properties()).also { it["user"] = username;it["password"] = password }, action)

    /**
     * この関数はconnectionが初期化される前に呼び出され、主にドライバーの初期化、データベースの作成、databaseの初期化に使用します。
     */
    override fun init(url: String, properties: Properties) {
        database = url.split("/").last()
        try { Class.forName("org.mariadb.jdbc.Driver") } catch (_: Exception) { }
        val bacon = DriverManager.getConnection(url, properties)
        bacon.prepareStatement("CREATE DATABASE IF NOT EXISTS $database").also { it.executeUpdate() }.close()
        bacon.close()
    }
}