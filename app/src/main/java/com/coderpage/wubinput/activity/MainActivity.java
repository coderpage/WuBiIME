package com.coderpage.wubinput.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.coderpage.wubinput.R;
import com.coderpage.wubinput.TestActivity;

/**
 *
 * @author abner
 * @since 2015-08-29
 */
public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void enableIME(View view) {
        Intent intent = new Intent("android.settings.INPUT_METHOD_SETTINGS");
        startActivity(intent);
    }

    public void pickIME(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showInputMethodPicker();
    }

    public void test(View view) {
        Intent intent = new Intent(MainActivity.this, TestActivity.class);
        startActivity(intent);
    }

    public void practice(View view) {
        Intent intent = new Intent(MainActivity.this, PracticeActivity.class);
        startActivity(intent);
    }

}
