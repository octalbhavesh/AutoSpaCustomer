package com.poshwash.android.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.ActivityConfirmBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmActivity extends BaseActivity {
    private ActivityConfirmBinding mBinding;
    private ImageView ivBack;
    private TextView dateTv, tvLocation, timeTv, tvPackageName, tvPackageDes, tvAmount, tvAmountDiscount,
            txt_offer_success, btnConfirm, btnCancel, tvCarName;
    private RelativeLayout layoutDiscount;
    private CardView card_view;
    private String mBookingTime, mCode, mBookingDate, mVehivleTypeId, mLongitude, mPaymentType, mCarName, mCarPlate,
            mBookingAddress, mLatitude, mBookingType, mReferCode, mUserVehicleId, mPkgName, mPkgDes,
            mPkgAmount, mPkgAmountDiscount, dateNew, timeNew;
    private long btnConfirm1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_confirm);
        setContentView(R.layout.activity_confirm);
        init();
    }

    private void init() {
        btnCancel = (TextView) findViewById(R.id.btn_cancel);
        tvCarName = (TextView) findViewById(R.id.tv_car_name);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        dateTv = (TextView) findViewById(R.id.tv_confirm_date);
        ivBack = (ImageView) findViewById(R.id.backIv);
        layoutDiscount = (RelativeLayout) findViewById(R.id.layout_discount);
        card_view = (CardView) findViewById(R.id.card_view);
        txt_offer_success = (TextView) findViewById(R.id.txt_offer_success);

        timeTv = (TextView) findViewById(R.id.time_tv);
        tvPackageName = (TextView) findViewById(R.id.txtPackageName);
        tvPackageDes = (TextView) findViewById(R.id.txtDescription);
        tvAmountDiscount = (TextView) findViewById(R.id.txtAmountDiscount);
        tvAmount = (TextView) findViewById(R.id.txtAmount);
        btnConfirm = (TextView) findViewById(R.id.next_btn);

        mBookingAddress = MySharedPreferences.getBookingAddress(mContext);
        mPkgName = getIntent().getExtras().getString("package_name");
        mPkgDes = getIntent().getExtras().getString("package_des");
        mPkgAmount = getIntent().getExtras().getString("package_amount");
        mPkgAmountDiscount = getIntent().getExtras().getString("package_amount_discount");
        mBookingDate = getIntent().getExtras().getString("booking_date");
        mBookingTime = getIntent().getExtras().getString("booking_time");
        dateNew = getIntent().getExtras().getString("booking_date_new");
        timeNew = getIntent().getExtras().getString("booking_time_new");
        mCode = getIntent().getExtras().getString("booking_code");
        mBookingType = getIntent().getExtras().getString("booking_type");
        mLatitude = MySharedPreferences.getBookingLat(mContext);
        mLongitude = MySharedPreferences.getBookingLong(mContext);
        mCarName = MySharedPreferences.getBookingUserVehicleName(mContext);
        mCarPlate = MySharedPreferences.getBookingUserPlate(mContext);
        mVehivleTypeId = MySharedPreferences.getBookingVehicleTypeId(mContext);
        mUserVehicleId = MySharedPreferences.getBookingUserVehicleId(mContext);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvPackageName.setText(mPkgName);
        tvPackageDes.setText(mPkgDes);
        if (TextUtils.isEmpty(mPkgAmountDiscount)) {
            layoutDiscount.setVisibility(View.GONE);
            tvAmountDiscount.setText("$" + mPkgAmount);
        } else {
            layoutDiscount.setVisibility(View.VISIBLE);
            tvAmount.setText("$" + mPkgAmount);
            tvAmountDiscount.setText("$" + mPkgAmountDiscount);
        }

        tvCarName.setText(mCarName + "(" + mCarPlate + ")");
        tvAmountDiscount.setText("$" + mPkgAmount);
        dateTv.setText(dateNew + "," + timeNew);
        tvLocation.setText("Location: " + mBookingAddress);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmActivity.this, MainActivity.class));
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - btnConfirm1 < 1000) {
                    return;
                }
                btnConfirm1 = SystemClock.elapsedRealtime();
                try {
                    callApiNotification(CountrySelectListener.APIRequest.
                            booking(MySharedPreferences.getUserId(mContext), mVehivleTypeId, mBookingDate, mLongitude
                                    , "3", mBookingAddress, mLatitude, mBookingType,
                                    mCode,
                                    mUserVehicleId, mBookingTime,MySharedPreferences.getLanguage(mContext)));

                   /* callApiNotification(CountrySelectListener.APIRequest.
                            notification_list("20", "1"));*/
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
        call = myApiEndpointInterface.booking_saves(login);
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
                        sucessToast(mContext, message);
                        Intent i = new Intent(ConfirmActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        errorToast(mContext, message);
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
