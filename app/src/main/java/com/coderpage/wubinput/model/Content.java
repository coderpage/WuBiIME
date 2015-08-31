package com.coderpage.wubinput.model;

import android.widget.TextView;

import com.coderpage.wubinput.Global;
import com.coderpage.wubinput.tools.Utils;
import com.coderpage.wubinput.tools.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abner-l
 * @since 2015-08-29
 */
public class Content {

    private final List<String> lines = new ArrayList<>();


    public Content(String content, TextView view) {
        Global global = Global.getInstance(view.getContext());

        float oneHanZiWidth = ViewUtil.measureTextWidth(view, String.valueOf('我'));
        float lineMaxLen = global.windowWidth - Utils.dip2px(view.getContext(),20);
        float totalLen = 0;
        int sPos = 0;
        String line;

        for (int i = 0; i < content.length(); i++) {
//          totalLen += ViewUtil.measureTextWidth(view, String.valueOf(content.charAt(i)));
            totalLen += oneHanZiWidth;

            if (totalLen >= lineMaxLen) {
                line = content.substring(sPos, i);
                sPos = i;
                lines.add(line);
                totalLen = oneHanZiWidth;
            }
        }

        line = content.substring(sPos);
        lines.add(line);
    }

    public List<String> getLines() {
        return lines;
    }
}
