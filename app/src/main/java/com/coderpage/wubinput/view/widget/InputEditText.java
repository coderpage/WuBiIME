package com.coderpage.wubinput.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import java.util.List;

/**
 * Created by abner-l on 15/8/30.
 */
public class InputEditText extends EditText {

    private boolean red = false;
    private List<ColorText> texts = null;

    public InputEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public static int getFontHeight(TextPaint paramTextPaint) {

        Paint.FontMetrics localFontMetrics = paramTextPaint.getFontMetrics();
        return (int) (localFontMetrics.descent - localFontMetrics.ascent - 4.0F);

    }

    public void setTexts(List<ColorText> texts) {
        this.texts = texts;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TextPaint paint = getPaint();
        int i = getFontHeight(paint);
        int j = 0;

        if (texts == null) {
            return;
        }

        String drawText;
        for (ColorText text : texts) {
            drawText = String.valueOf(text.text);
            paint.setColor(text.color);
            canvas.drawText(drawText, j, i, paint);
            j = (int) (j + paint.measureText(drawText));

        }
    }
}
