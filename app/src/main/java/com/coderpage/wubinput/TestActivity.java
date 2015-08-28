package com.coderpage.wubinput;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import java.lang.annotation.Target;


public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        setActionBar();
    }

    @TargetApi(11)
    private void setActionBar(){
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

}
