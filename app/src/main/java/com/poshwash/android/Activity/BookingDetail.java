package com.poshwash.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.BookingDetailBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetail extends BaseActivity {
    private BookingDetailBinding mBinding;

    DisplayImageOptions options;
    private ImageView backIcon;
    private String bookingId, driverId;
    private String mStatus;
    private String driverPhone, driverImage;
    private String sourceLat, sourceLong, destLat, destLong;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.booking_detail);
        bookingId = getIntent().getExtras().getString("booking_id");
        driverId = getIntent().getExtras().getString("driver_id");
        /*Bitmap default_bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defalut_bg);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(mContext.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(mContext.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(mContext.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();*/
        initView();
    }

    private void initView() {

        mBinding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mBinding.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(BookingDetail.this, TrackingActivity.class);
                i.putExtra("driver_name", mBinding.detailDriverName.getText().toString().trim());
                i.putExtra("driver_phone", driverPhone);
                i.putExtra("driver_image", driverImage);
                i.putExtra("source_lat", sourceLat);
                i.putExtra("source_long", sourceLong);
                i.putExtra("dest_lat", destLat);
                i.putExtra("dest_long", destLong);
                i.putExtra("driver_id", driverId);
                startActivity(i);
            }
        });

        if (Util.isConnectingToInternet(mContext))
            try {
                callApiNotification(CountrySelectListener.APIRequest.
                        booking_detail(MySharedPreferences.getUserId(mContext), driverId, bookingId,
                                MySharedPreferences.getLanguage(mContext)));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            Util.errorToast(mContext, mContext.getString(R.string.no_internet_connection));
    }

    private void callApiNotification(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.booking_detail(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    String messsage = jsonObject1.optString("message");
                    boolean status1 = jsonObject1.optBoolean("status");

                    if (status1) {
                        JSONObject jsonObject2 = jsonObject1.optJSONObject("data");
                        mBinding.detailDriverName.setText(jsonObject2.optString("first_name") + " " + jsonObject2.optString("last_name"));
                        mBinding.detailLocation.setText(jsonObject2.optString("booking_address"));

                        sourceLat = jsonObject2.optString("sp_lat");
                        sourceLong = jsonObject2.optString("sp_long");
                        destLat = jsonObject2.optString("user_lat");
                        destLong = jsonObject2.optString("user_long");
                        driverPhone = jsonObject2.optString("mobile");
                        mStatus = jsonObject2.optString("status");
                        if (mStatus.equalsIgnoreCase("0")) {
                            mBinding.detailStatus.setText("Initiated");
                        } else if (mStatus.equalsIgnoreCase("1")) {
                            mBinding.detailStatus.setText("Scheduled");
                        } else if (mStatus.equalsIgnoreCase("2")) {
                            mBinding.detailStatus.setText("Completed");
                        } else if (mStatus.equalsIgnoreCase("3")) {
                            mBinding.detailStatus.setText("Cancelled");
                        } else if (mStatus.equalsIgnoreCase("4")) {
                            mBinding.detailStatus.setText("Refunded");
                        } else if (mStatus.equalsIgnoreCase("5")) {
                            mBinding.detailStatus.setText("Start wash");
                        } else if (mStatus.equalsIgnoreCase("6")) {
                            mBinding.detailStatus.setText("Cancel By Service Provider");
                        } else if (mStatus.equalsIgnoreCase("7")) {
                            mBinding.detailStatus.setText("Cancel By Admin");
                        } else if (mStatus.equalsIgnoreCase("8")) {
                            mBinding.detailStatus.setText("Cancel By Customer");
                        } else if (mStatus.equalsIgnoreCase("9")) {
                            mBinding.detailStatus.setText("Reached");
                        }
                        String date = jsonObject2.optString("booking_date");
                        mBinding.detailDate.setText(date);


                        String driver_image = jsonObject2.optString("img");
                        String d_image = GlobalControl.IMAGE_BASE_URL + driver_image;
                        driverImage = d_image;
                        if (!TextUtils.isEmpty(d_image)) {
                            Picasso.with(mContext).
                                    load(d_image).
                                    into(mBinding.detailDriverImg);
                        } else {
                            mBinding.detailDriverImg.setImageResource(R.drawable.defalut_bg);
                        }

                        JSONArray jsonArray31 = jsonObject2.optJSONArray("BookingUserVehicle");
                        JSONObject jsonObject32 = jsonArray31.optJSONObject(0);
                        JSONObject jsonObject3 = jsonObject32.optJSONObject("UserVehicle");
                        String plateNumber = jsonObject3.optString("plate_number");
                        mBinding.detailPlateNumber.setText(plateNumber);
                        String vehicleImage = jsonObject3.optString("vehicle_picture");
                        if (!vehicleImage.equals("")) {
                            Picasso.with(mContext).
                                    load(GlobalControl.IMAGE_BASE_URL + vehicleImage).placeholder(R.drawable.no_image).placeholder(R.drawable.no_image).into(mBinding.detailImg);

                        } else
                            mBinding.detailImg.setImageResource(R.drawable.no_image);


                        mBinding.detailCarName.setText(jsonObject3.optString("make_name") + " " + jsonObject3.optString("model_name"));
                    } else {
                        errorToast(mContext, messsage);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Util.hideProgress();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //   AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }


}