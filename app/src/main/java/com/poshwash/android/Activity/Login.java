package com.poshwash.android.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.FbLoginActivty;
import com.poshwash.android.Utils.GoogleLoginActivity;
import com.poshwash.android.Utils.LocationService;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.ActivityLoginBinding;
import com.poshwash.android.response.LoginResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends BaseActivity implements View.OnClickListener, CountrySelectListener, FbLoginActivty.FbSignInDetail,
        GoogleLoginActivity.GoogleSignInDetail {

    final String TAG = "Login.java";
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 9001;
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
    private EditText etCountryCode, dialogTextBox;
    private ActivityLoginBinding binding;
    private String countryCode;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        Env.currentActivity = Login.this;
        checkRequestPermission();
        setOnClick();
        if (TextUtils.isEmpty(MySharedPreferences.getDeviceId(Login.this))) {
            if (FirebaseInstanceId.getInstance().getToken() != null) {
                MySharedPreferences.setDeviceId(getApplicationContext(), FirebaseInstanceId.getInstance().getToken());
            }
        }
    }

    private void setOnClick() {
        binding.btnSignin.setOnClickListener(this);
        binding.txtAlreadyaccount.setOnClickListener(this);
        binding.imgFb.setOnClickListener(this);
        binding.imgGp.setOnClickListener(this);
        GoogleLoginActivity.setOnGoogleSignInListener(this);
        FbLoginActivty.setOnFbClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_signin:
                if (TextUtils.isEmpty(binding.etMobileNumber.getText().toString().trim())) {
                    showErrorSnackBar(binding.etMobileNumber, getString(R.string.mobile_number_required), mContext);
                    return;
                }
                if (binding.etMobileNumber.getText().toString().trim().length() < 6) {
                    showErrorSnackBar(binding.etMobileNumber, getString(R.string.enter_valid_mobile_number), mContext);
                    return;
                }
                try {
                    countryCode = binding.countryCode.getSelectedCountryCode();
                    Log.e("code", countryCode);
                    String mobileNumber = binding.etMobileNumber.getText().toString().trim();
                    String latitude = "", longitude = "";
                    if (LocationService.LATEST_LOC_OBJ != null) {
                        latitude = String.valueOf(LocationService.LATEST_LOC_OBJ.getLatitude());
                    }
                    if (LocationService.LATEST_LOC_OBJ != null) {
                        longitude = String.valueOf(LocationService.LATEST_LOC_OBJ.getLongitude());
                    }
                    if (Util.isConnectingToInternet(mContext))
                        callApi(CountrySelectListener.APIRequest.login(mobileNumber, latitude, longitude,
                                "eng", countryCode));
                    else
                        Util.errorToast(mContext, mContext.getString(R.string.no_internet_connection));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.txt_alreadyaccount:
                startActivity(new Intent(Login.this, Register.class));
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                finish();
                break;
            case R.id.img_gp:
                signIn();
                break;
            case R.id.img_fb:
                fbLogin();
                break;
        }
    }

    private void fbLogin() {

        if (AccessToken.getCurrentAccessToken() == null) {
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            String accessToken = loginResult.getAccessToken().getToken();
                            Log.i("accessToken", accessToken);

                            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    Log.i("LoginActivity", response.toString());
                                    Log.i("activty", object + "");

                                    try {
                                        String id = object.getString("id");

                                        Bundle bundle = new Bundle();
                                        bundle.putString("id", id);
                                        bundle.putString("first_name", object.getString("first_name"));
                                        bundle.putString("last_name", object.getString("last_name"));
                                        bundle.putString("email", object.getString("email"));
                                        bundle.putString("login_type", "Facebook");
                                        bundle.putString("profile_image", Util.getFbProfileImageUrl(id));

                                        callSocialExistWebservice(id);

                                    } catch (Exception e) {
                                        Log.e(TAG, e.toString());
                                    }
                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id, first_name, last_name, email,gender"); // Parámetros que pedimos a facebook
                            request.setParameters(parameters);
                            request.executeAsync();
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Log.e("exception", exception.toString());
                        }
                    });

        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

            }
        }).executeAsync();

    }

    private void signIn() {
        if (!AutoSpaApplication.mGoogleApiClient.hasConnectedApi(Auth.GOOGLE_SIGN_IN_API)) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            AutoSpaApplication.mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(AutoSpaApplication.mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void checkRequestPermission() {

        if (ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            }, 1);
        } else {
            getLastLocation();
            AutoSpaApplication.location = LocationServices.FusedLocationApi.getLastLocation(AutoSpaApplication.mGoogleApiClient);

        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {

        AutoSpaApplication.mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            AutoSpaApplication.mLastLocation = task.getResult();

                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        int i = 0;
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkRequestPermission();
                //   startService(new Intent(Login.this, LocationService.class));
            } else {
                finish();
            }
        }
    }


    @Override
    public void onCountryClick(int position) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Log.d("statuscode", result.getStatus().toString());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            //String name = "";
            if (acct != null) {
                if (acct.getDisplayName() != null) {
                    String[] name = acct.getDisplayName().split(" ");
                    String fName = name[0];
                    String lName = name[1];
                    String url = "";
                    if (acct.getPhotoUrl() != null) {
                        url = acct.getPhotoUrl().toString();
                    }
                    try {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", acct.getId());
                        bundle.putString("first_name", fName);
                        bundle.putString("last_name", lName);
                        bundle.putString("email", acct.getEmail());
                        bundle.putString("login_type", "Google");
                        bundle.putString("profile_image", url);

                        callSocialExistWebservice(acct.getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /*APIs*/
    private void callApi(Map<String, Object> login) {
        showProgress(mContext);
        String device_token = "";
        if (!MySharedPreferences.getDeviceId(Login.this).equals("") || MySharedPreferences.getDeviceId(Login.this).equals("null")) {
            device_token = MySharedPreferences.getDeviceId(Login.this);
        } else {
            if (FirebaseInstanceId.getInstance().getToken() != null) {
                device_token = FirebaseInstanceId.getInstance().getToken();
                MySharedPreferences.setDeviceId(getApplicationContext(), device_token);
            }
        }
        if (!TextUtils.isEmpty(device_token)) {
            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            Call<LoginResponse> call = null;
            call = myApiEndpointInterface.login(login);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    try {
                        hideProgress();
                        if (response.body().getResponse().isStatus()) {
                            // sucessToast(mContext, response.body().getResponse().getMessage());
                            Intent intent = new Intent(Login.this, OTPActivity.class);
                            intent.putExtra("user_id", response.body().getResponse().getData().get(0).getUser_id());
                            intent.putExtra("mobile", binding.etMobileNumber.getText().toString().trim());
                            intent.putExtra("otp", response.body().getResponse().getData().get(0).getMobile_otp());
                            intent.putExtra("type", "login");
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
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    hideProgress();
                }
            });
        } else {
            hideProgress();
            errorToast(mContext, "There is some issue with access notification please try again.");
            if (MySharedPreferences.getDeviceId(Login.this).equals("") || MySharedPreferences.getDeviceId(Login.this).equals("null")) {
                if (FirebaseInstanceId.getInstance().getToken() != null) {
                    MySharedPreferences.setDeviceId(getApplicationContext(), FirebaseInstanceId.getInstance().getToken());
                }
            }
            return;
        }
    }

    private void callSocialExistWebservice(String id) {
        showProgress(mContext);
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("language", MySharedPreferences.getLanguage(mContext));
            jsonObject.put("social_id", id);
            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.socialExist(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgress();
                    try {
                        String response1 = response.body().string();
                        JSONObject mJsonObj = new JSONObject(response1);
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            JSONArray jsonArray = mJsonObj.getJSONObject("response").optJSONArray("data");
                            JSONObject jsonObject1 = jsonArray.optJSONObject(0);
                            String userId = jsonObject1.optString("user_id");
                            MySharedPreferences.setUserId(mContext, userId);
                            MySharedPreferences.setLanguage(mContext, "eng");
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            errorToast(mContext, mJsonObj.getJSONObject("response").optString("message"));
                            // finish();
                        }

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgress();
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            hideProgress();
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }

    }

    private void callSocialLoginWebservice(Bundle bundle) {

        showProgress(Env.currentActivity);
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        String device_token = "";
        if (!MySharedPreferences.getDeviceId(Login.this).equals("")) {
            device_token = MySharedPreferences.getDeviceId(Login.this);
        } else {
            if (FirebaseInstanceId.getInstance().getToken() != null) {
                device_token = FirebaseInstanceId.getInstance().getToken();
                MySharedPreferences.setDeviceId(getApplicationContext(), device_token);
            }
        }

        String lat = "";
        String lng = "";
        if (LocationService.LATEST_LOC_OBJ != null) {
            try {
                lat = LocationService.LATEST_LOC_OBJ.getLatitude() + "";
                lng = LocationService.LATEST_LOC_OBJ.getLongitude() + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!TextUtils.isEmpty(device_token)) {
            try {

                jsonObject = new JSONObject();
                jsonObject.put("social_id", bundle.getString("id"));
                jsonObject.put("device_id", device_token);
                jsonObject.put("device_type", getString(R.string.android));
                jsonObject.put("login_type", bundle.getString("login_type"));
                jsonObject.put("email", bundle.getString("email"));
                jsonObject.put("first_name", bundle.getString("first_name"));
                jsonObject.put("last_name", bundle.getString("last_name"));
                jsonObject.put("mobile", "");
                jsonObject.put("country_code", "");
                jsonObject.put("img", bundle.getString("profile_image"));
                jsonObject.put("location", "");
                jsonObject.put("latitude", lat);
                jsonObject.put("longitude", lng);
                jsonObject.put("language", MySharedPreferences.getLanguage(mContext));

                MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
                call = endpointInterface.socailLogin(new ConvertJsonToMap().jsonToMap(jsonObject));

                final String finalDevice_token = device_token;
                call.enqueue(new Callback<ResponseBody>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        hideProgress();
                        try {
                            JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                            Log.e("tg", "response from server = " + mJsonObj.toString());
                            if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                                JSONObject jsonObject1 = mJsonObj.getJSONObject("response").getJSONArray("data").getJSONObject(0);
                                if (jsonObject1.getString("mobile_verify").compareTo("0") == 0) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("user_id", jsonObject1.getString("user_id"));
                                    bundle.putString("mobile", jsonObject1.getString("mobile"));
                                    bundle.putString("country_code", jsonObject1.getString("country_code"));

                                    Intent intent = new Intent(Env.currentActivity, OTPActivity.class);
                                    intent.putExtras(bundle);
                                    intent.putExtra("otp", jsonObject1.getString("mobile_otp"));
                                    intent.putExtra("type", "signup");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                                } else {
                                    MySharedPreferences.setUserId(Env.currentActivity, jsonObject1.getString("user_id"));
                                    MySharedPreferences.setLoginType(Env.currentActivity, jsonObject1.getString("login_type"));
                                    MySharedPreferences.setFirstName(Env.currentActivity, jsonObject1.getString("first_name"));
                                    MySharedPreferences.setLastName(Env.currentActivity, jsonObject1.getString("last_name"));
                                    MySharedPreferences.setPhoneNumber(Env.currentActivity, jsonObject1.getString("mobile"));
                                    MySharedPreferences.setUserEmail(Env.currentActivity, jsonObject1.getString("email"));
                                    MySharedPreferences.setNotificationSetting(Env.currentActivity, jsonObject1.getString("notification"));
                                    MySharedPreferences.setNotificationCount(Env.currentActivity, jsonObject1.getString("notification_count"));
                                    MySharedPreferences.setProfileImage(Env.currentActivity, jsonObject1.getString("img"));
                                    MySharedPreferences.setLanguage(mContext, jsonObject1.getString("language"));

                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                                }
                            } else {
                                Util.Toast(Env.currentActivity, mJsonObj.getJSONObject("response").getString("message"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideProgress();
                        t.printStackTrace();
                    }
                });

            } catch (Exception e) {
                hideProgress();
                e.printStackTrace();
            }
        } else {
            hideProgress();
            Util.errorToast(Env.currentActivity, "There is some issue with access notification, Please try again.");
            if (MySharedPreferences.getDeviceId(Login.this).equals("") || MySharedPreferences.getDeviceId(Login.this).equals("null")) {
                if (FirebaseInstanceId.getInstance().getToken() != null) {
                    MySharedPreferences.setDeviceId(getApplicationContext(), FirebaseInstanceId.getInstance().getToken());
                }
            }
            LoginManager.getInstance().logOut();
            return;
        }
    }

    @Override
    public void onGetFBAccountDetail(JSONObject newProfile) {

    }

    @Override
    public void onGetGoogleAccountDetail(GoogleSignInAccount acct) {

    }
}