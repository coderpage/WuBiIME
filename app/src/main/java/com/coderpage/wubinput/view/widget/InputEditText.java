package com.coderpage.wubinput.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.List;

/**
 * Created by abner-l on 15/8/30.
 */
public class InputEditText extends EditText {

    public int watcherSize = 0;
    private List<ColorText> texts = null;

    public InputEditText(Context context) {
        super(context);
    }

    public InputEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public static int getFontHeight(TextPaint paint) {

        Paint.FontMetrics localFontMetrics = paint.getFontMetrics();
        return (int) (localFontMetrics.descent - localFontMetrics.ascent - 4.0F);

    }

    public void setTexts(List<ColorText> texts) {
        this.texts = texts;
    }


    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        ++watcherSize;
        super.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        --watcherSize;
        super.removeTextChangedListener(watcher);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TextPaint paint = getPaint();
        int fontHeight = getFontHeight(paint);
        int xCoordinate = 0;

        if (texts == null) {
            return;
        }

        String drawText;
        for (ColorText text : texts) {

            drawText = String.valueOf(text.text);
            paint.setColor(text.color);
            canvas.drawText(drawText, xCoordinate, fontHeight, paint);
            xCoordinate = (int) (xCoordinate + paint.measureText(drawText));
        }

    }
}
