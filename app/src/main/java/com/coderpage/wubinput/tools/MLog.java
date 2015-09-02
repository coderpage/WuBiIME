package com.coderpage.wubinput.tools;

import android.util.Log;

/**
 * @author abner-l
 * @since 2015-09-02
 */
public class MLog {

    public static void info(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void debug(String tag, String msg) {
        Log.d(tag, msg);
    }


    public static void error(String tag, String msg) {
        Log.e(tag, msg);
    }
}
