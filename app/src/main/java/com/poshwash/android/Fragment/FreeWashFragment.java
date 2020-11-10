package com.poshwash.android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;


public class FreeWashFragment extends Fragment {
    private Context context;
    private TextView tvRefer;
    private Button btnShare;

    public static FreeWashFragment newInstance(Context context) {
        FreeWashFragment f = new FreeWashFragment();
        context = context;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_free_wash, container, false);
        tvRefer = rootView.findViewById(R.id.tv_refer);
        btnShare = rootView.findViewById(R.id.btn_share);
        tvRefer.setText(MySharedPreferences.getReferCode(getActivity()));


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String referCode = getResources().getString(R.string.share_message) +
                        getResources().getString(R.string.use_code) + MySharedPreferences.getReferCode(getActivity());
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, referCode);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Promo Code");
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share) + "..."));
            }
        });
        return rootView;
    }


}
