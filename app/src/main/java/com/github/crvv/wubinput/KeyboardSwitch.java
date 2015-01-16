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

package com.github.crvv.wubinput;

import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.text.InputType;

/**
 * Switches between four input modes: two symbol modes (number-symbol and
 * shift-symbol) and two letter modes (Chinese and English), by three toggling
 * keys: mode-change-letter, mode-change, and shift keys.
 * <p/>
 * <pre>
 * State transition (the initial state is always 'English'):
 *   English
 *     MODE_CHANGE_LETTER -> Chinese
 *     MODE_CHANGE -> NumberSymbol
 *     SHIFT -> English (no-op)
 *   Chinese
 *     MODE_CHANGE_LETTER -> English
 *     MODE_CHANGE -> NumberSymbol
 *     SHIFT (n/a)
 *   NumberSymbol
 *     MODE_CHANGE_LETTER (n/a)
 *     MODE_CHANGE -> English or Chinese
 *     SHIFT -> ShiftSymbol
 *   ShiftSymbol
 *     MODE_CHANGE_LETTER (n/a)
 *     MODE_CHANGE -> English or Chinese
 *     SHIFT -> NumberSymbol
 * </pre>
 */
public class KeyboardSwitch {

    private final Context context;
    private SoftKeyboard mKeyboard;

    private boolean wasEnglishToSymbol;
    private int currentDisplayWidth;

    public KeyboardSwitch(Context context) {
        this.context = context;
    }

    /**
     * Recreates the keyboards if the display-width has been changed.
     *
     * @param displayWidth the display-width for keyboards.
     */
    public void initializeKeyboard(int displayWidth) {
        if ((mKeyboard != null) && (displayWidth == currentDisplayWidth)) {
            return;
        }

        currentDisplayWidth = displayWidth;
        mKeyboard = new SoftKeyboard(context, R.xml.qwerty);

        if (mKeyboard == null) {
            // Select English keyboard at the first time the input method is launched.
            toEnglish();
        } else {
            // Preserve the selected keyboard and its shift-status.
            boolean isShifted = mKeyboard.isShifted();
            toEnglish();
            // Restore shift-status.
            mKeyboard.setShifted(isShifted);
        }
    }

    public Keyboard getKeyboard() {
        return mKeyboard;
    }

    /**
     * Switches to the appropriate keyboard based on the type of text being
     * edited, for example, the symbol keyboard for numbers.
     *
     * @param inputType one of the {@code InputType.TYPE_CLASS_*} values listed in
     *                  {@link android.text.InputType}.
     */
    public void onStartInput(int inputType) {
        switch (inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_DATETIME:
            case InputType.TYPE_CLASS_PHONE:
                // Numbers, dates, and phones default to the symbol keyboard, with
                // no extra features.
                toNumberSymbol();
                break;

            case InputType.TYPE_CLASS_TEXT:
                int variation = inputType & InputType.TYPE_MASK_VARIATION;
                if ((variation == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                        || (variation == InputType.TYPE_TEXT_VARIATION_URI)
                        || (variation == InputType.TYPE_TEXT_VARIATION_PASSWORD)
                        || (variation == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    toEnglish();
                } else {
                    // Switch to non-symbol keyboard, either Chinese or English keyboard,
                    // for other general text editing.
//                    toNonSymbols();
                }
                break;

            default:
                // Switch to non-symbol keyboard, either Chinese or English keyboard,
                // for all other input types.
                toEnglish();
        }
    }

    /**
     * Consumes the pressed key-code and switch keyboard if applicable.
     *
     * @return {@code true} if the keyboard is switched; otherwise {@code false}.
     */
    public boolean onKey(int keyCode) {

        // Return false if the key isn't consumed to switch a keyboard.
        return false;
    }

    /**
     * Switches to the number-symbol keyboard and remembers if it was English.
     */
    private void toNumberSymbol() {
        if (!mKeyboard.isSymbols()) {
            // Remember the current non-symbol keyboard to switch back from symbols.
            wasEnglishToSymbol = mKeyboard.isEnglish();
        }

//        mKeyboard = numberSymbolKeyboard;
    }


    private void toEnglish() {
        mKeyboard = mKeyboard;
    }


    /**
     * Switches from symbol (number-symbol or shift-symbol) keyboard,
     * back to the non-symbol (English or Chinese) keyboard.
     */
}
