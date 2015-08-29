package com.coderpage.wubinput.define;

/**
 * @author abner-l
 * @since 2015-08-29
 */
public class SQLDefine {


    public static class TableDictionary {

        public final static String TABLE_NAME = "dictionary";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE_COUNT = "value_count";
        public final static String COLUMN_VALUE_1 = "value1";
        public final static String COLUMN_VALUE_2 = "value2";
        public final static String COLUMN_VALUE_3 = "value3";
        public final static String COLUMN_VALUE_4 = "value4";
        public final static String COLUMN_VALUE_5 = "value5";
        public final static String COLUMN_VALUE_6 = "value6";
        public final static String COLUMN_VALUE_7 = "value7";
        public final static String COLUMN_VALUE_8 = "value8";
        public final static String COLUMN_VALUE_9 = "value9";
        public final static String COLUMN_VALUE_10 = "value10";
        public final static String COLUMN_VALUE_11 = "value11";
        public final static String COLUMN_VALUE_12 = "value12";
        public final static String COLUMN_VALUE_13 = "value13";
        public final static String COLUMN_VALUE_14 = "value14";
        public final static String COLUMN_VALUE_15 = "value15";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_COUNT_INDEX = 2;
        public final static int COLUMN_VALUE_BASE_INDEX = 3;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE_COUNT + " INTEGER DEFAULT 0," + COLUMN_VALUE_1 + " TEXT,"
                + COLUMN_VALUE_2 + " TEXT," + COLUMN_VALUE_3 + " TEXT," + COLUMN_VALUE_4 + " TEXT,"
                + COLUMN_VALUE_5 + " TEXT," + COLUMN_VALUE_6 + " TEXT," + COLUMN_VALUE_7 + " TEXT,"
                + COLUMN_VALUE_8 + " TEXT," + COLUMN_VALUE_9 + " TEXT," + COLUMN_VALUE_10 + " TEXT,"
                + COLUMN_VALUE_11 + " TEXT," + COLUMN_VALUE_12 + " TEXT," + COLUMN_VALUE_13 + " TEXT,"
                + COLUMN_VALUE_14 + " TEXT," + COLUMN_VALUE_15 + " TEXT);";
    }

    public static class TableWuBi86 {
        public final static String TABLE_NAME = "wb_86";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }

    public static class TableWuBi98 {
        public final static String TABLE_NAME = "wb_98";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }

    public static class TableSingleCharLevel1 {
        public final static String TABLE_NAME = "single_l1";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }

    public static class TableSingleCharLevel2 {
        public final static String TABLE_NAME = "single_l2";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }

    public static class TableSingleCharLevel3 {
        public final static String TABLE_NAME = "single_l3";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }

    public static class TableSingleChar {
        public final static String TABLE_NAME = "single";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }

    public static class TableWord {
        public final static String TABLE_NAME = "word";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }

    public static class TableArticle {
        public final static String TABLE_NAME = "article";
        public final static String COLUMN_ID = "id";
        public final static String COLUMN_KEY = "key";
        public final static String COLUMN_VALUE = "value";

        public final static int COLUMN_KEY_INDEX = 1;
        public final static int COLUMN_VALUE_INDEX = 2;

        public final static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_KEY + " TEXT,"
                + COLUMN_VALUE + " TEXT);";
    }
}
