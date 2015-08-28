/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.coderpage.wubinput;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.provider.Settings;
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
