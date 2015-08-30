package com.coderpage.wubinput.tools;

import android.view.View;
import android.widget.TextView;

/**
 *
 * @author abner-l
 * @since 2015-08-30
 */
public class ViewUtil {

    /**
     * 测量 TextView 中一个字符占据的宽度
     * @param view TextView
     * @param text 字符
     * @return 字符宽度
     */
    public static float measureTextWidth(TextView view, String text){
        float size = view.getPaint().measureText(text);
        return size;
    }

    /**
     * 测量一个 View 的宽度
     *
     * @param view
     * @return
     */
    public static int measureViewWidth(View view){
        int width = view.getMeasuredWidth();
        return width;
    }


}
