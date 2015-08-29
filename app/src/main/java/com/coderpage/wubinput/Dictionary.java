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
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Dictionary {
    private boolean isAvailable = false;
    private static final String LOG_TAG = "Dictionary";
    private static Dictionary mInstance = null;
    private Context main;
    private Map<String, ArrayList<String>> mDict;

    private static final int BUFFER_SIZE = 8192;

    public static Dictionary getInstance(Context context) {
        if (mInstance == null) mInstance = new Dictionary(context);
        return mInstance;
    }

    public ArrayList<String> getCandidates(String input) {
        if (input.length() == 0) return null;
        while (!isAvailable) ;
        ArrayList<String> candidates = readDict(input);
        if (!candidates.isEmpty() || input.length() > 3) return candidates;

        StringBuilder code = new StringBuilder(input);
        while (candidates.isEmpty() && code.length() < 5) {
            code = code.append(code.charAt(code.length() - 1));
            candidates = readDict(code);
        }
        return candidates;
    }

    private Dictionary(Context context) {
        main = context;
        new ReadDictionary().execute();
    }

    private ArrayList<String> readDict(CharSequence code) {
        ArrayList<String> candidates = mDict.get(code.toString());
        if (candidates == null) {
            candidates = new ArrayList<>();
        }
        return candidates;
    }

    private Map<String, ArrayList<String>> readDictFromResource() {
        Map<String, ArrayList<String>> dict = new HashMap<>(262144);
        BufferedReader bufferedResourceReader = null;

        try {
            long start = System.currentTimeMillis();
            InputStream resourceFileInputStream = main.getResources().openRawResource(R.raw.wubi);
            InputStreamReader resourceReader = new InputStreamReader(resourceFileInputStream, "utf-8");
            bufferedResourceReader = new BufferedReader(resourceReader, BUFFER_SIZE);

            String line;
            while ((line = bufferedResourceReader.readLine()) != null) {
                String[] words = line.split(" ");
                if (dict.containsKey(words[0])) {
                    for (int i = 1; i < words.length; i++)
                        dict.get(words[0]).add(words[i]);
                } else {
                    ArrayList<String> list = new ArrayList<>();
                    for (int i = 1; i < words.length; i++)
                        list.add(words[i]);
                    dict.put(words[0], list);
                }
            }
            Log.d(LOG_TAG, "read from resource file success, " + String.valueOf(System.currentTimeMillis() - start));
        } catch (Exception e) {
            Log.d(LOG_TAG, "read from resource file failed");
            return null;
        } finally {
            try {
                if (bufferedResourceReader != null) bufferedResourceReader.close();
            } catch (Exception e) {
            }
        }
        return dict;
    }

    private class ReadDictionary extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            mDict = readDictFromResource();
            isAvailable = true;
            return null;
        }
    }

    public Map<String, ArrayList<String>> getmDict() {
        return mDict;
    }
}
