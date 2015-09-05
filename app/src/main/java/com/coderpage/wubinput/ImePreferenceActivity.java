package com.coderpage.wubinput;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.inputmethod.InputMethodManager;

public class ImePreferenceActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
//        String IMEName = "com.coderpage.wubinput/.IME";
//        Settings.Secure.putString( getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD, IMEName );
    }
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference preference){
        if(preference.getTitle().equals(getResources().getString(R.string.prefs_pick_ime))){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showInputMethodPicker();
        }
        return super.onPreferenceTreeClick(screen, preference);
    }
}
