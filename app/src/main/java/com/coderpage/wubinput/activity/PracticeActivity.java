package com.coderpage.wubinput.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.coderpage.wubinput.R;

/**
 * Created by abner on 15-8-29.
 */
public class PracticeActivity extends Activity {

    private TextView remindTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        initView();
    }

    private void initView(){
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_practice_view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        remindTV = (TextView)actionBar.getCustomView().findViewById(R.id.tv_remind);
    }


}
