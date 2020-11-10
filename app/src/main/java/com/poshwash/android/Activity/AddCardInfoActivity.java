package com.poshwash.android.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardInfoActivity extends BaseActivity {

    private EditText etName, etNumber, etMonth;
    private Button btnAdd;
    private ImageView backIv;
    ArrayList<String> listOfPattern = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_info);
        initView();

        String ptVisa = "^4[0-9]{6,}$";
        listOfPattern.add(ptVisa);
        String ptMasterCard = "^5[1-5][0-9]{5,}$";
        listOfPattern.add(ptMasterCard);
        String ptAmeExp = "^3[47][0-9]{5,}$";
        listOfPattern.add(ptAmeExp);
        String ptDinClb = "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$";
        listOfPattern.add(ptDinClb);
        String ptDiscover = "^6(?:011|5[0-9]{2})[0-9]{3,}$";
        listOfPattern.add(ptDiscover);
        String ptJcb = "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$";
        listOfPattern.add(ptJcb);
    }

    private void initView() {
        backIv = (ImageView) findViewById(R.id.backIv);
        etName = (EditText) findViewById(R.id.et_holder_name);
        etNumber = (EditText) findViewById(R.id.et_number);
        etMonth = (EditText) findViewById(R.id.et_month);
        btnAdd = (Button) findViewById(R.id.btn_add_card);
    }

    @Override
    protected void onResume() {
        super.onResume();

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        etMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String ccNum = s.toString();
                for (String p : listOfPattern) {
                    if (ccNum.matches(p)) {
                        Log.d("DEBUG", "afterTextChanged : discover");
                        break;
                    }
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(etName.getText().toString().trim())) {
                    Toast.makeText(getBaseContext(), "Enter holder name", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etNumber.getText().toString().trim())) {
                    Toast.makeText(getBaseContext(), "Enter card number", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(etMonth.getText().toString().trim())) {
                    Toast.makeText(getBaseContext(), "Enter Month/Year", Toast.LENGTH_LONG).show();
                } else {
                    String month = null, year = null, type = null;
                    try {
                        callApi(CountrySelectListener.APIRequest.add_card(MySharedPreferences.getUserId(mContext),
                                etNumber.getText().toString().trim(),
                                "08", "20", "VISA", etName.getText().toString().trim()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }

    private void callApi(Map<String, Object> login) {
        showProgress(mContext);
        String device_token = "";
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.save_card(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    boolean status = jsonObject1.optBoolean("status");
                    String message = jsonObject1.optString("message");

                    if (status) {
                        finish();
                    } else {
                        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
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
