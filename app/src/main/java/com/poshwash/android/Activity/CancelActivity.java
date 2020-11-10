package com.poshwash.android.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelActivity extends BaseActivity implements
        AdapterView.OnItemSelectedListener {

    private EditText etMessage;
    private TextView btnSubmit;
    private String bookingId, driverId;
    private ImageView ivBack;
    String[] country = {"Booking price is high", "Service provider is too late", "Service provider is not come"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel);

        ivBack = (ImageView) findViewById(R.id.backIv);
        etMessage = (EditText) findViewById(R.id.tv_cancel_message);
        btnSubmit = (TextView) findViewById(R.id.cancel_btn);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        bookingId = getIntent().getExtras().getString("booking_id");
        driverId = getIntent().getExtras().getString("driver_id");
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etMessage.getText().toString().trim())) {
                    errorToast(mContext, "Enter message");
                } else {
                    try {
                        callApiNotification(CountrySelectListener.APIRequest.
                                cancel_booking(MySharedPreferences.getUserId(mContext),
                                        etMessage.getText().toString().trim(), driverId,
                                        bookingId,MySharedPreferences.getLanguage(mContext)));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        //  Toast.makeText(getApplicationContext(), country[position], Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void callApiNotification(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.cancel_booking_by_customer(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    boolean status1 = jsonObject1.optBoolean("status");
                    String message = jsonObject1.optString("message");
                    if (status1) {
                        sucessToast(mContext, message);
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
