package com.poshwash.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Activity.BookingDetail;
import com.poshwash.android.Activity.CancelActivity;
import com.poshwash.android.Beans.PastBookingModel;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.Fragment.Notification;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.Util;

import java.util.ArrayList;

/**
 * Created by anand jain on 3/20/2017.
 */

public class MyNewBookingAdapter extends RecyclerView.Adapter<MyNewBookingAdapter.MyViewHolder> {

    private ArrayList<PastBookingModel> moviesList;
    DisplayImageOptions options;
    Context context;
    Notification notification;
    MyProgressDialog myProgressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView circleimageview, iv_cancel;
        public TextView tvCarName, tvDate, tvUserName, tvStatus, tvAmount, tvDetail;
        public RelativeLayout row_bg;

        public MyViewHolder(View view) {
            super(view);
            iv_cancel = (ImageView) view.findViewById(R.id.iv_cancel);
            tvDetail = (TextView) view.findViewById(R.id.viewDeatilTv);
            tvCarName = (TextView) view.findViewById(R.id.txt_carname);
            tvUserName = (TextView) view.findViewById(R.id.txt_username);
            tvAmount = (TextView) view.findViewById(R.id.txt_price);
            tvDate = (TextView) view.findViewById(R.id.txt_time);
            tvStatus = (TextView) view.findViewById(R.id.status_tv);
            circleimageview = (ImageView) view.findViewById(R.id.img_car);

        }


    }

    public MyNewBookingAdapter(Context context, ArrayList<PastBookingModel> moviesList) {
        this.moviesList = moviesList;

        this.context = context;

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
                .inflate(R.layout.adapter_past_booking_new, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvCarName.setText(moviesList.get(position).getCar_name());
        holder.tvUserName.setText("Plate Number: " + moviesList.get(position).getPlate_number());
        String[] date = moviesList.get(position).getDate().split("-");
        holder.tvDate.setText(date[1] + "-" + date[2] + "-" + date[0]);


        if (moviesList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.tvStatus.setText("Initiated");
            holder.iv_cancel.setVisibility(View.VISIBLE);
            holder.tvDetail.setVisibility(View.GONE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setText("Scheduled");
            holder.tvDetail.setVisibility(View.VISIBLE);
            holder.iv_cancel.setVisibility(View.GONE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.tvStatus.setText("Completed");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.tvStatus.setText("Cancelled");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("4")) {
            holder.tvStatus.setText("Refunded");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("5")) {
            holder.tvStatus.setText("Start wash");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("6")) {
            holder.tvStatus.setText("Cancel By Service Provider");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("7")) {
            holder.tvStatus.setText("Cancel By Admin");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("8")) {
            holder.tvStatus.setText("Cancel By Customer");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        } else if (moviesList.get(position).getStatus().equalsIgnoreCase("9")) {
            holder.tvStatus.setText("Reached");
            holder.iv_cancel.setVisibility(View.GONE);
            holder.tvDetail.setVisibility(View.VISIBLE);
        }

        if (!moviesList.get(position).getImg().equals(""))
            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL
                    + moviesList.get(position).getImg(), holder.circleimageview, options);
        else
            holder.circleimageview.setImageResource(R.drawable.defalut_bg);


        holder.iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, CancelActivity.class);
                i.putExtra("booking_id", moviesList.get(position).getBooking_id());
                i.putExtra("driver_id", moviesList.get(position).getDriver_id());
                context.startActivity(i);
            }
        });
        holder.tvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (moviesList.get(position).getStatus().equalsIgnoreCase("0")) {
                    Util.warningToast(context, "Please try later");
                } else {
                    Intent i = new Intent(context, BookingDetail.class);
                    i.putExtra("booking_id", moviesList.get(position).getBooking_id());
                    i.putExtra("driver_id", moviesList.get(position).getDriver_id());
                    context.startActivity(i);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size(); //Dynamic Data moviesList.size();
    }


}
