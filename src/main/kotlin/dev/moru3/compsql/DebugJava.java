package dev.moru3.compsql;

import dev.moru3.compsql.connection.MariaDBConnection;
import dev.moru3.compsql.datatype.BaseDataType;
import dev.moru3.compsql.datatype.DataType;
import dev.moru3.compsql.datatype.types.date.DATE;
import dev.moru3.compsql.datatype.types.date.property.DateDefaultProperty;
import dev.moru3.compsql.datatype.types.text.VARCHAR;
import dev.moru3.compsql.syntax.Insert;
import dev.moru3.compsql.syntax.Select;
import dev.moru3.compsql.syntax.table.Table;

import java.sql.ResultSet;

public class DebugJava {
    public static void main(String[] args) {
        // connection変数を作成する。
        Database database = new MariaDBConnection("localhost:3306", "cars", "moru", "password", null);

        // テーブルの作成
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

        // テーブルにデータを挿入する Connection.insert(table_name)
        Insert insert = database.insert("cars");
        // nameフィールドを "もる" に
        insert.add("name", "もる");
        // long_nameeeeフィールドを "もるもるもるもるもるもるもる" に
        insert.add("long_nameeee", "もるもるもるもるもるもるもる");
        // send(force)でデータを挿入。forceをfalseにすることでPrimaryKeyが重複する場合はスルー
        insert.send(false);

        // selectでデータを取得
        Select select = database.select("cars");
        // nameフィールドが"もる"かつ、long_nameeeeフィールドに"もるる"が含まれるかつ、idが3以上のフィールドを取得
        select.where("name").equal("もる").and("long_nameeee").like("%もるる%").and("id").greaterOrEquals(3);
        // send()でResultSetを取得。
        ResultSet result = select.send();
    }
}
