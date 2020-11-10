package com.poshwash.android.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poshwash.android.R;


/**
 * Created by abhinava on 1/12/2017.
 */

public class WalkThroughFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";
    View rootview;
    Context mContext;
    TextView textView;
    RelativeLayout layout;

    @SuppressLint("ValidFragment")
    public static WalkThroughFragment newInstance(int page) {
        WalkThroughFragment fragment = new WalkThroughFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, page);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.walkthroughfragment, container, false);
        mContext = getActivity();
        finViewbyID();

        Bundle bundle = getArguments();
        try {
            if (bundle != null) {
                int page = bundle.getInt(ARG_PAGE_NUMBER);
                if (page == 0) {
                    layout.setBackgroundResource(R.drawable.walk1);
                }
                if (page == 1) {
                    layout.setBackgroundResource(R.drawable.walk2);
                }
                if (page == 2) {
                    layout.setBackgroundResource(R.drawable.walk3);
                }
            } else {
                layout.setBackgroundResource(R.drawable.walk1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootview;
    }

    private void finViewbyID() {
        textView = (TextView) rootview.findViewById(R.id.text);
        layout = (RelativeLayout) rootview.findViewById(R.id.layout);
    }
}
