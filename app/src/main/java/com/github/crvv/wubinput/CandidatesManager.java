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

package com.github.crvv.wubinput;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Contains all candidates in pages where users could move forward (next page)
 * or move backward (previous) page to select one of these candidates.
 */
public class CandidatesManager {
    private static CandidatesManager mInstance = null;

    private InputMethodService main;

    private ArrayList<String> words;
    private CandidateViewListener mListener;
    private View mCandidatesView;
    private GridLayout mGridLayout;

    private CandidatesManager(InputMethodService ims) {
        main = ims;
        mCandidatesView = main.getLayoutInflater().inflate(R.layout.candidates, null);
        mGridLayout = (GridLayout)mCandidatesView.findViewById(R.id.candidates_container);
//        mCandidatesView.findViewById(R.id.horizontalScrollView).setLayoutParams(new ViewGroup.LayoutParams(main.getMaxWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
    }
    public static CandidatesManager getInstance(InputMethodService ims){
        if(mInstance == null)mInstance = new CandidatesManager(ims);
        return mInstance;
    }
    public View getCandidatesView(){
        return mCandidatesView;
    }
    public void setCandidateViewListener(CandidateViewListener listener) {
        this.mListener = listener;
    }

    //TODO
    public void setCandidates(ArrayList<String> words) {
        mGridLayout.removeAllViews();
        if(words == null)return;

        this.words = words;
        int index = 1;
        for(String word : words){
            LinearLayout candidate = (LinearLayout) main.getLayoutInflater().inflate(R.layout.word, mGridLayout, false);
            ((TextView) candidate.findViewById(R.id.word_num)).setText(String.valueOf(index));
            ((TextView) candidate.findViewById(R.id.word)).setText(word);
            mGridLayout.addView(candidate);
            ++index;
        }
    }

    public boolean pickHighlighted() {
        if(mGridLayout.getChildCount() == 0)return false;
        TextView wordView = (TextView)mGridLayout.getChildAt(0).findViewById(R.id.word);
        mListener.onPickCandidate(wordView.getText().toString());
        return true;
    }

    public void onClick(View view){
        mListener.onPickCandidate(((TextView)view.findViewById(R.id.word)).getText().toString());
    }
    public static interface CandidateViewListener {
        void onPickCandidate(String candidate);
    }
}
