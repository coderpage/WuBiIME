package com.coderpage.wubinput.view.widget;

import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.tools.Utils;

/**
 * @author abner-l
 * @since 2015-08-30
 */
public class InputView extends LinearLayout {
    private TextView inputSrcView;
    private InputEditText inputEditText;

    public InputView(Context context) {
        super(context);
        init(context);
    }

    public InputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    private void init(Context context) {

        int marginSize = Utils.dip2px(context, 10);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, marginSize, 0, marginSize);
        setOrientation(VERTICAL);
        setLayoutParams(layoutParams);


        inputSrcView = (TextView) inflate(context, R.layout.input_src_view, null);
        inputEditText = (InputEditText) inflate(context, R.layout.input_edit_view, null);

        addView(inputSrcView);
        addView(inputEditText);

    }

    public void addTextChangedListener(TextWatcher watcher) {
        inputEditText.addTextChangedListener(watcher);
    }

    public void editEnabled(boolean enable) {
        inputEditText.setEnabled(enable);
    }


    public InputEditText getInputEditText() {
        return inputEditText;
    }

    public TextView getInputSrcView() {
        return inputSrcView;
    }

    public boolean isParent(View view) {
        return inputSrcView.equals(view) || inputEditText.equals(view);
    }
}
