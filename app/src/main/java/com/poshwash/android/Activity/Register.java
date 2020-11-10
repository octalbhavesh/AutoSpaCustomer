package com.poshwash.android.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.FbLoginActivty;
import com.poshwash.android.Utils.GoogleLoginActivity;
import com.poshwash.android.Utils.LocationService;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.databinding.ActivityRegisterBinding;
import com.poshwash.android.response.RegisterResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends BaseActivity implements View.OnClickListener, FbLoginActivty.FbSignInDetail,
        GoogleLoginActivity.GoogleSignInDetail {
    ActivityRegisterBinding mBinding;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String typeFb, typeGoogle, socialId, countryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        getData();
        setSpinnerPreferredLang();
        setOnClick();
    }

    private void setSpinnerPreferredLang() {
        String[] items = {"English", "Arabic"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Env.currentActivity, R.layout.spinner_row, items);
        mBinding.spinnerPreferredLang.setAdapter(adapter);
    }

    private void getData() {

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getBundleExtra("bundle");

            String firstName = Objects.requireNonNull(bundle).getString("first_name");
            String lastName = Objects.requireNonNull(bundle).getString("last_name");
            String email = Objects.requireNonNull(bundle).getString("email");

            mBinding.etFirstName.setText(firstName);
            mBinding.etLastName.setText(lastName);
            mBinding.etEmailAddress.setText(email);
        }
    }

    private void setOnClick() {

        mBinding.backIcon.setOnClickListener(this);
        mBinding.btnSignup.setOnClickListener(this);
        mBinding.imgFb.setOnClickListener(this);
        mBinding.imgGp.setOnClickListener(this);
        mBinding.txtAlreadyaccount.setOnClickListener(this);
        GoogleLoginActivity.setOnGoogleSignInListener(this);
        FbLoginActivty.setOnFbClickListener(this);

        countryCode = mBinding.countryCode.getSelectedCountryCode();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_signup:
                if (isConnectingToInternet(mContext))
                    submit();
                else
                    warningToast(mContext, getString(R.string.no_internet_connection));
                break;

            case R.id.txt_alreadyaccount:
                startActivity(new Intent(Register.this, Login.class));
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;

            case R.id.back_icon:
                startActivity(new Intent(Register.this, Login.class));
                finish();
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;

            case R.id.img_fb:
                startActivity(new Intent(Register.this, FbLoginActivty.class));
                break;

            case R.id.img_gp:
                startActivity(new Intent(Register.this, GoogleLoginActivity.class));
                break;
        }
    }

    private void submit() {

        if (TextUtils.isEmpty(mBinding.etFirstName.getText().toString().trim())) {
            showErrorSnackBar(mBinding.etFirstName, getString(R.string.enter_first_name), mContext);
        } else if (TextUtils.isEmpty(mBinding.etLastName.getText().toString().trim())) {
            showErrorSnackBar(mBinding.etLastName, getString(R.string.enter_last_name), mContext);
        } else if (TextUtils.isEmpty(mBinding.etPhoneNumber.getText().toString().trim())) {
            showErrorSnackBar(mBinding.etPhoneNumber, getString(R.string.enter_your_phone_number), mContext);
        } else if (TextUtils.isEmpty(mBinding.etEmailAddress.getText().toString().trim())) {
            showErrorSnackBar(mBinding.etEmailAddress, getString(R.string.enter_your_email_address), mContext);
        } else if (!mBinding.etEmailAddress.getText().toString().trim().matches(emailPattern)) {
            showErrorSnackBar(mBinding.etEmailAddress, getString(R.string.enter_valid_email_address), mContext);
        } else {
            try {
                String referCode = mBinding.etrefercode.getText().toString().trim();
                String phoneNumber = mBinding.etPhoneNumber.getText().toString().trim();
                String emailAddress = mBinding.etEmailAddress.getText().toString().trim();
                String firstName = mBinding.etFirstName.getText().toString().trim();
                String lastName = mBinding.etLastName.getText().toString().trim();

                String latitude = "", longitude = "";
                if (LocationService.LATEST_LOC_OBJ != null) {
                    latitude = String.valueOf(LocationService.LATEST_LOC_OBJ.getLatitude());
                }
                if (LocationService.LATEST_LOC_OBJ != null) {
                    longitude = String.valueOf(LocationService.LATEST_LOC_OBJ.getLongitude());
                }
                String prefLang = "" + mBinding.spinnerPreferredLang.getSelectedItem();
                String prefLanguage = prefLang.equals("English") ? "eng" : "ara";
                MySharedPreferences.setLanguage(mContext, prefLanguage);
                if (!TextUtils.isEmpty(typeFb)) {
                    callApi(CountrySelectListener.APIRequest.register(referCode, phoneNumber,
                            emailAddress, firstName, lastName, "Location name", prefLanguage, latitude,
                            longitude, "Facebook", socialId, countryCode));
                } else if (!TextUtils.isEmpty(typeGoogle)) {
                    callApi(CountrySelectListener.APIRequest.register(referCode, phoneNumber,
                            emailAddress, firstName, lastName, "Location name", prefLanguage, latitude,
                            longitude, "Google", socialId, countryCode));
                } else {
                    callApi(CountrySelectListener.APIRequest.register(referCode, phoneNumber,
                            emailAddress, firstName, lastName, "Location name", prefLanguage, latitude,
                            longitude, "Manual", "", countryCode));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void callApi(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<RegisterResponse> call = null;
        call = myApiEndpointInterface.register(login);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                try {
                    hideProgress();
                    if (response.body().getResponse().isStatus()) {
                        sucessToast(mContext, response.body().getResponse().getMessage());
                        String userId = response.body().getResponse().getData().get(0).getUser_id();
                        Log.e("otp_user_id", userId);
                        Intent intent = new Intent(Register.this, OTPActivity.class);
                        intent.putExtra("otp", response.body().getResponse().getData().get(0).getMobile_otp());
                        intent.putExtra("type", "register");
                        intent.putExtra("user_id", userId);
                        intent.putExtra("mobile", mBinding.etPhoneNumber.getText().toString().trim());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                    } else {
                        errorToast(mContext, response.body().getResponse().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Register.this, Login.class));
        finish();
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    @Override
    public void onGetFBAccountDetail(JSONObject jsonObject) {
        Log.e("fb_data", String.valueOf(jsonObject));
        socialId = jsonObject.optString("id");
        mBinding.etFirstName.setText(jsonObject.optString("first_name"));
        mBinding.etLastName.setText(jsonObject.optString("last_name"));
        mBinding.etEmailAddress.setText(jsonObject.optString("email"));
        typeFb = "fb";
    }

    @Override
    public void onGetGoogleAccountDetail(GoogleSignInAccount acct) {
        Log.e("google", acct.getEmail());
        socialId = acct.getId();
        mBinding.etFirstName.setText(acct.getDisplayName());
        mBinding.etEmailAddress.setText(acct.getEmail());
        typeGoogle = "google";
    }
}
