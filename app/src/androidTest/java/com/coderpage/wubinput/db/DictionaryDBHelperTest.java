package com.coderpage.wubinput.db;

import android.database.Cursor;
import android.test.AndroidTestCase;

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

}
