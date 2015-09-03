package com.coderpage.wubinput.view.activity;

import android.app.ActionBar;
import android.os.Bundle;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.model.Wubi;
import com.coderpage.wubinput.view.widget.InputView;

import java.util.List;

/**
 * @author abner-l
 * @since 2015-08-31
 */
public class PracticeInputActivity extends BaseInputActivity {

    private TextView remindTV; // 五笔编码提示框

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_practice);
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
