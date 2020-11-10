package com.poshwash.android.Activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.OtpScreenBinding;
import com.poshwash.android.response.OtpConfirmResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends BaseActivity implements View.OnClickListener {

    private OtpScreenBinding binding;
    private String otp, userId, type, mobile;
    MyProgressDialog progressDialog;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.otp_screen);
        Env.currentActivity = OTPActivity.this;
        progressDialog = new MyProgressDialog(Env.currentActivity);
        progressDialog.setCancelable(false);
        otp = getIntent().getStringExtra("otp");
        userId = getIntent().getStringExtra("user_id");
        type = getIntent().getStringExtra("type");
        mobile = getIntent().getStringExtra("mobile");
        Log.e("otp_activity: ",otp);
        warningToast(mContext, otp);
        setOnClick();

        onTextChange(binding.edtOne, binding.edtOne, binding.edtTwo);
        onTextChange(binding.edtOne, binding.edtTwo, binding.edtThree);
        onTextChange(binding.edtTwo, binding.edtThree, binding.edtFour);
        onTextChange(binding.edtThree, binding.edtFour, binding.edtFour);
    }

    private void onTextChange(final EditText edtFirst, final EditText edtCurrent, final EditText edtLast) {

        edtCurrent.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return false;
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (TextUtils.isEmpty(edtCurrent.getText().toString())) {
                        edtFirst.setText("");
                        edtFirst.requestFocus();
                    }
                }
                return false;
            }
        });
        edtCurrent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtCurrent.length() > 0)
                    edtLast.requestFocus();
                else
                    edtCurrent.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setOnClick() {

        binding.btnSubmit.setOnClickListener(this);
        binding.regenrateOtp.setOnClickListener(this);
        binding.backIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit: {
                if (TextUtils.isEmpty(binding.edtOne.getText())
                        || TextUtils.isEmpty(binding.edtTwo.getText())
                        || TextUtils.isEmpty(binding.edtThree.getText())
                        || TextUtils.isEmpty(binding.edtFour.getText())) {
                    Util.showErrorSnackBar(binding.btnSubmit, getString(R.string.valid_otp), this);
                } else {
                    try {
                        String otp = binding.edtOne.getText().toString().trim() + binding.edtTwo.getText().toString().trim() +
                                binding.edtThree.getText().toString().trim() + binding.edtFour.getText().toString().trim();
                        try{
                            callApi_otpConfirmation(CountrySelectListener.APIRequest.send_otp(userId,
                                    otp, String.valueOf(""), String.valueOf(""),
                                    MySharedPreferences.getLanguage(mContext)));
                        }
                        catch (NullPointerException e){
                           // callApi_otpConfirmation(CountrySelectListener.APIRequest.send_otp(userId, otp, String.valueOf(0.0), String.valueOf(0.0)));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case R.id.regenrate_otp: {
                try {
                    callApi_otpResend(CountrySelectListener.APIRequest.re_send_otp(mobile,MySharedPreferences.getLanguage(mContext)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.back_icon: {
                finish();
                overridePendingTransition(R.anim.back_slide_in_up, R.anim.back_slide_out_up);
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void callApi_otpConfirmation(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<OtpConfirmResponse> call = null;
        call = myApiEndpointInterface.otp_confirmation(login);
        call.enqueue(new Callback<OtpConfirmResponse>() {
            @Override
            public void onResponse(Call<OtpConfirmResponse> call, Response<OtpConfirmResponse> response) {
                try {
                    hideProgress();
                    if (response.body().getResponse().isStatus()) {

                        MySharedPreferences.setUserId(mContext, response.body().getResponse().getData().get(0).getUser_id());
                        MySharedPreferences.setLoginStatus(mContext, "true");
                        MySharedPreferences.setNotificationCount(mContext,response.body().getResponse().getData().get(0).getNotification_count());
                        MySharedPreferences.setReferCode(mContext, response.body().getResponse().getData().get(0).getReferral_code());
                        MySharedPreferences.setFirstName(mContext, response.body().getResponse().getData().get(0).getFirst_name());
                        MySharedPreferences.setLastName(mContext, response.body().getResponse().getData().get(0).getLast_name());
                        MySharedPreferences.setUserEmail(mContext, response.body().getResponse().getData().get(0).getEmail());
                        MySharedPreferences.setPhoneNumber(mContext, response.body().getResponse().getData().get(0).getMobile());
                        MySharedPreferences.setProfileImage(mContext, response.body().getResponse().getData().get(0).getImg());
                        startActivity(new Intent(OTPActivity.this, MainActivity.class));
                        finish();
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    } else {
                        errorToast(mContext, response.body().getResponse().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<OtpConfirmResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    private void callApi_otpResend(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.resend_otp(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    boolean status1 = jsonObject1.optBoolean("status");
                    String message1 = jsonObject1.optString("message");
                    JSONArray jsonArray = jsonObject1.optJSONArray("data");
                    JSONObject jsonObject2 = jsonArray.optJSONObject(0);
                    String otp1 = jsonObject2.optString("mobile_otp");
                    if (status1) {
                        /*{"response":{"status":true,"message":"Please enter OTP sent to your mobile.",
                        "data":[{"mobile_otp":"3417"}]}}*/
                        sucessToast(mContext, message1);
                        sucessToast(mContext, otp1);
                    } else {
                        errorToast(mContext, message1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgress();
            }
        });

    }
}
