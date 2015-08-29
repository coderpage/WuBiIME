package com.coderpage.wubinput.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coderpage.wubinput.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.coderpage.wubinput.define.SQLDefine.TableDictionary;
import static com.coderpage.wubinput.define.SQLDefine.TableSingleCharLevel1;


/**
 * @author abner-l
 * @since 2015-08-29
 */
public class DictionaryDBHelper {
    private final boolean debug = true;

    private static DictionaryDBHelper instance = null;
    private SQLiteDatabase db;
    private Context context;

    public static synchronized DictionaryDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DictionaryDBHelper(context);
        }
        return instance;
    }

    private DictionaryDBHelper(Context context) {
        this.context = context;
        if (db != null) {
            this.closeDb();
        }
        db = openDb();
    }

    private SQLiteDatabase openDb() {
        String dbPath = "/data/data/com.coderpage.wubinput/databases/dictionary.db";

        File dbFile = new File(dbPath);

        if (!dbFile.exists()) {
            try {
                int BUFFER_SIZE = 200000;
                InputStream is = context.getResources().openRawResource(R.raw.dictionary);
                File localFile = new File("/data/data/com.coderpage.wubinput/databases/");
                if (!localFile.exists()) {
                    localFile.mkdir();
                }
                FileOutputStream fos = new FileOutputStream(dbPath);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();//关闭输出流
                is.close();//关闭输入流

                Runtime.getRuntime().exec("chmod 777 " + dbPath);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        return db;
    }

    public void closeDb() {
        db.close();
    }

//    boolean isDBEmpty() {
//        Cursor cursor = db.query(TableDictionary.TABLE_NAME, null, null, null, null, null, null, null);
//        return cursor.getCount() == 0;
//    }

//    void initDbData() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, ArrayList<String>> dict = new HashMap<>(262144);
//                BufferedReader bufferedResourceReader = null;
//
//                try {
//                    InputStream resourceFileInputStream = context.getResources().openRawResource(R.raw.wubi);
//                    InputStreamReader resourceReader = new InputStreamReader(resourceFileInputStream, "utf-8");
//                    bufferedResourceReader = new BufferedReader(resourceReader, 8192);
//
//                    String line;
//                    while ((line = bufferedResourceReader.readLine()) != null) {
//                        String[] words = line.split(" ");
//                        if (dict.containsKey(words[0])) {
//                            for (int i = 1; i < words.length; i++)
//                                dict.get(words[0]).add(words[i]);
//                        } else {
//                            ArrayList<String> list = new ArrayList<>();
//                            for (int i = 1; i < words.length; i++)
//                                list.add(words[i]);
//                            dict.put(words[0], list);
//                        }
//                    }
//
//                    String[] columnValueNames = {
//                            TableDictionary.COLUMN_VALUE_1
//                            , TableDictionary.COLUMN_VALUE_2
//                            , TableDictionary.COLUMN_VALUE_3
//                            , TableDictionary.COLUMN_VALUE_4
//                            , TableDictionary.COLUMN_VALUE_5
//                            , TableDictionary.COLUMN_VALUE_6
//                            , TableDictionary.COLUMN_VALUE_7
//                            , TableDictionary.COLUMN_VALUE_8
//                            , TableDictionary.COLUMN_VALUE_9
//                            , TableDictionary.COLUMN_VALUE_10
//                            , TableDictionary.COLUMN_VALUE_11
//                            , TableDictionary.COLUMN_VALUE_12
//                            , TableDictionary.COLUMN_VALUE_13
//                            , TableDictionary.COLUMN_VALUE_14
//                            , TableDictionary.COLUMN_VALUE_15
//                    };
//
//                    db.beginTransaction();
//                    ContentValues values;
//                    for (Map.Entry<String, ArrayList<String>> entry : dict.entrySet()) {
//                        values = new ContentValues();
//                        String key = entry.getKey();
//                        ArrayList<String> value = entry.getValue();
//                        values.put(TableDictionary.COLUMN_KEY, key);
//                        values.put(TableDictionary.COLUMN_VALUE_COUNT, value.size());
//
//                        for (int i = 0; i < value.size(); i++) {
//                            values.put(columnValueNames[i], value.get(i));
//                        }
//                        insert(TableDictionary.TABLE_NAME, values);
//                    }
//                    db.setTransactionSuccessful();
//
//                    if (debug) {
//                        Log.d("DictionaryDBHelper", "init database data success");
//                    }
//
//                } catch (Exception e) {
//                    Log.e("DB ERROR:", "初始化数据库数据时出现错误：" + e.getMessage());
//                } finally {
//                    db.endTransaction();
//                    try {
//                        if (bufferedResourceReader != null) bufferedResourceReader.close();
//                    } catch (Exception e) {
//
//                    }
//                }
//
//            }
//        }).start();
//
//    }

//    public long insert(String tableName, ContentValues values) {
//        long id = db.insert(tableName, null, values);
//        return id;
//    }

    public Cursor queryAll(String tableName) {
        Cursor cursor = db.query(TableDictionary.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor fuzzyQuery(String key) {
        String selection = TableDictionary.COLUMN_KEY + " LIKE '" + key + "%'";
        return db.rawQuery("SELECT * FROM " + TableDictionary.TABLE_NAME + " WHERE " + selection, null);
    }

    public List<String> querySingleLevel1() {
        ArrayList<String> singles = new ArrayList<>();
        Cursor cursor = db.query(TableSingleCharLevel1.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()){
            String data = cursor.getString(TableSingleCharLevel1.COLUMN_VALUE_INDEX);
            singles.add(data);
        }
        cursor.close();
        return singles;
    }
}
