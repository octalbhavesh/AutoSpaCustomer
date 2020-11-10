package com.poshwash.android.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.ActivityRatingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingActivity extends BaseActivity {
    private String data;
    private ActivityRatingBinding mBinding;
    private String driver_name, booking_driver_id, driver_image, booking_id;
    DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_rating);
        data = getIntent().getStringExtra("data");
        Log.e("data", data);
        Bitmap default_bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.iphone);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(mContext.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(mContext.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(mContext.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        try {
            JSONObject jsonObject = new JSONObject(data);

            driver_name = jsonObject.optString("customer_name");
            driver_image = jsonObject.optString("customer_profile");
            booking_driver_id = jsonObject.optString("Booking_driver_id");
            booking_id = jsonObject.optString("booking_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mBinding.txtCustomerName.setText(driver_name);
        if (!driver_image.equals(""))
            ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL +
                    driver_image, mBinding.imgUserImage, options);


        mBinding.txtCompleteRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    callApiNotification(CountrySelectListener.APIRequest.
                            rating(MySharedPreferences.getUserId(mContext), "eng", booking_driver_id, booking_id, String.valueOf(mBinding.ratingBar.getRating())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void callApiNotification(Map<String, Object> login) {
        Util.showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.driver_rating(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Util.hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    String message = jsonObject1.optString("message");
                    boolean status1 = jsonObject1.optBoolean("status");
                    if (status1) {
                        Util.sucessToast(mContext, message);
                        startActivity(new Intent(RatingActivity.this, MainActivity.class));

                    } else {
                        Util.errorToast(mContext, message);

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
}
