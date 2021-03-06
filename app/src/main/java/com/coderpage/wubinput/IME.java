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
import android.content.Intent;
import android.content.res.Configuration;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.KeyboardView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.ArrayList;


public class IME extends InputMethodService implements
        KeyboardView.OnKeyboardActionListener,
        CandidatesManager.CandidateViewListener {

    protected SoftKeyboardView inputView;
    private View mCandidatesView;
    private CandidatesManager mCandidatesManager;
    private KeyboardSwitch keyboardSwitch;
    private Editor editor;
    private int orientation;

    private KeyboardSwitch createKeyboardSwitch(Context context){
        return new KeyboardSwitch(context);
    }

    private Editor createEditor(){
            return new Editor();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        keyboardSwitch = createKeyboardSwitch(this);
        editor = createEditor();
        Dictionary.getInstance(this);

        orientation = getResources().getConfiguration().orientation;
        // Use the following line to debug IME service.
        //android.os.Debug.waitForDebugger();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (orientation != newConfig.orientation) {
            // Clear composing text and candidates for orientation change.
            escape();
            orientation = newConfig.orientation;
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart,
                                  int newSelEnd, int candidatesStart, int candidatesEnd) {
        super.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd,
                candidatesStart, candidatesEnd);
        if ((candidatesEnd != -1) &&
                ((newSelStart != candidatesEnd) || (newSelEnd != candidatesEnd))) {
            // Clear composing text and its candidates for cursor movement.
            escape();
        }
        // Update the caps-lock status for the current cursor position.
        updateCursorCapsToInputView();
    }

    @Override
    public void onComputeInsets(InputMethodService.Insets outInsets) {
        super.onComputeInsets(outInsets);
        outInsets.contentTopInsets = outInsets.visibleTopInsets;
    }

    @Override
    public View onCreateInputView() {
        inputView = (SoftKeyboardView) getLayoutInflater().inflate(R.layout.input, null);
        inputView.setOnKeyboardActionListener(this);
        inputView.setPreviewEnabled(false);
        return inputView;
    }

    @Override
    public View onCreateCandidatesView() {
        mCandidatesManager = CandidatesManager.getInstance(this);
        mCandidatesView = mCandidatesManager.getCandidatesView();
        mCandidatesManager.setCandidateViewListener(this);
        return mCandidatesView;
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);
        setCandidatesViewShown(true);

        // Reset editor and candidates when the input-view is just being started.
        editor.start(attribute.inputType);
        clearCandidates();

        keyboardSwitch.initializeKeyboard(getMaxWidth());
        // Select a keyboard based on the input type of the editing field.
        keyboardSwitch.onStartInput(attribute.inputType);
        bindKeyboardToInputView();
    }

    @Override
    public void onFinishInput() {
        // Clear composing as any active composing text will be finished, same as in
        // onFinishInputView, onFinishCandidatesView, and onUnbindInput.
        editor.clearComposingText(getCurrentInputConnection());
        super.onFinishInput();
    }

    @Override
    public void onFinishInputView(boolean finishingInput) {
        editor.clearComposingText(getCurrentInputConnection());
        super.onFinishInputView(finishingInput);
        // Dismiss any pop-ups when the input-view is being finished and hidden.
        inputView.closing();
    }

    @Override
    public void onFinishCandidatesView(boolean finishingInput) {
        editor.clearComposingText(getCurrentInputConnection());
        super.onFinishCandidatesView(finishingInput);
    }

    @Override
    public void onUnbindInput() {
        editor.clearComposingText(getCurrentInputConnection());
        super.onUnbindInput();
    }

    private void bindKeyboardToInputView() {
        if (inputView != null) {
            // Bind the selected keyboard to the input view.
            inputView.setKeyboard(keyboardSwitch.getKeyboard());
            updateCursorCapsToInputView();
        }
    }

    private void updateCursorCapsToInputView() {
        InputConnection ic = getCurrentInputConnection();
        if ((ic != null) && (inputView != null)) {
            int caps = 0;
            EditorInfo ei = getCurrentInputEditorInfo();
            if ((ei != null) && (ei.inputType != EditorInfo.TYPE_NULL)) {
                caps = ic.getCursorCapsMode(ei.inputType);
            }
            inputView.updateCursorCaps(caps);
        }
    }

    private void commitText(CharSequence text) {
        if (editor.commitText(getCurrentInputConnection(), text)) {
            // Clear candidates after committing any text.
            clearCandidates();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && (event.getRepeatCount() == 0)) {
            // Handle the back-key to close the pop-up keyboards.
            if ((inputView != null) && inputView.handleBack()) {
                return true;
            }
        }
        else{
            //TODO
            switch(keyCode){
                case KeyEvent.KEYCODE_DEL: //backspace
                    onKey(SoftKeyboard.KEYCODE_BACKSPACE, null);
                    return true;
                case KeyEvent.KEYCODE_ESCAPE: //esc
                case KeyEvent.KEYCODE_SHIFT_LEFT: //left shift
                case KeyEvent.KEYCODE_SHIFT_RIGHT: //right shift
                case KeyEvent.KEYCODE_CTRL_LEFT: //left control
                case KeyEvent.KEYCODE_FORWARD_DEL: //delete
                case KeyEvent.KEYCODE_ENTER: //enter
                case KeyEvent.KEYCODE_SPACE: //space
                case KeyEvent.KEYCODE_TAB: //tab
                    onKey(event.getUnicodeChar(), null);
                    return true;
            }
            if(keyCode >= KeyEvent.KEYCODE_0 && keyCode <= KeyEvent.KEYCODE_9){
                onKey(event.getUnicodeChar(), null);
                return true;
            }
            if(keyCode >= KeyEvent.KEYCODE_A && keyCode <= KeyEvent.KEYCODE_PERIOD){
                onKey(event.getUnicodeChar(), null);
                return true;
            }
//            commitText(String.format("%04d", event.getKeyCode()));
//            commitText(String.format("%04d", event.getUnicodeChar()));
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onKey(int primaryCode, int[] keyCodes) {
        if (keyboardSwitch.onKey(primaryCode)) {
            escape();
            bindKeyboardToInputView();
            return;
        }
        if (handleCapsLock(primaryCode)
                || handleEnter(primaryCode) || handleSpace(primaryCode)
                || handleDelete(primaryCode) || handleComposing(primaryCode)) {
            return;
        }
        handleKey(primaryCode);
    }

    public void onText(CharSequence text) {
        commitText(text);
    }

    public void onPress(int primaryCode) {
        //Sound and vibrate
    }

    public void onRelease(int primaryCode) {
        // no-op
    }

    public void swipeLeft() {
        // no-op
    }

    public void swipeRight() {
        // no-op
    }

    public void swipeUp() {
        // no-op
    }

    public void swipeDown() {
        requestHideSelf(0);
    }

    public void onPickCandidate(String candidate) {
        commitText(candidate);
    }

    private void clearCandidates() {
        setCandidates(null);
    }

    private void setCandidates(ArrayList<String> words) {
        if (mCandidatesView != null) {
            mCandidatesManager.setCandidates(words);
            if (inputView != null) {
                inputView.setEscape(editor.hasComposingText());
            }
        }
    }
    private boolean handleCapsLock(int keyCode) {
        return (keyCode == SoftKeyboard.KEYCODE_SHIFT) && inputView.toggleCapsLock();
    }

    private boolean handleEnter(int keyCode) {
        if (keyCode == '\n') {
            if (editor.hasComposingText()) {
                escape();
            } else if (editor.treatEnterAsLinkBreak()) {
                commitText("\n");
            } else {
                sendKeyChar('\n');
            }
            return true;
        }
        return false;
    }

    private boolean handleSpace(int keyCode) {
        if (keyCode == ' ') {
            if (mCandidatesManager.hasCandidate() > 0) {
                mCandidatesManager.pickCandidate(1);
            } else {
                commitText(" ");
            }
            return true;
        }
        return false;
    }

    private boolean handleDelete(int keyCode) {
        // Handle delete-key only when no composing text.
        if ((keyCode == SoftKeyboard.KEYCODE_BACKSPACE) && !editor.hasComposingText()) {
            if (inputView.hasEscape()) {
                escape();
            } else {
                sendDownUpKeyEvents(KeyEvent.KEYCODE_DEL);
            }
            return true;
        }
        return false;
    }

    private boolean handleComposing(int keyCode) {
        if (editor.compose(getCurrentInputConnection(), keyCode)) {
            // Set the candidates for the updated composing-text and provide default
            // highlight for the word candidates.
            setCandidates(Dictionary.getInstance(this).getCandidates(editor.composingText().toString()));
            return true;
        }
        return false;
    }

    /**
     * Handles input of SoftKeyboard key code that has not been consumed by
     * other handling-methods.
     */
    private void handleKey(int keyCode) {
        if (isInputViewShown() && inputView.isShifted()) {
            keyCode = Character.toUpperCase(keyCode);
        }
        commitText(String.valueOf((char) keyCode));
    }

    /**
     * Simulates PC Esc-key function by clearing all composing-text or candidates.
     */
    protected void escape() {
        editor.clearComposingText(getCurrentInputConnection());
        clearCandidates();
    }
    public void onClick(View view){
        if(view.getId() == R.id.setting_button){
            Intent pref = new Intent(this, ImePreferenceActivity.class);
            pref.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(pref);
            return;
        }
        mCandidatesManager.onClick(view);
    }


    @Override
    public boolean onEvaluateInputViewShown(){
        return true;
    }
    @Override
    public boolean onEvaluateFullscreenMode(){
        return false;
    }
}
