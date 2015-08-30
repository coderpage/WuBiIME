package com.coderpage.wubinput.tools;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.view.activity.PracticeActivity;

/**
 * @author abner-l
 * @since 2015-8-30
 */
public class ViewUtilTest extends ActivityInstrumentationTestCase2<PracticeActivity> {

    Activity activity;

    public ViewUtilTest() {
        super(PracticeActivity.class);
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

    public void testMeasureTextWidth() {
        TextView view = (TextView) activity.findViewById(R.id.tv_practice_src);

        float size = ViewUtil.measureTextWidth(view, "我");
        assertTrue(size > 0);

        size = ViewUtil.measureTextWidth(view, ",");
        assertTrue(size > 0);

        size = ViewUtil.measureTextWidth(view, "，");
        assertTrue(size > 0);

        size = ViewUtil.measureTextWidth(view, "1");
        assertTrue(size > 0);
    }

    public void testMeasureViewWidth() {
        TextView view = (TextView) activity.findViewById(R.id.tv_practice_src);

        int size = ViewUtil.measureViewWidth(view);
        assertTrue(size > 0);
    }

}
