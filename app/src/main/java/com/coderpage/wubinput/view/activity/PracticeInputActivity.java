package com.coderpage.wubinput.view.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.db.DictionaryDBHelper;
import com.coderpage.wubinput.model.Content;
import com.coderpage.wubinput.model.Wubi;
import com.coderpage.wubinput.view.widget.InputView;

import java.util.List;

/**
 * @author abner-l
 * @since 2015-08-31
 */
public class PracticeInputActivity extends BaseInputActivity {

//    /* 通过 bundle 对象传递过来的 bundle 名称 */
//    public static final String BUNDLE_NAME = "metadata";
    /* bundle 数据中模式的 key 名称，模式有：单字练习、词组练习、文章练习 */
    public static final String BUNDLE_KEY_MODE = "mode";
    /* bundle 数据中用户选择练习的篇数，比如：第几篇文章，选择单字练习的难易程度 */
    public static final String BUNDLE_KEY_SELECT = "select";

    private int mode;
    private int select;

    private TextView remindTV; // 五笔编码提示框

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_practice);

        Bundle bundle = getIntent().getExtras();
        mode = bundle.getInt(BUNDLE_KEY_MODE);
        select = bundle.getInt(BUNDLE_KEY_SELECT);

        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_practice_view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        remindTV = (TextView) findViewById(R.id.tv_remind);
        if (inputSourceTV != null) {
            CharSequence inputSrcData = inputSourceTV.getText();
            if (inputSrcData.length() > 0) {
                String word = String.valueOf(inputSrcData.charAt(0));
                remindTV.setText(getCode(word));
            }
        }

    }

    @Override
    protected void initContent() {
        dbHelper = DictionaryDBHelper.getInstance(PracticeInputActivity.this);
        String data = null;
        switch (mode) {
            case Wubi.TypingMode.PRACTICE_SINGLE_WORD:
                data = getSingleWords();
                break;
            case Wubi.TypingMode.PRACTICE_PHRASH:
                data = getPhrase();
                break;
            case Wubi.TypingMode.PRACTICE_ARTICLE:
                data = dbHelper.queryArticle(select);
                break;
            default:
                throw new IllegalStateException("不识别此模式：" + mode);
        }


        if (inputSourceTV == null) {
            inputSourceTV = (TextView) View.inflate(PracticeInputActivity.this, R.layout.input_src_view, null);
        }

        Content content = new Content(data, inputSourceTV);
        this.lines = content.getLines();
    }

    private String getSingleWords() {
        List<String> words;
        switch (select) {
            case Wubi.SingleLevelType.LEVEL_1:
                words = dbHelper.querySingleLevel1();
                return generateSingleData(words);

            case Wubi.SingleLevelType.LEVEL_2:
                words = dbHelper.querySingleLevel2(100);
                return generateSingleData(words);

            case Wubi.SingleLevelType.LEVEL_3:
                words = dbHelper.querySingleLevel3(100);
                return generateSingleData(words);

            case Wubi.SingleLevelType.RANDOW:
                words = dbHelper.querySingles(100);
                return generateSingleData(words);

            default:
                throw new IllegalStateException("不识别此类别：" + select);
        }
    }

    private String generateSingleData(List<String> words) {
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word);
        }

        return builder.toString();
    }

    private String getPhrase() {
        List<String> phrases = dbHelper.queryPhrase(100);
        return generatePhraseData(phrases);
    }

    private String generatePhraseData(List<String> phrases) {
        StringBuilder builder = new StringBuilder();
        for (String phrase : phrases) {
            builder.append(phrase);
        }

        return builder.toString();
    }

    @Override
    protected void onCurChanged(String data) {
        super.onCurChanged(data);

        updateRemindView();
    }

    /**
     * 更新编码提示
     */
    private void updateRemindView() {
        // 获取当前 inputEditText 光标位置
        int selectionIndex = inputET.getSelectionStart();
        // 获取对照内容
        CharSequence srcData = inputSourceTV.getText();

        while (selectionIndex < srcData.length()) {
            char currentChar = srcData.charAt(selectionIndex);
            if (!isHanZi(currentChar)) {
                selectionIndex++;
                continue;
            }
            String nextWord = String.valueOf(srcData.charAt(selectionIndex));
            remindTV.setText(getCode(nextWord));
            return;
        }

        if (haveNextLine()) {
            InputView inputView = inputViews.get(currentPos + 1);
            srcData = inputView.getInputSrcView().getText();
            selectionIndex = 0;
        } else {
            return;
        }

        while (selectionIndex < srcData.length()) {
            char currentChar = srcData.charAt(selectionIndex);
            if (!isHanZi(currentChar)) {
                selectionIndex++;
                continue;
            }
            String nextWord = String.valueOf(srcData.charAt(selectionIndex));
            remindTV.setText(getCode(nextWord));
            return;
        }

    }

    /**
     * 查询字符的五笔编码
     *
     * @param word 需要查询的字符
     * @return 字符的五笔编码
     */
    private String getCode(String word) {
        StringBuilder builder = new StringBuilder();
        builder.append(word).append('(');
        List<String> codes = dbHelper.queryCode(word, Wubi.TYPE_86);
        for (int i = 0; i < codes.size(); i++) {
            builder.append(codes.get(i));
            if (i < codes.size() - 1) {
                builder.append(',');
            }
        }
        builder.append(')');
        return builder.toString();
    }

    /**
     * 判断是否是汉字
     *
     * @param c 需判断的字符
     * @return 若是汉字，返回 true，反之，返回 false
     */
    private boolean isHanZi(char c) {

        return c >= 0x4e00 && c < 0x9fff;
    }


}
