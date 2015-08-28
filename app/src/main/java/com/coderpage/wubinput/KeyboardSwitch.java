/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coderpage.wubinput;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.text.InputType;

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
