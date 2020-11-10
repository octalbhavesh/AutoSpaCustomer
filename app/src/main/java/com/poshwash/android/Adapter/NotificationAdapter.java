package com.poshwash.android.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.poshwash.android.Beans.NotificationModal;
import com.poshwash.android.Fragment.Notification;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.Util;

import java.text.ParseException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anand jain on 3/20/2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationModal> moviesList;
    DisplayImageOptions options;
    Context context;
    Notification notification;
    MyProgressDialog myProgressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CircleImageView circleimageview;
        public TextView txt_notificationtext, txt_time, userNameTv;
        public RelativeLayout row_bg;
        //  FrameLayout frameDelete;
        // SwipeRevealLayout swipeRevealLayout;

        public MyViewHolder(View view) {
            super(view);
            txt_notificationtext = (TextView) view.findViewById(R.id.txt_notificationtext);
            txt_time = (TextView) view.findViewById(R.id.txt_time);
            userNameTv = (TextView) view.findViewById(R.id.userNameTv);


        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_bg:
                    //Dynamic Data
//                    String booking_id = moviesList.get(getAdapterPosition()).getBooking_id();
//                    String Booking_driver_id = moviesList.get(getAdapterPosition()).getBooking_driver_id();
//                    if (moviesList.get(getAdapterPosition()).is_read().compareTo("0") == 0)
//                        getReadNoticafition(moviesList.get(getAdapterPosition()).getId(), booking_id, Booking_driver_id);
//                    else {
//                        if (booking_id != null && !booking_id.equalsIgnoreCase("null") && !booking_id.isEmpty())
//                            getBookingDetails(booking_id, Booking_driver_id);
//                    }
                    //Static Data


                    break;
               /* case R.id.frameDelete:
                    removeNotification(moviesList.get(getAdapterPosition()).getId());
                    break;*/
            }
        }
    }

    public NotificationAdapter(Context context, List<NotificationModal> moviesList) {
        this.moviesList = moviesList;
        this.notification = notification;
        this.context = context;
        myProgressDialog = new MyProgressDialog(this.context);
        myProgressDialog.setCancelable(false);

        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defalut_bg);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_child, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.userNameTv.setText(moviesList.get(position).getName());
        holder.txt_notificationtext.setText(moviesList.get(position).getMessage());
        try {
            holder.txt_time.setText(Util.calculateTimeDiffFromNow(moviesList.get(position).getDate(), context));
        } catch (ParseException e) {
            e.printStackTrace();
        }
       /* holder.txt_notificationtext.setText(moviesList.get(position).getText());
        try {
            holder.txt_time.setText(Util.calculateTimeDiffFromNow(moviesList.get(position).getTime(), context));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!moviesList.get(position).getPic().equals(""))
            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + moviesList.get(position).getPic(), holder.circleimageview, options);
        else
            holder.circleimageview.setImageResource(R.drawable.defalut_bg);*/


//Dynamic Data
//        holder.txt_notificationtext.setText(moviesList.get(position).getText());
//        holder.userNameTv.setText(moviesList.get(position).getUserName());
//        try {
//            holder.txt_time.setText(Util.calculateTimeDiffFromNow(moviesList.get(position).getTime(), context));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (!moviesList.get(position).getPic().equals(""))
//            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + moviesList.get(position).getPic(), holder.circleimageview, options);
//        else
//            holder.circleimageview.setImageResource(R.drawable.defalut_bg);
//        if (moviesList.get(position).is_read().compareTo("0") == 0)
//            holder.row_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.notificationback));
//        else
//            holder.row_bg.setBackgroundColor(ContextCompat.getColor(context, R.color.white));

    }

    @Override
    public int getItemCount() {
        return moviesList.size(); //Dynamic Data moviesList.size();
    }


}
