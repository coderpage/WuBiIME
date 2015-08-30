package com.coderpage.wubinput;

import android.content.Context;
import android.view.WindowManager;

/**
 * @author abner-l
 * @since 2015-08-30
 */
public class Global {
    private static Global instance = null;

    public int windowWidth;
    public int windowHeight;

    public static Global getInstance(Context ctx) {
        if (instance == null) {
            instance = new Global(ctx);
        }

        return instance;
    }

    private Global(Context ctx) {
        WindowManager manager = (WindowManager)ctx.getSystemService(Context.WINDOW_SERVICE);
        this.windowWidth = manager.getDefaultDisplay().getWidth();
        this.windowHeight = manager.getDefaultDisplay().getHeight();
    }
}
