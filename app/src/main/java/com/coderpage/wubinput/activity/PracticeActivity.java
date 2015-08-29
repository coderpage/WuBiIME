package com.coderpage.wubinput.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.db.DictionaryDBHelper;
import com.coderpage.wubinput.model.Content;
import com.coderpage.wubinput.model.Word;

import java.util.List;

/**
 * Created by abner on 15-8-29.
 */
public class PracticeActivity extends Activity {

    private TextView remindTV;
    private TextView practiceSourceTV;
    private EditText practiceInputET;

    private DictionaryDBHelper dbHelper;
    private Content content = new Content();
    private List<Word> words = content.getWords();

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
        practiceInputET = (EditText) findViewById(R.id.et_practice_input);

        for (Word word : words) {
            practiceSourceTV.append(word.getWord() + "  ");
        }
    }

    private void initContent() {

//        dbHelper = DictionaryDBHelper.getInstance(PracticeActivity.this);
//        Cursor cursor = dbHelper.queryAll(TableDictionary.TABLE_NAME);
//        Random random = new Random(0);
//        for (int i = 0; i < 100; i++) {
//            for (; ; ) {
//                Word word = null;
//                int index = random.nextInt(cursor.getCount());
//                cursor.moveToPosition(index);
//                int valueCount = cursor.getInt(TableDictionary.COLUMN_VALUE_COUNT_INDEX);
//                for (int j = 0; j < valueCount; j++) {
//                    String value = cursor.getString(TableDictionary.COLUMN_VALUE_BASE_INDEX + j);
//                    if (value.length() == 1) {
//                        word = new Word();
//                        String key = cursor.getString(TableDictionary.COLUMN_KEY_INDEX);
//                        word.setCode(key);
//                        word.setWord(value);
//                        content.addWord(word);
//                        break;
//                    }
//
//                }
//                if (word != null) {
//                    break;
//                }
//            }
//        }
//
//        cursor.close();
    }

}
