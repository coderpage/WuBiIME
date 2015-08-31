package com.coderpage.wubinput.db;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.coderpage.wubinput.model.Wubi;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abner-l
 * @since 2015-08-29
 */
public class DictionaryDBHelperTest extends AndroidTestCase {
    private DictionaryDBHelper helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = DictionaryDBHelper.getInstance(getContext());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        helper = null;
    }

    public void testQuerySingleLevel1() throws Exception {
        List<String> singles = helper.querySingleLevel1();
        assertNotNull(singles);
        assertTrue(singles.size() > 0);
    }

    public void testFuzzyQuery() throws Exception {
        Cursor cursor = helper.fuzzyQuery("a");
        assertNotNull(cursor);
        assertEquals(4184, cursor.getCount());
        cursor.close();

        cursor = helper.fuzzyQuery("al");
        assertNotNull(cursor);
        assertEquals(163, cursor.getCount());
        cursor.close();
    }

    public void testQueryArticle() throws Exception {
        String article = helper.queryArticle(1);
        assertNotNull(article);
    }

    public void testQueryCode() throws Exception {
        List<String> codes = helper.queryCode("我", Wubi.TYPE_86);
        assertEquals(3, codes.size());
        assertEquals("q", codes.get(0));
        assertEquals("trn", codes.get(1));
        assertEquals("trnt", codes.get(2));

        codes = helper.queryCode("我", Wubi.TYPE_98);
        assertEquals(4, codes.size());
        assertEquals("q", codes.get(0));
        assertEquals("tray", codes.get(1));
        assertEquals("trn", codes.get(2));
        assertEquals("trny", codes.get(3));
    }

    public void testQuerySingleLevel2() {
        List<String> singles = new ArrayList<>(100);
        singles = helper.querySingleLevel2(100);
        assertEquals(100, singles.size());

        StringBuilder builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }
        String single2tmp = builder.toString();

        singles = helper.querySingleLevel2(100);
        assertEquals(100, singles.size());

        builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }

        assertTrue(!single2tmp.equals(builder.toString()));
    }

    public void testQuerySingleLevel3() {
        List<String> singles = new ArrayList<>(100);
        singles = helper.querySingleLevel3(100);
        assertEquals(100, singles.size());

        StringBuilder builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }
        String single2tmp = builder.toString();

        singles = helper.querySingleLevel3(100);
        assertEquals(100, singles.size());

        builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }

        assertTrue(!single2tmp.equals(builder.toString()));
    }

    public void testQuerySingles() {
        List<String> singles = new ArrayList<>(100);
        singles = helper.querySingles(100);
        assertEquals(100, singles.size());

        StringBuilder builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }
        String single2tmp = builder.toString();

        singles = helper.querySingles(100);
        assertEquals(100, singles.size());

        builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }

        assertTrue(!single2tmp.equals(builder.toString()));
    }

    public void testQueryPhrase() {
        List<String> singles = new ArrayList<>(100);
        singles = helper.queryPhrase(100);
        assertEquals(100, singles.size());

        StringBuilder builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }
        String single2tmp = builder.toString();

        singles = helper.queryPhrase(100);
        assertEquals(100, singles.size());

        builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }

        assertTrue(!single2tmp.equals(builder.toString()));
    }

}
