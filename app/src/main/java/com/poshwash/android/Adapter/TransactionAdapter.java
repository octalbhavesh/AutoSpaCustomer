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
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.poshwash.android.Activity.BookingDetail;
import com.poshwash.android.Beans.TransactionModel;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.customViews.CustomTexViewRegular;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by anand jain on 3/20/2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {

    private List<TransactionModel> moviesList;
    DisplayImageOptions options;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        private RelativeLayout rowBg;
        private CircleImageView circleimageview;
        private CustomTexViewRegular userNameTv;
        private CustomTexViewRegular txtTransactionId;
        private CustomTexViewRegular txtTime;
        private CustomTexViewRegular txtPrice;
        private CustomTexViewRegular txtPaymentType;


        public MyViewHolder(View view) {
            super(view);
            rowBg = (RelativeLayout) itemView.findViewById(R.id.row_bg);
            circleimageview = (CircleImageView) itemView.findViewById(R.id.circleimageview);
            userNameTv = (CustomTexViewRegular) itemView.findViewById(R.id.userNameTv);
            txtTransactionId = (CustomTexViewRegular) itemView.findViewById(R.id.txt_transactionId);
            txtTime = (CustomTexViewRegular) itemView.findViewById(R.id.txt_time);
            txtPrice = (CustomTexViewRegular) itemView.findViewById(R.id.txt_price);
            txtPaymentType = (CustomTexViewRegular) itemView.findViewById(R.id.txt_paymentType);


        }


    }

    public TransactionAdapter(Context context, ArrayList<TransactionModel> moviesList) {
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
                .inflate(R.layout.transaction_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        if (moviesList.get(position).getPayment_status().equalsIgnoreCase("1")) {
            holder.userNameTv.setText("Credit to wallet");
        } else {
            holder.userNameTv.setText("Debit from wallet");
        }

        holder.txtTransactionId.setText(moviesList.get(position).getTransaction_id());
        try {
            holder.txtTime.setText(Util.calculateTimeDiffFromNow(moviesList.get(position).getDate(), context));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (moviesList.get(position).getPayment_status().equalsIgnoreCase("1")) {
            holder.txtPrice.setText("+"+"$" + moviesList.get(position).getAmount());
        } else {
            holder.txtPrice.setText("-"+"$" + moviesList.get(position).getAmount());
        }


       /* if (moviesList.get(position).get.compareTo("1") == 0) {
            holder.txtPaymentType.setText(context.getString(R.string.cash));
        } else {
            holder.txtPaymentType.setText(context.getString(R.string.span_payment));
        }*/

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    private void getBookingDetails(String booking_id, String Booking_driver_id) {

        Intent intent = new Intent(context, BookingDetail.class);
        intent.putExtra("bookingId", "");//Dynamic Data booking_id
        intent.putExtra("bookingDriverId", ""); //Dynamic Data Booking_driver_id
        context.startActivity(intent);
    }
}
