package com.coderpage.wubinput.view.activity;

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
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.db.DictionaryDBHelper;
import com.coderpage.wubinput.view.widget.ColorText;
import com.coderpage.wubinput.view.widget.InputEditText;
import com.coderpage.wubinput.view.widget.InputView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author abner-l
 * @since 2015-08-29
 */
public abstract class BaseInputActivity extends Activity {

    private String tag = BaseInputActivity.class.getSimpleName();
    private boolean debug = true;

    protected TextView inputSourceTV; // 输入的对照内容
    protected InputEditText inputET; // 输入框
    protected LinearLayout container; // 包含对照内容和输入框的容器

    protected DictionaryDBHelper dbHelper; // 数据库帮助类对象
    protected List<String> lines; // 所有输入内容的行内容集合
    protected List<InputView> inputViews = new ArrayList<>(); // 所有对照内容和输入框的集合
    protected InputView currentInputView; // 当前输入框

    protected int currentPos = 0; // 当前输入框在 #inputViews 中占的位置
    protected int maxPos = 0; // #inputViews 的大小
    protected int delTimes = 0; // 记录在当前输入框内容为空时，用户点击删除按钮的次数

    TextWatcher watcher = new TextWatcher() { // 监听输入框内容变化
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onCurChanged(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = DictionaryDBHelper.getInstance(this);

        initContent();
        initView();
    }

    private void initView() {

        container = (LinearLayout) findViewById(R.id.input_view_container);

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            InputView inputView = new InputView(BaseInputActivity.this);
            inputSourceTV = inputView.getInputSrcView();
            inputSourceTV.setText(line);
            inputET = inputView.getInputEditText();
            inputET.setEnabled(false);
            inputViews.add(inputView);
            container.addView(inputView);
        }

        if (inputViews.size() != 0) {
            currentInputView = inputViews.get(0);
            inputSourceTV = currentInputView.getInputSrcView();
            inputET = currentInputView.getInputEditText();
            inputET.addTextChangedListener(watcher);
            inputET.setEnabled(true);
            inputET.requestFocus();
        }

        this.maxPos = inputViews.size() - 1;

    }

    protected abstract void initContent();


    /**
     * 当有数据输入时调用，检查输入的数据是否正确，不正确的会红字显示
     *
     * @param data 输入的数据
     */
    protected void onCurChanged(String data) {

        if (!currentInputView.isParent(inputSourceTV)) {
            inputSourceTV = currentInputView.getInputSrcView();
        }

        String srcText = inputSourceTV.getText().toString();
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

        inputET.setTexts(texts);
        inputSourceTV.setText(spannableString);

        if (overData != null) {

            if (!overData.equals("")) {
                inputET.removeTextChangedListener(watcher);
                inputET.setText(data);
                goNextLine(overData);
            } else {
                goNextLine(overData);
            }

        }
    }

    /**
     * 当点击删除按钮会被调用
     */
    protected void onDelText() {
        if (debug) {
            Log.d(tag, "onDelText..");
        }

        if (inputET.getText().length() == 0) {
            delTimes++;
            if (shouldPreLine()) {
                goPreLine();
            }
        } else {
            resetDelTimes();
        }
    }

    /**
     * 判断是否要跳转到上一行，根据是当用户在当前输入框内容为空的时候，有再次触发删除操作
     *
     * @return 用户想要跳转到上一行，返回 true，反之，返回 false
     */
    private boolean shouldPreLine() {
        return delTimes >= 2;
    }

    /**
     * 当还有下一个输入行时，跳转到下一行
     *
     * @param overData 当前输入字符数超出部分，当有下一行时，会添加到下一行输入框
     */
    private void goNextLine(String overData) {
        if (haveNextLine()) {
            // 缓存当前输入框对象，这么做的原因是：若立即设置当前输入框为不可用时，软键盘会自动隐藏
            InputEditText tmp = inputET;

            // 获取下一个输入框作为当前输入框对象
            currentInputView = inputViews.get(++currentPos);
            inputSourceTV = currentInputView.getInputSrcView();
            inputET = currentInputView.getInputEditText();
            inputET.addTextChangedListener(watcher);
            inputET.setEnabled(true);
            inputET.requestFocus();

            // 设置上一个输入框对象不可用
            tmp.clearFocus();
            tmp.setEnabled(false);
            tmp.removeTextChangedListener(watcher);

            if (!TextUtils.isEmpty(overData)) {
                // 将超出的字符添加到当前输入框
                inputET.setText(overData);
                inputET.setSelection(overData.length());
                onCurChanged(overData);
            }

        } else {
            // 已经是最后一行了
            if (inputET.watcherSize == 0) {
                inputET.addTextChangedListener(watcher);
            }
            inputET.setSelection(inputET.getText().length());
        }
    }

    /**
     * 如果有上一行，跳转到上一行
     */
    private void goPreLine() {
        if (havePreLine()) {
            InputEditText tmp = inputET;

            currentInputView = inputViews.get(--currentPos);
            inputSourceTV = currentInputView.getInputSrcView();
            inputET = currentInputView.getInputEditText();
            inputET.addTextChangedListener(watcher);
            inputET.setEnabled(true);
            inputET.requestFocus();
            inputET.setSelection(inputET.getText().length());
            resetDelTimes();

            tmp.setEnabled(false);
            tmp.clearFocus();
            tmp.removeTextChangedListener(watcher);

        } else {
            resetDelTimes();
        }
    }

    /**
     * 是否还有上一行
     *
     * @return 若有上一行，返回 true，反之，返回 false
     */
    protected boolean havePreLine() {
        return currentPos > 0;
    }

    /**
     * 是否还有下一行
     *
     * @return 若有下一行，返回 true，反之，返回 false
     */
    protected boolean haveNextLine() {
        return currentPos < maxPos;
    }

    /**
     * 重置删除事件次数
     */
    private void resetDelTimes() {
        delTimes = 0;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        // 当点击删除按钮时，触发 onDelText() 方法
        if (event.getKeyCode() == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_UP) {
            onDelText();
        }
        return super.dispatchKeyEvent(event);
    }

}
