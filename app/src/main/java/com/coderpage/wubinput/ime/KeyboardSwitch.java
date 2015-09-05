package com.coderpage.wubinput.ime;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.text.InputType;

import com.coderpage.wubinput.R;

public class KeyboardSwitch {

    private final Context context;
    private SoftKeyboard currentKeyboard;

    private SoftKeyboard qwertyKeyboard;
    private SoftKeyboard symbolKeyboard;
    private SoftKeyboard numberKeyboard;

    private int currentDisplayWidth;

    public KeyboardSwitch(Context context) {
        this.context = context;
    }

    public void initializeKeyboard(int displayWidth) {
        if ((currentKeyboard != null) && (displayWidth == currentDisplayWidth)) {
            return;
        }
        currentDisplayWidth = displayWidth;
        qwertyKeyboard = new SoftKeyboard(context, R.xml.qwerty);
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

        return false;
    }

    private void toChinese(){
        currentKeyboard = qwertyKeyboard;
        //TODO
    }
    private void toSymbol(){
        currentKeyboard = symbolKeyboard;
    }
    private void toNumber(){
        currentKeyboard = numberKeyboard;
    }
    private void toEnglish(){
        //TODO
    }

}
