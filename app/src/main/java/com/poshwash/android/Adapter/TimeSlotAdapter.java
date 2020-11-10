package com.poshwash.android.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poshwash.android.Beans.BeanTimeSlot;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Util;

import java.util.ArrayList;

/**
 * Created by anandj on 3/17/2017.
 */

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {

    private ArrayList<BeanTimeSlot> moviesList;
    Context context;
    OnTimeSlotSelect onTimeSlotSelect;
    String date = "";

    public interface OnTimeSlotSelect {
        void timeSlotSelect(int slot);
    }

    public void setOnTimeSlotSelect(OnTimeSlotSelect onTimeSlotSelect) {
        this.onTimeSlotSelect = onTimeSlotSelect;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RelativeLayout parent_rl;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_settingname);
            parent_rl = (RelativeLayout) view.findViewById(R.id.parent_rl);
        }
    }

    public TimeSlotAdapter(Context context, ArrayList<BeanTimeSlot> moviesList, String date) {
        this.moviesList = moviesList;
        this.context = context;
        this.date = date;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeslot_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final String timeslot = moviesList.get(position).getTime_slot();

        holder.title.setText(Util.Time24to12(timeslot));

        if (Util.CheckDate(date + " " + (timeslot).split(" - ")[0]) || moviesList.get(position).getAvailStatus().compareToIgnoreCase("0") == 0) {
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.lightdarkgraycolor));
        } else {
            holder.title.setTextColor(ContextCompat.getColor(context, R.color.graycolor));
        }

        holder.parent_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.CheckDate(date + " " + (timeslot).split(" - ")[0]) && moviesList.get(position).getAvailStatus().compareToIgnoreCase("0") != 0) {
                    onTimeSlotSelect.timeSlotSelect(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}