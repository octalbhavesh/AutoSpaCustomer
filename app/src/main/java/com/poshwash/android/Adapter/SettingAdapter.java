package com.poshwash.android.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;

import java.util.List;

/**
 * Created by anandj on 3/17/2017.
 */

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.MyViewHolder> {

    private List<String> moviesList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout parent_rl;
        public ImageView iv_arrow;

        public MyViewHolder(View view) {
            super(view);
            iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
            title = (TextView) view.findViewById(R.id.txt_settingname);
            parent_rl = (RelativeLayout) view.findViewById(R.id.parent_rl);
        }
    }

    public SettingAdapter(Context context, List<String> moviesList) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.setting_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (MySharedPreferences.getLanguage(context).equalsIgnoreCase("eng")) {
            holder.iv_arrow.setRotation(0);
        } else {
            holder.iv_arrow.setRotation(180);
        }
        holder.title.setText(moviesList.get(position));
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}