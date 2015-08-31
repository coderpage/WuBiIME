package com.coderpage.wubinput.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.model.Wubi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.coderpage.wubinput.define.SQLDefine.TableArticle;
import static com.coderpage.wubinput.define.SQLDefine.TableDictionary;
import static com.coderpage.wubinput.define.SQLDefine.TableSingleChar;
import static com.coderpage.wubinput.define.SQLDefine.TableSingleCharLevel1;
import static com.coderpage.wubinput.define.SQLDefine.TableSingleCharLevel2;
import static com.coderpage.wubinput.define.SQLDefine.TableSingleCharLevel3;
import static com.coderpage.wubinput.define.SQLDefine.TablePhrase;
import static com.coderpage.wubinput.define.SQLDefine.TableWuBi86;
import static com.coderpage.wubinput.define.SQLDefine.TableWuBi98;


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

    public Cursor queryAll(String tableName) {
        Cursor cursor = db.query(TableDictionary.TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }

    public Cursor fuzzyQuery(String key) {
        String selection = TableDictionary.COLUMN_KEY + " LIKE '" + key + "%'";
        return db.rawQuery("SELECT * FROM " + TableDictionary.TABLE_NAME + " WHERE " + selection, null);
    }

    /**
     * 查询单字一级编码
     *
     * @return 所有一级编码的单字集合
     */
    public List<String> querySingleLevel1() {
        ArrayList<String> singles = new ArrayList<>();
        Cursor cursor = db.query(TableSingleCharLevel1.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String data = cursor.getString(TableSingleCharLevel1.COLUMN_VALUE_INDEX);
            singles.add(data);
        }
        cursor.close();
        return singles;
    }


    /**
     * 随机查询二级编码
     *
     * @param number 需要查询的个数
     * @return 查询到的二级编码单字集合
     */
    public List<String> querySingleLevel2(int number) {
        ArrayList<String> singles = new ArrayList<>(number);
        String[] columns = {TableSingleCharLevel2.COLUMN_VALUE};
        String orderBy = "random()";
        String limit = String.valueOf(number);

        Cursor cursor = db.query(TableSingleCharLevel2.TABLE_NAME, columns, null, null, null, null, orderBy, limit);
        while (cursor.moveToNext()) {
            String data = cursor.getString(0);
            singles.add(data);
        }
        cursor.close();
        return singles;
    }

    /**
     * 随机查询三级编码
     *
     * @param number 需要查询的个数
     * @return 查询到的三级编码单字集合
     */
    public List<String> querySingleLevel3(int number) {
        ArrayList<String> singles = new ArrayList<>(number);
        String[] columns = {TableSingleCharLevel3.COLUMN_VALUE};
        String orderBy = "random()";
        String limit = String.valueOf(number);

        Cursor cursor = db.query(TableSingleCharLevel3.TABLE_NAME, columns, null, null, null, null, orderBy, limit);
        while (cursor.moveToNext()) {
            String data = cursor.getString(0);
            singles.add(data);
        }
        cursor.close();
        return singles;
    }

    /**
     * 随机查询指定数量的单字
     *
     * @param number 需要查询的个数
     * @return 查询到的单字集合
     */
    public List<String> querySingles(int number) {
        ArrayList<String> singles = new ArrayList<>(number);
        String[] columns = {TableSingleChar.COLUMN_VALUE};
        String orderBy = "random()";
        String limit = String.valueOf(number);

        Cursor cursor = db.query(TableSingleChar.TABLE_NAME, columns, null, null, null, null, orderBy, limit);
        while (cursor.moveToNext()) {
            String data = cursor.getString(0);
            singles.add(data);
        }
        cursor.close();
        return singles;
    }


    /**
     * 随机查询指定数量的词组
     *
     * @param number 需要查询的个数
     * @return 查询到的词组集合
     */
    public List<String> queryPhrase(int number) {
        ArrayList<String> singles = new ArrayList<>(number);
        String[] columns = {TablePhrase.COLUMN_VALUE};
        String orderBy = "random()";
        String limit = String.valueOf(number);

        Cursor cursor = db.query(TablePhrase.TABLE_NAME, columns, null, null, null, null, orderBy, limit);
        while (cursor.moveToNext()) {
            String data = cursor.getString(0);
            singles.add(data);
        }

        cursor.close();
        return singles;
    }

    /**
     * 查询文章
     *
     * @param num 文章序号
     * @return 文章内容
     */
    public String queryArticle(int num) {
        String article = null;
        String[] columns = {TableArticle.COLUMN_VALUE};
        String selection = TableArticle.COLUMN_ID + "=?";
        String[] args = {String.valueOf(num)};
        Cursor cursor = db.query(TableArticle.TABLE_NAME, columns, selection, args, null, null, null);

        int index = cursor.getColumnIndex(TableArticle.COLUMN_VALUE);
        while (cursor.moveToNext()) {
            article = cursor.getString(index);
        }

        cursor.close();
        return article;
    }

    /**
     * 查询单字的五笔编码
     *
     * @param word 需要查询的字符
     * @param type 查询的编码格式 {@link Wubi#TYPE_86} {@link Wubi#TYPE_98}
     * @return 对应五笔编码集合
     */
    public List<String> queryCode(String word, int type) {

        if (type == Wubi.TYPE_98) {
            return queryCodeType98(word);
        }

        return queryCodeType86(word);
    }

    private List<String> queryCodeType86(String word) {

        String[] columns = {TableWuBi86.COLUMN_KEY};
        String selection = TableWuBi86.COLUMN_VALUE + "=?";
        String[] args = {word};

        Cursor cursor = db.query(TableWuBi86.TABLE_NAME, columns, selection, args, null, null, null);
        List<String> codes = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext()) {
            String code = cursor.getString(0);
            codes.add(code);
        }

        cursor.close();
        return codes;
    }

    private List<String> queryCodeType98(String word) {


        String[] columns = {TableWuBi98.COLUMN_KEY};
        String selection = TableWuBi98.COLUMN_VALUE + "=?";
        String[] args = {word};

        Cursor cursor = db.query(TableWuBi98.TABLE_NAME, columns, selection, args, null, null, null);
        List<String> codes = new ArrayList<>(cursor.getCount());

        while (cursor.moveToNext()) {
            String code = cursor.getString(0);
            codes.add(code);
        }

        cursor.close();
        return codes;
    }


}
