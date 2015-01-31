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
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Shows a soft keyboard, rendering keys and detecting key presses.
 */
public class SoftKeyboardView extends KeyboardView {

    private SoftKeyboard currentKeyboard;
    private boolean capsLock;

    private static Method invalidateKeyMethod;

    static {
        try {
            invalidateKeyMethod = KeyboardView.class.getMethod(
                    "invalidateKey", new Class[]{int.class});
        } catch (NoSuchMethodException e) {
        }
    }

    public static boolean canRedrawKey() {
        return invalidateKeyMethod != null;
    }

    public SoftKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SoftKeyboardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private boolean canCapsLock() {
        // Caps-lock can only be toggled on English keyboard.
        return (currentKeyboard != null) && currentKeyboard.isEnglish();
    }

    public boolean toggleCapsLock() {
        if (canCapsLock()) {
            capsLock = !isShifted();
            setShifted(capsLock);
            return true;
        }
        return false;
    }

    public void updateCursorCaps(int caps) {
        if (canCapsLock()) {
            setShifted(capsLock || (caps != 0));
        }
    }

    public boolean hasEscape() {
        return (currentKeyboard != null) && currentKeyboard.hasEscape();
    }

    public void setEscape(boolean escape) {
        if ((currentKeyboard != null) && currentKeyboard.setEscape(escape)) {
            invalidateEscapeKey();
        }
    }

    private void invalidateEscapeKey() {
        // invalidateKey method is only supported since 1.6.
        if (invalidateKeyMethod != null) {
            try {
                invalidateKeyMethod.invoke(this, currentKeyboard.getEscapeKeyIndex());
            }
            catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                Log.e("SoftKeyboardView", "exception: ", e);
            }
        }
    }
    @Override
    public void setKeyboard(Keyboard keyboard) {
        if (keyboard instanceof SoftKeyboard) {
            boolean escape = hasEscape();
            currentKeyboard = (SoftKeyboard) keyboard;
            currentKeyboard.updateStickyKeys();
            currentKeyboard.setEscape(escape);
        }
        super.setKeyboard(keyboard);
    }
}
