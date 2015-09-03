package com.coderpage.wubinput.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coderpage.wubinput.R;

/**
 * @author abner-l
 * @since 2015-09-03
 */
public class HomeFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.fragment_home, container, false);



        return contentView;
    }

}
