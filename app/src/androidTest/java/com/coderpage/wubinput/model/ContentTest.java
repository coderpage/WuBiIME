package com.coderpage.wubinput.model;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.TextView;

import com.coderpage.wubinput.db.DictionaryDBHelper;
import com.coderpage.wubinput.tools.Utils;
import com.coderpage.wubinput.view.activity.BaseInputActivity;

import java.util.List;

/**
 * @author abner-l
 * @since 2015-08-30
 */
public class ContentTest extends ActivityInstrumentationTestCase2<BaseInputActivity> {

    Activity activity;

    public ContentTest() {
        super(BaseInputActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.activity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstructor() {
//        TextView view = (TextView) activity.findViewById(R.id.tv_practice_src);
        TextView view = new TextView(activity);
        view.setTextSize(Utils.dip2px(activity, 20));

        DictionaryDBHelper helper = DictionaryDBHelper.getInstance(getActivity());
        List<String> singles = helper.querySingleLevel1();

        StringBuilder builder = new StringBuilder();
        for (String s : singles) {
            builder.append(s);
        }
        String data = builder.toString();

        Content content = new Content(data, view);
        List<String> lines = content.getLines();

        assertTrue(lines.size() > 0);

        builder.setLength(0);
        for (String line : lines) {
            Log.d("line", line);
            builder.append(line);
        }

        assertEquals(data, builder.toString());

    }
}
