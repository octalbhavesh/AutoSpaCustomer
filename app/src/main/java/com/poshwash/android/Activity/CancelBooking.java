package com.poshwash.android.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.customViews.CustomEditTextFont;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anandj on 4/13/2017.
 */

public class CancelBooking extends AppCompatActivity implements View.OnClickListener {

    private ImageView toggle_icon;
    private CustomEditTextFont edt_cancelitem;
    private Button btn_submit;
    private Context context;
    private final String TAG = "CancelBooking.java";
    private MyProgressDialog progressDialog;
    String booking_id = "";
    String booking_driver_id = "";
    TextView selectReasonTv;
    ArrayList<String> reasonList;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cancel_booking);
        context = this;

        reasonList = new ArrayList<>();
        reasonList.add(getString(R.string.booking_price_is_high));
        reasonList.add(getString(R.string.service_provider_is_too_late));
        reasonList.add(getString(R.string.service_provider_not_come));
        initView();

    }

    private void initView() {
        toggle_icon = (ImageView) findViewById(R.id.toggle_icon);
        edt_cancelitem = (CustomEditTextFont) findViewById(R.id.edt_cancelitem);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        selectReasonTv = (TextView) findViewById(R.id.selectReasonTv);

        // prograss bar initialization
        progressDialog = new MyProgressDialog(context);
        progressDialog.setCancelable(false);

        // set typeface
        btn_submit.setTypeface(Util.setTypefaceBold(context));

        // set on click listener
        btn_submit.setOnClickListener(this);
        toggle_icon.setOnClickListener(this);
        selectReasonTv.setOnClickListener(this);

        booking_id = getIntent().getStringExtra("booking_id");
        booking_driver_id = getIntent().getStringExtra("booking_driver_id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                submit();
                break;
            case R.id.toggle_icon:
                Util.hideKeyboard(context, edt_cancelitem);
                finish();
                break;
            case R.id.selectReasonTv:
                Util.hideKeyboard(context, edt_cancelitem);
                showDailogVehicleType(R.style.DialogAnimation_2);
                break;
        }
    }

    // Show Vehicle Type Dialog
    private void showDailogVehicleType(int animationSource) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.choose_vechicle_type);

        RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radio_group);
        TextView titleTV = (TextView) dialog.findViewById(R.id.titleTV);
        titleTV.setText(getString(R.string.cancel_reason));
        for (int i = 0; i < reasonList.size(); i++) {
            // items[i] = vechicleType.get(i).getName();
            AppCompatRadioButton radioButton = new AppCompatRadioButton(context);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 20);

            radioButton.setLayoutParams(layoutParams);
            radioButton.setButtonDrawable(R.drawable.radiobutton_selector);
            radioButton.setChecked(false);
            radioButton.setPadding(17, 0, 0, 10);
            radioButton.setText(reasonList.get(i));
            radioButton.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
            radioButton.setTextSize(18.0f);
            radioButton.setId(i);
            if (selectReasonTv.getText().toString().equalsIgnoreCase(reasonList.get(i)))
                radioButton.setChecked(true);

            radioGroup.addView(radioButton);
        }

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                selectReasonTv.setText(reasonList.get(checkedId));
                edt_cancelitem.setText(reasonList.get(checkedId) + " : ");
                dialog.dismiss();
                edt_cancelitem.setSelection(reasonList.get(checkedId).length());
            }
        });

    }


    private void submit() {
        Util.hideKeyboard(context, edt_cancelitem);
        // validate
        String cancelitem = edt_cancelitem.getText().toString().trim();
        if (TextUtils.isEmpty(cancelitem)) {
            Util.setError(context, edt_cancelitem);
            return;
        }
        // call webservice
        callWebServiceCancelBooking(cancelitem);

    }

    private void callWebServiceCancelBooking(String reason) {
        progressDialog.show();
        Call<ResponseBody> call = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("cancel_reason_text", reason);
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("booking_driver_id", booking_driver_id);
            jsonObject.put("language", MySharedPreferences.getLanguage(context));

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.cancelBookingUser(new ConvertJsonToMap().toMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        // parse response
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {
                            Util.Toast(context, jsonObject1.getJSONObject("response").getString("message"));
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        } else {
                            Util.Toast(context, jsonObject1.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(progressDialog);
                }
            });
        } catch (Exception e) {
            Util.dismissPrograssDialog(progressDialog);
            Log.e(TAG, e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }
}
