package com.poshwash.android.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Activity.UpcomingBookingDetail;
import com.poshwash.android.Beans.MyBookingChildModal;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.Fragment.Notification;
import com.poshwash.android.Fragment.UpcomingBookings;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.customViews.CustomTexViewRegular;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anand jain on 3/20/2017.
 */

public class UpcomingBookingAdapter extends RecyclerView.Adapter<UpcomingBookingAdapter.MyViewHolder> {

    private List<MyBookingChildModal> moviesList;
    DisplayImageOptions options;
    Context context;
    MyProgressDialog myProgressDialog;
    UpcomingBookings upcomingBookings;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView circleimageview;
        private CustomTexViewRegular userNameTv;
        private CustomTexViewRegular txtdate;
        private CustomTexViewRegular txtStatus;
        private CustomTexViewRegular txtPrice;
        private CustomTexViewRegular txtLocation;
        private RelativeLayout row_bg;
        FrameLayout frameDelete;
        SwipeRevealLayout swipeRevealLayout;

        public MyViewHolder(View view) {
            super(view);
            circleimageview = (ImageView) itemView.findViewById(R.id.circleimageview);
            userNameTv = (CustomTexViewRegular) itemView.findViewById(R.id.userNameTv);
            txtdate = (CustomTexViewRegular) itemView.findViewById(R.id.txt_date);
            txtStatus = (CustomTexViewRegular) itemView.findViewById(R.id.txt_status);
            txtPrice = (CustomTexViewRegular) itemView.findViewById(R.id.txt_price);
            txtLocation = (CustomTexViewRegular) itemView.findViewById(R.id.txt_location);
            row_bg = (RelativeLayout) itemView.findViewById(R.id.row_bg);
            frameDelete = (FrameLayout) view.findViewById(R.id.frameDelete);
            swipeRevealLayout = (SwipeRevealLayout) view.findViewById(R.id.swipeRefresh);

            row_bg.setOnClickListener(this);
            frameDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_bg:
                    String booking_id = moviesList.get(getAdapterPosition()).getId();

                    getBookingDetails(moviesList.get(getAdapterPosition()).getVehicleDetail());

                    break;
                case R.id.frameDelete:
                    swipeRevealLayout.close(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle(context.getString(R.string.are_you_sure_you_want_to_delete));
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeBooking(moviesList.get(getAdapterPosition()).getId());
                            dialog.dismiss();

                        }
                    }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    Dialog d = builder.show();
                    int textViewId = d.getContext().getResources().getIdentifier("android:id/alertTitle", null, null);
                    int btn = d.getContext().getResources().getIdentifier("android:id/button1", null, null);
                    TextView tv = (TextView) d.findViewById(textViewId);
                    Button nbutton1 = (Button) d.findViewById(btn);
                    tv.setTextColor(ContextCompat.getColor(context, R.color.skybluecolor));
                    nbutton1.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
                    int btn1 = d.getContext().getResources().getIdentifier("android:id/button2", null, null);
                    Button nbutton2 = (Button) d.findViewById(btn1);
                    nbutton2.setTextColor(ContextCompat.getColor(context, R.color.graycolor));
                    break;

            }
        }
    }

    public UpcomingBookingAdapter(Context context, List<MyBookingChildModal> moviesList, UpcomingBookings upcomingBookings) {
        this.moviesList = moviesList;
        this.context = context;
        this.upcomingBookings = upcomingBookings;
        this.myProgressDialog = new MyProgressDialog(context);

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
                .inflate(R.layout.upcoming_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.userNameTv.setText(moviesList.get(position).getCar_name());
        holder.txtLocation.setText(moviesList.get(position).getBooking_long());

        try {
            holder.txtdate.setText(Util.convertUtcTimeToLocal(moviesList.get(position).getBooking_date() + " " + moviesList.get(position).getBooking_time(), true, context));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txtPrice.setText("" + moviesList.get(position).getPrice());
        holder.txtStatus.setText(context.getString(R.string.pending));
        holder.txtStatus.setTextColor(ContextCompat.getColor(context, R.color.pending_color));

        if (!moviesList.get(position).getImage().equals(""))

            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + moviesList.get(position).getImage(), holder.circleimageview, options);
        else
            holder.circleimageview.setImageResource(R.drawable.aboutusbanner);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    private void getBookingDetails(JSONObject myBookingChildModal) {
        Intent intent = new Intent(context, UpcomingBookingDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString("myBookingChildModal", myBookingChildModal.toString());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void removeBooking(String booking_id) {
        myProgressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("type", "1");
            jsonObject.put("language", MySharedPreferences.getLanguage(context));


            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.booking_remove(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Util.dismissPrograssDialog(myProgressDialog);
                        JSONObject mjJsonObject = new JSONObject(new String(response.body().bytes()));
                        if (mjJsonObject.getJSONObject("response").getBoolean("status")) {
                            upcomingBookings.callWebServiceShowUpcomingBookings();
                        } else
                            Util.Toast(context, mjJsonObject.getJSONObject("response").getString("message"));

                    } catch (Exception e) {
                        Log.e(Notification.TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(myProgressDialog);
                }
            });
        } catch (Exception e) {
            Log.e(Notification.TAG, e.toString());
        }
    }
}
