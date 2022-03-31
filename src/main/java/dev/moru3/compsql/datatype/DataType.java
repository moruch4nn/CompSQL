package dev.moru3.compsql.datatype;

import dev.moru3.compsql.TypeHub;
import dev.moru3.compsql.datatype.types.binary.BINARY;
import dev.moru3.compsql.datatype.types.binary.LONGBLOB;
import dev.moru3.compsql.datatype.types.binary.VARBINARY;
import dev.moru3.compsql.datatype.types.bool.BOOLEAN;
import dev.moru3.compsql.datatype.types.date.DATE;
import dev.moru3.compsql.datatype.types.date.DATETIME;
import dev.moru3.compsql.datatype.types.numeric.BIGINT;
import dev.moru3.compsql.datatype.types.numeric.INTEGER;
import dev.moru3.compsql.datatype.types.numeric.SMALLINT;
import dev.moru3.compsql.datatype.types.numeric.TINYINT;
import dev.moru3.compsql.datatype.types.numeric.unsigned.UBIGINT;
import dev.moru3.compsql.datatype.types.numeric.unsigned.UINTEGER;
import dev.moru3.compsql.datatype.types.numeric.unsigned.USMALLINT;
import dev.moru3.compsql.datatype.types.numeric.unsigned.UTINYINT;
import dev.moru3.compsql.datatype.types.text.CHAR;
import dev.moru3.compsql.datatype.types.text.LONGTEXT;
import dev.moru3.compsql.datatype.types.text.TEXT;
import dev.moru3.compsql.datatype.types.text.VARCHAR;

public class DataType {
    // binary系
    public static BINARY BINARY = TypeHub.INSTANCE.register(new BINARY(255));
    public static LONGBLOB LONGBLOB = TypeHub.INSTANCE.register(new LONGBLOB(1023));
    public static VARBINARY VARBINARY = TypeHub.INSTANCE.register(new VARBINARY(255));

    // boolean系
    public static BOOLEAN BOOLEAN = TypeHub.INSTANCE.register(new BOOLEAN());

    // numeric系
    public static BIGINT BIGINT = TypeHub.INSTANCE.register(new BIGINT((byte) 19));
    public static INTEGER INTEGER = TypeHub.INSTANCE.register(new INTEGER((byte) 10));
    public static SMALLINT SMALLINT = TypeHub.INSTANCE.register(new SMALLINT((byte) 5));
    public static TINYINT TINYINT = TypeHub.INSTANCE.register(new TINYINT((byte) 3));
    // unsigned
    public static UBIGINT UBIGINT = TypeHub.INSTANCE.register(new UBIGINT((byte) 20));
    public static UINTEGER UINTEGER = TypeHub.INSTANCE.register(new UINTEGER((byte) 10));
    public static USMALLINT USMALLINT = TypeHub.INSTANCE.register(new USMALLINT((byte) 5));
    public static UTINYINT UTINYINT = TypeHub.INSTANCE.register(new UTINYINT((byte) 3));

    // text系
    public static CHAR CHAR = TypeHub.INSTANCE.register(new CHAR(255));
    public static LONGTEXT LONGTEXT = TypeHub.INSTANCE.register(new LONGTEXT(1023));
    public static TEXT TEXT = TypeHub.INSTANCE.register(new TEXT(511));
    public static VARCHAR VARCHAR = TypeHub.INSTANCE.register(new VARCHAR(255));

    // date型
    public static DATE DATE = TypeHub.INSTANCE.register(new DATE(null));
    public static DATETIME DATETIME = TypeHub.INSTANCE.register(new DATETIME(null));
}
