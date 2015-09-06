package com.coderpage.wubinput.ime;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;

import com.coderpage.wubinput.R;

/**
 * A soft keyboard definition.
 */
public class WuBiKeyboard extends Keyboard {

    public static final int KEYCODE_BACKSPACE = KEYCODE_DELETE;
    public static final int KEYCODE_ENTER = 10;
    public static final int KEYCODE_SYMBOL = 11;
    public static final int KEYCODE_NUMBER = 12;
    public static final int KEYCODE_LANGUAGE = 13;
    public static final int KEYCODE_SPACE = 32;
    private static final String ESCAPE_LABEL = "Esc";

    private final int id;
    private Key symbolKey;
    private Key enterKey;
    private Key shiftKey;

    private Drawable enterIcon;
    private Drawable enterPreviewIcon;
    private Drawable shiftOn;
    private Drawable shiftOff;

    private int enterKeyIndex;
    private boolean escaped;
    private boolean shift;

    public WuBiKeyboard(Context context, int xmlLayoutResId) {
        super(context, xmlLayoutResId);
        id = xmlLayoutResId;
    }

    public boolean isEnglish() {
        return id == R.xml.qwerty;
    }

    public boolean isNumberSymbol() {
        return false;
//        return id == R.xml.symbols;
    }

    public boolean isShiftSymbol() {
        return false;
//        return id == R.xml.symbols_shift;
    }

    /**
     * Returns {@code true} if the current keyboard is the symbol (number-symbol
     * or shift-symbol) keyboard; otherwise returns {@code false}.
     */
    public boolean isSymbols() {
        return isNumberSymbol() || isShiftSymbol();
    }

    /**
     * Updates the on/off status of sticky keys (symbol-key and shift-key).
     */
    public void updateStickyKeys() {
        if (isSymbols()) {
            // Updates the shift-key status for symbol keyboards: shifted-off for
            // number-symbol keyboard and shifted-on for shift-symbol keyboard.
            setShifted(isShiftSymbol());
        }

        if (symbolKey != null) {
            symbolKey.on = isSymbols();
        }
    }

    public int getEscapeKeyIndex() {
        // Escape-key is the enter-key set 'Esc'.
        return enterKeyIndex;
    }

    public boolean hasEscape() {
        return escaped;
    }

    /**
     * Sets enter-key as the escape-key.
     *
     * @return {@code true} if the key is changed.
     */
    public boolean setEscape(boolean escapeState) {
        if ((escaped != escapeState) && (enterKey != null)) {
            if (SoftKeyboardView.canRedrawKey()) {
                if (escapeState) {
                    enterKey.icon = null;
                    enterKey.iconPreview = null;
                    enterKey.label = ESCAPE_LABEL;
                } else {
                    enterKey.icon = enterIcon;
                    enterKey.iconPreview = enterPreviewIcon;
                    enterKey.label = null;
                }
            }
            escaped = escapeState;
            return true;
        }
        return false;
    }

    /**
     * 是否大写模式
     */
    public boolean isShift() {
        return shift;
    }

    /**
     * 设置是否大写模式
     */
    public void setShift(boolean isShift) {
        this.shift = isShift;
        if (isShift) {
            if (shiftOn == null) {
                shiftOn = Resources.getSystem().getDrawable(R.drawable.keyboard_shift_on_icon);
            }
            shiftKey.icon = shiftOn;
            setShift(true);
        } else {
            shiftKey.icon = shiftOff;
            setShift(false);
        }
    }

    @Override
    protected Key createKeyFromXml(Resources res, Row parent, int x, int y, XmlResourceParser parser) {
        Key key = new Keyboard.Key(res, parent, x, y, parser);

        switch (key.codes[0]) {
            case KEYCODE_ENTER:
                enterKey = key;
                enterIcon = key.icon;
                enterPreviewIcon = key.iconPreview;
                enterKeyIndex = getKeys().size();
                escaped = false;
                break;
            case KEYCODE_SYMBOL:
                symbolKey = key;
                break;
            case KEYCODE_SHIFT:
                shiftKey = key;
                shiftOff = shiftKey.icon;
                shift = false;
                break;
            default:
                break;
        }

        return key;
    }
}
