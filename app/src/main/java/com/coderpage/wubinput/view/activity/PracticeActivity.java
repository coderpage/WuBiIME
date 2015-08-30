package com.coderpage.wubinput.view.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.db.DictionaryDBHelper;
import com.coderpage.wubinput.model.Content;
import com.coderpage.wubinput.view.widget.ColorText;
import com.coderpage.wubinput.view.widget.InputEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abner on 15-8-29.
 */
public class PracticeActivity extends Activity {

    private String tag = PracticeActivity.class.getSimpleName();
    private boolean debug = true;

    private TextView remindTV;
    private TextView practiceSourceTV;
    private InputEditText practiceInputET;

    private DictionaryDBHelper dbHelper;
    private Content content = new Content();
    private List<String> words;

    private int currentPos = 0;

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
                Log.d(tag, "onTextChanged:" + s.toString());
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
        practiceSourceTV = (TextView) findViewById(R.id.tv_practice_src);
        practiceInputET = (InputEditText) findViewById(R.id.et_practice_input);
        practiceInputET.addTextChangedListener(watcher);

        for (String word : words) {
            practiceSourceTV.append(word);
        }
    }

    private void initContent() {
        dbHelper = DictionaryDBHelper.getInstance(PracticeActivity.this);
        words = dbHelper.querySingleLevel1();
    }

    private void onCurChanged(String data) {

        String str = practiceSourceTV.getText().toString();
        SpannableString spannableString = new SpannableString(str);

        List<ColorText> texts = new ArrayList<>();

        for (int i = 0; i < data.length(); i++) {
            ColorText text = new ColorText();

            if (str.charAt(i) != data.charAt(i)) {
                text.color = Color.RED;
                text.text = data.charAt(i);
                texts.add(text);
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), i, i + 1, Spanned.SPAN_POINT_MARK);
            }else {
                text.text = data.charAt(i);
                texts.add(text);
            }

        }

        practiceInputET.setTexts(texts);
        practiceSourceTV.setText(spannableString);
    }

}
