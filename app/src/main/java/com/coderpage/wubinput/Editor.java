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

import android.text.InputType;
import android.view.inputmethod.InputConnection;

/**
 * Updates the editing field and handles composing-text.
 */
public class Editor {
    protected StringBuilder composingText = new StringBuilder();
    private boolean canCompose;
    private boolean enterAsLineBreak;

    public CharSequence composingText() {
        return composingText;
    }

    public boolean hasComposingText() {
        return composingText.length() > 0;
    }

    /**
     * Resets the internal state of this editor, typically called when a new input
     * session commences.
     */
    public void start(int inputType) {
        composingText.setLength(0);
        canCompose = true;
        enterAsLineBreak = false;

        switch (inputType & InputType.TYPE_MASK_CLASS) {
            case InputType.TYPE_CLASS_NUMBER:
            case InputType.TYPE_CLASS_DATETIME:
            case InputType.TYPE_CLASS_PHONE:
                // Composing is disabled for number, date-time, and phone input types.
                canCompose = false;
                break;

            case InputType.TYPE_CLASS_TEXT:
                int variation = inputType & InputType.TYPE_MASK_VARIATION;
                if (variation == InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE) {
                    // Make enter-key as line-breaks for messaging.
                    enterAsLineBreak = true;
                }
                break;
        }
    }

    public void clearComposingText(InputConnection ic) {
        if (hasComposingText()) {
            // Clear composing only when there's composing-text to avoid the selected
            // text being cleared unexpectedly.
            composingText.setLength(0);
            updateComposingText(ic);
        }
    }

    private void updateComposingText(InputConnection ic) {
        if (ic != null) {
            // Set cursor position 1 to advance the cursor to the text end.
            // TODO: if composingText.length()==0, E/SpannableStringBuilder﹕ SPAN_EXCLUSIVE_EXCLUSIVE spans cannot have a zero length
            ic.setComposingText(composingText, 1);
        }
    }

    private boolean deleteLastComposingChar(InputConnection ic) {
        if (hasComposingText()) {
            // Delete-key are accepted only when there's text in composing.
            composingText.deleteCharAt(composingText.length() - 1);
            updateComposingText(ic);
            return true;
        }
        return false;
    }

    /**
     * Commits the given text to the editing field.
     */
    public boolean commitText(InputConnection ic, CharSequence text) {
        if (ic != null) {
            if (text.length() > 1) {
                // Batch edit a sequence of characters.
                ic.beginBatchEdit();
                ic.commitText(text, 1);
                ic.endBatchEdit();
            } else {
                ic.commitText(text, 1);
            }
            // Composing-text in the editor has been cleared.
            composingText.setLength(0);
            return true;
        }
        return false;
    }

    public boolean treatEnterAsLinkBreak() {
        return enterAsLineBreak;
    }

    /**
     * Composes the composing-text further with the specified key-code.
     *
     * @return {@code true} if the key is handled and consumed for composing.
     */
    public boolean compose(InputConnection ic, int keyCode) {
        if (keyCode == SoftKeyboard.KEYCODE_BACKSPACE) {
            return deleteLastComposingChar(ic);
        }

        if (canCompose && doCompose(keyCode, ic)) {
            updateComposingText(ic);
            return true;
        }
        return false;
    }

    /**
     * Composes the key-code into the composing-text by composing rules.
     */
    public boolean doCompose(int keyCode, InputConnection ic) {
        int maxLength = 4;

        char c = (char) keyCode;
        if (keyCode < 97 || keyCode > 122) {
            return false;
        }
        if (composingText.length() >= maxLength) {
            CandidatesManager.getInstance().pickCandidate(1);
            clearComposingText(ic);
        }
        composingText.append(c);
        return true;
    }
}
