package com.coderpage.wubinput.view.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.db.DictionaryDBHelper;
import com.coderpage.wubinput.model.Content;
import com.coderpage.wubinput.view.widget.ColorText;
import com.coderpage.wubinput.view.widget.InputEditText;
import com.coderpage.wubinput.view.widget.InputView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abner-l
 * @since 2015-08-29
 */
public class PracticeActivity extends Activity {

    private String tag = PracticeActivity.class.getSimpleName();
    private boolean debug = true;

    private TextView remindTV;
    private TextView practiceSourceTV;
    private InputEditText practiceInputET;
    private LinearLayout container;

    private DictionaryDBHelper dbHelper;
    private List<String> lines;
    private List<InputView> inputViews = new ArrayList<>();
    private InputView currentInputView;

    private int currentPos = 0;
    private int maxPos = 0;

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (debug) {
                Log.d(tag, "beforeTextChanged:" + s.toString());
            }

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (debug) {
                Log.d(tag, "onTextChanged:" + s.toString() + "  " + practiceInputET.isFocused());
            }

            onCurChanged(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (debug) {
                Log.d(tag, "afterTextChanged:" + s.toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        dbHelper = DictionaryDBHelper.getInstance(this);

        initContent();
        initView();
    }

    private void initView() {
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_practice_view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        remindTV = (TextView) actionBar.getCustomView().findViewById(R.id.tv_remind);

        container = (LinearLayout) findViewById(R.id.input_view_container);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            InputView inputView = new InputView(PracticeActivity.this);
            practiceSourceTV = inputView.getInputSrcView();
            practiceSourceTV.setText(line);
            practiceInputET = inputView.getInputEditText();
            practiceInputET.setEnabled(false);
            inputViews.add(inputView);
            container.addView(inputView);
        }

        if (inputViews.size() != 0) {
            currentInputView = inputViews.get(0);
            practiceSourceTV = currentInputView.getInputSrcView();
            practiceInputET = currentInputView.getInputEditText();
            practiceInputET.addTextChangedListener(watcher);
            practiceInputET.setEnabled(true);
            practiceInputET.requestFocus();
        }

        this.maxPos = inputViews.size() - 1;

    }

    private void initContent() {

        dbHelper = DictionaryDBHelper.getInstance(PracticeActivity.this);
        List<String> words = dbHelper.querySingleLevel1();
        StringBuilder builder = new StringBuilder();
        for (String s : words) {
            builder.append(s);
        }
        String data = builder.toString();

        if (practiceSourceTV == null) {
            LinearLayout inputView = (LinearLayout) View.inflate(PracticeActivity.this, R.layout.input_view_item, null);
            practiceSourceTV = (TextView) inputView.findViewById(R.id.tv_practice_src);
        }

        Content content = new Content(data, practiceSourceTV);
        this.lines = content.getLines();
    }

    /**
     * 当有数据输入时调用，检查输入的数据是否正确，不正确的会红字显示
     *
     * @param data 输入的数据
     */
    private void onCurChanged(String data) {

        if (!currentInputView.isParent(practiceSourceTV)) {
            practiceSourceTV = currentInputView.getInputSrcView();
        }

        String srcText = practiceSourceTV.getText().toString();
        SpannableString spannableString = new SpannableString(srcText);

        List<ColorText> texts = new ArrayList<>();

        String overData = null;
        if (srcText.length() <= data.length()) {
            int overSize = data.length() - srcText.length();
            overData = data.substring(data.length() - overSize);
            data = data.substring(0, srcText.length());
        }


        for (int i = 0; i < data.length(); i++) {
            ColorText text = new ColorText();

            if (srcText.charAt(i) != data.charAt(i)) {
                text.color = Color.RED;
                text.text = data.charAt(i);
                texts.add(text);
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), i, i + 1, Spanned.SPAN_POINT_MARK);
            } else {
                text.text = data.charAt(i);
                texts.add(text);
            }

        }

        practiceInputET.setTexts(texts);
        practiceSourceTV.setText(spannableString);

        if (overData != null) {

            if (!overData.equals("")) {
                practiceInputET.removeTextChangedListener(watcher);
                practiceInputET.setText(data);
                goNextLine(overData);
            } else {
                goNextLine(overData);
            }

        }
    }

    private void goNextLine(String overData) {
        if (haveNextLine()) {
            practiceInputET.clearFocus();
            practiceInputET.setEnabled(false);
            practiceInputET.removeTextChangedListener(watcher);

            currentInputView = inputViews.get(++currentPos);
            practiceSourceTV = currentInputView.getInputSrcView();
            practiceInputET = currentInputView.getInputEditText();
            practiceInputET.addTextChangedListener(watcher);
            practiceInputET.setEnabled(true);
            practiceInputET.requestFocus();

            if (!TextUtils.isEmpty(overData)) {
                practiceInputET.setText(overData);
                practiceInputET.setSelection(overData.length());
                onCurChanged(overData);
            }

        } else {
            if (practiceInputET.watcherSize == 0) {
                practiceInputET.addTextChangedListener(watcher);
            }
            practiceInputET.setSelection(practiceInputET.getText().length());
            // back result
        }
    }

    private void goPreLine() {

    }

    private boolean havePreLine() {
        return currentPos > 0;
    }

    private boolean haveNextLine() {
        return currentPos < maxPos;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(tag, "onRestart currentPos:" + currentPos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(tag, "onStart currentPos:" + currentPos);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(tag, "onResume currentPos:" + currentPos);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(tag, "onStop currentPos:" + currentPos);
    }
}
