package com.coderpage.wubinput.ime;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.Keyboard;

/**
 * @author abner-l
 * @since 2015-09-05
 */
public class EnglishKeyboard extends Keyboard {

    public EnglishKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {



        return super.createKeyFromXml(res, parent, x, y, parser);
    }
}
