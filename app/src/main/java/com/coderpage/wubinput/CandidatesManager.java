package com.coderpage.wubinput;

import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
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

    private CandidateViewListener mListener;
    private View mCandidatesView;
    private ViewGroup mContainer;

    private CandidatesManager(InputMethodService ims) {
        main = ims;
        mCandidatesView = main.getLayoutInflater().inflate(R.layout.candidates, null);
        mContainer = (ViewGroup) mCandidatesView.findViewById(R.id.candidates_container);
    }

    public static CandidatesManager getInstance(InputMethodService ims) {
        mInstance = new CandidatesManager(ims);
        return mInstance;
    }

    public static CandidatesManager getInstance() {
        return mInstance;
    }

    public View getCandidatesView() {
        return mCandidatesView;
    }

    public int hasCandidate() {
        return mContainer.getChildCount();
    }

    public void setCandidateViewListener(CandidateViewListener listener) {
        this.mListener = listener;
    }

    public void setCandidates(ArrayList<String> words) {
        mContainer.removeAllViews();
        if (words == null) return;

        int index = 1;
        for (String word : words) {
            LinearLayout candidate = (LinearLayout) main.getLayoutInflater().inflate(R.layout.word, mContainer, false);
            ((TextView) candidate.findViewById(R.id.word_num)).setText(String.valueOf(index));
            ((TextView) candidate.findViewById(R.id.word)).setText(word);
            mContainer.addView(candidate);
            ++index;
        }
    }

    public boolean pickCandidate(int index) {
        if (index > hasCandidate()) {
            return false;
        }
        TextView wordView = (TextView) mContainer.getChildAt(index - 1).findViewById(R.id.word);
        mListener.onPickCandidate(wordView.getText().toString());
        return true;
    }

    public void onClick(View view) {
        mListener.onPickCandidate(((TextView) view.findViewById(R.id.word)).getText().toString());
    }

    public static interface CandidateViewListener {
        void onPickCandidate(String candidate);
    }

    public void onDestroy() {
        mInstance = null;
    }
}
