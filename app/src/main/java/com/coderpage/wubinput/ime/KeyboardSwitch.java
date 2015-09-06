package com.coderpage.wubinput.ime;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.text.InputType;

import com.coderpage.wubinput.R;

public class KeyboardSwitch {
    private final String tag = KeyboardSwitch.class.getSimpleName();
    private final boolean debug = true;

    final static int KEYBOARD_WUBI = 0x1;
    final static int KEYBOARD_ENGLISH = 0x2;
    final static int KEYBOARD_NUMBLE = 0x3;
    final static int KEYBOARD_SYMBOL = 0x4;


    private final Context context;
    private Keyboard currentKeyboard;
    private int currentKeyboardType;

    private WuBiKeyboard wuBiKeyboard;
    private EnglishKeyboard englishKeyboard;
    private WuBiKeyboard symbolKeyboard;
    private WuBiKeyboard numberKeyboard;

    private int currentDisplayWidth;

    public KeyboardSwitch(Context context) {
        this.context = context;
    }

    public void initializeKeyboard(int displayWidth) {
        if ((currentKeyboard != null) && (displayWidth == currentDisplayWidth)) {
            return;
        }
        currentDisplayWidth = displayWidth;
        wuBiKeyboard = new WuBiKeyboard(context, R.xml.qwerty);
        englishKeyboard = new EnglishKeyboard(context, R.xml.qwerty_english);
        if (currentKeyboard == null) {
            toChinese();
        }
    }

    public Keyboard getKeyboard() {
        return currentKeyboard;
    }

    public void onStartInput(int inputType) {
        switch (inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_DATETIME:
            case InputType.TYPE_CLASS_PHONE:
                toNumber();
                break;

            case InputType.TYPE_CLASS_TEXT:
                int variation = inputType & InputType.TYPE_MASK_VARIATION;
                if ((variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                        || (variation == InputType.TYPE_TEXT_VARIATION_URI)
                        || (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        || (variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    toEnglish();
                } else {
                    toChinese();
                }
                break;
            default:
                toChinese();
        }
    }

    public boolean onKey(int keyCode) {
        switch (keyCode) {
            case WuBiKeyboard.KEYCODE_LANGUAGE:
                if (currentKeyboard instanceof EnglishKeyboard) {
                    toChinese();
                } else {
                    toEnglish();
                }
                return true;
            default:
                break;
        }
        return false;
    }

    private void toChinese() {
        currentKeyboard = wuBiKeyboard;
        currentKeyboardType = KEYBOARD_WUBI;
    }

    private void toSymbol() {
        currentKeyboard = symbolKeyboard;
        currentKeyboardType = KEYBOARD_SYMBOL;
    }

    private void toNumber() {
        currentKeyboard = numberKeyboard;
        currentKeyboardType = KEYBOARD_NUMBLE;
    }

    private void toEnglish() {
        currentKeyboard = englishKeyboard;
        currentKeyboardType = KEYBOARD_ENGLISH;
    }

    int getCurrentKeyboardType() {
        return currentKeyboardType;
    }

}
