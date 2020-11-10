package com.poshwash.android.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.poshwash.android.Activity.EditProfile;
import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Activity.OTPActivity;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.countryList.Country;
import com.poshwash.android.countryList.CountryAdapter;
import com.poshwash.android.countryList.PhoneUtils;
import com.poshwash.android.customViews.CustomEditTextFont;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anandj on 3/16/2017.
 */

public class MyProfile extends Fragment implements View.OnClickListener, CountrySelectListener {

    static Context context;
    private ImageView toggle_icon;
    private TextView txt_email;
    private TextView txt_phone;
    private TextView txt_language;
    private TextView txt_phone_edit;
    private Button btn_editprofile;
    private RatingBar rating_bar;
    private CircleImageView user_profleimg;
    private TextView txt_username;
    MyProgressDialog myProgressDialog;
    protected SparseArray<ArrayList<Country>> mCountriesMap = new SparseArray<ArrayList<Country>>();
    protected CountryAdapter mAdapter;
    protected PhoneNumberUtil mPhoneNumberUtil = PhoneNumberUtil.getInstance();
    ArrayList<Country> countryList;
    Dialog countryDialog;
    private EditText dialogTextBox;
    private EditText etCountryCode;
    MyProgressDialog progressDialog;

    public static MyProfile newInstance(Context contex) {
        MyProfile f = new MyProfile();
        context = contex;
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myprofile, container, false);
        context = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        toggle_icon = (ImageView) view.findViewById(R.id.toggle_icon);
        txt_email = (TextView) view.findViewById(R.id.txt_email);
        txt_phone = (TextView) view.findViewById(R.id.txt_phone);
        txt_phone_edit = (TextView) view.findViewById(R.id.txt_phone_edit);
        btn_editprofile = (Button) view.findViewById(R.id.btn_editprofile);
        user_profleimg = (CircleImageView) view.findViewById(R.id.user_profleimg);
        txt_username = (TextView) view.findViewById(R.id.txt_username);
        txt_language = (TextView) view.findViewById(R.id.txt_language);
        rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);

        progressDialog = new MyProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        countryList = new ArrayList<>();
        initCodes(getActivity());

        btn_editprofile.setOnClickListener(this);
        toggle_icon.setOnClickListener(this);
        txt_phone_edit.setOnClickListener(this);
        myProgressDialog = new MyProgressDialog(context);
        myProgressDialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
        autoFillData();
        if(Util.isConnectingToInternet(getActivity()))
        getProfile();
        else
            Util.errorToast(getActivity(),getActivity().getString(R.string.no_internet_connection));

    }

    public void autoFillData() {
        txt_username.setText(MySharedPreferences.getFirstName(getActivity()) + " " + MySharedPreferences.getLastName(getActivity()));
        txt_email.setText(MySharedPreferences.getUserEmail(getActivity()));
        txt_phone.setText(MySharedPreferences.getPhoneNumber(getActivity()));
        if (MySharedPreferences.getLanguage(getActivity()).compareTo("eng") == 0) {
            txt_language.setText(getString(R.string.english));
        } else {
            txt_language.setText(getString(R.string.arabic));
        }

        Util.setProfilePic(getActivity(), user_profleimg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_editprofile:
                startActivity(new Intent(getActivity(), EditProfile.class));
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
                break;
            case R.id.toggle_icon:
                ((MainActivity) context).OpenDrawer();
                break;
            case R.id.txt_phone_edit:
                getNewPhoneNumber();
                break;
        }
    }


    private void getProfile() {
        myProgressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("language", MySharedPreferences.getLanguage(context));
            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.getProfile(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(myProgressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(response.body().string());
                        Log.e("tg", "response from server = " + mJsonObj.toString());
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1 = mJsonObj.getJSONObject("response").getJSONObject("data");

                           /* if (jsonObject1.getString("avail_status").equalsIgnoreCase("0") || jsonObject1.getString("avail_status").equalsIgnoreCase("2"))
                                ((MainActivity) getActivity()).LogoutWebService(getActivity(), true);
                            else {*/

                            MySharedPreferences.setFirstName(context, jsonObject1.getString("first_name"));
                            MySharedPreferences.setLastName(context, jsonObject1.getString("last_name"));
                            MySharedPreferences.setUserEmail(context, jsonObject1.getString("email"));
                            MySharedPreferences.setPhoneNumber(context, jsonObject1.getString("mobile"));
                            MySharedPreferences.setCountryCode(context, jsonObject1.getString("country_code"));
                            MySharedPreferences.setLoginType(context, jsonObject1.getString("login_type"));
                            MySharedPreferences.setLanguage(context, jsonObject1.getString("language"));
                            MySharedPreferences.setNotificationSetting(context, jsonObject1.getString("notification"));
                            MySharedPreferences.setProfileImage(context, GlobalControl.IMAGE_BASE_URL + jsonObject1.getString("img"));
                            MySharedPreferences.setNotificationCount(context, jsonObject1.getString("unread_badge_count"));
                            autoFillData();
                            ((MainActivity) getActivity()).SetProfile();
                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Util.dismissPrograssDialog(myProgressDialog);

                }
            });
        } catch (Exception e) {
            Util.dismissPrograssDialog(myProgressDialog);
        }
    }

    private void getNewPhoneNumber() {

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.input_mobile_number);


        TextView txt_cancel = (TextView) dialog.findViewById(R.id.txt_cancel);
        TextView txt_ok = (TextView) dialog.findViewById(R.id.txt_ok);
        TextView titleTv = (TextView) dialog.findViewById(R.id.titleTv);
        etCountryCode = (CustomEditTextFont) dialog.findViewById(R.id.et_countryCode);
        dialogTextBox = (EditText) dialog.findViewById(R.id.edit_dialog);
        titleTv.setText(getString(R.string.mobile_number));


        etCountryCode.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    ShowCountryCodes();
                }
                return true;
            }
        });

        txt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogTextBox.getText().toString().trim().equals("")) {
                    Util.setError(context, dialogTextBox);
                    return;
                }

                if (TextUtils.getTrimmedLength(dialogTextBox.getText().toString().trim()) < 9) {
                    Toast.makeText(context, R.string.valid_phone_number, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(dialogTextBox.getWindowToken(), 0);
                    // callForgotPasswordWebService(dialogTextBox.getText().toString());
                    callChangeNumberWebservice(dialogTextBox.getText().toString().trim(), etCountryCode.getText().toString().trim());


                }
                dialog.dismiss();
            }
        });
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();

    }

    public void ShowCountryCodes() {
        countryDialog = new Dialog(context);
        countryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        countryDialog.setContentView(R.layout.country_code_layout);
        RecyclerView counrtyRecycler = (RecyclerView) countryDialog.findViewById(R.id.counrtyRecycler);

        mAdapter = new CountryAdapter(getActivity(), countryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        counrtyRecycler.setLayoutManager(mLayoutManager);
        counrtyRecycler.setItemAnimator(new DefaultItemAnimator());
        counrtyRecycler.setAdapter(mAdapter);
        mAdapter.onclickListner(this);

        countryDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        countryDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = countryDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        countryDialog.show();

    }

    protected void initCodes(Context context) {
        new AsyncPhoneInitTask(context).execute();
    }


    protected class AsyncPhoneInitTask extends AsyncTask<Void, Void, ArrayList<Country>> {

        private int mSpinnerPosition = -1;
        private Context mContext;

        public AsyncPhoneInitTask(Context context) {
            mContext = context;
        }

        @Override
        protected ArrayList<Country> doInBackground(Void... params) {
            ArrayList<Country> data = new ArrayList<Country>(233);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));

                // do reading, usually loop until end of file reading
                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    //process line
                    Country c = new Country(mContext, line, i);
                    data.add(c);
                    ArrayList<Country> list = mCountriesMap.get(c.getCountryCode());
                    if (list == null) {
                        list = new ArrayList<Country>();
                        mCountriesMap.put(c.getCountryCode(), list);
                    }
                    list.add(c);
                    i++;
                }
            } catch (IOException e) {
                //log the exception
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //log the exception
                    }
                }
            }
           /* if (!TextUtils.isEmpty(mPhoneEdit.getText())) {
                return data;
            }*/
            String countryRegion = PhoneUtils.getCountryRegionFromPhone(mContext);
            int code = mPhoneNumberUtil.getCountryCodeForRegion(countryRegion);
            ArrayList<Country> list = mCountriesMap.get(code);
            if (list != null) {
                for (Country c : list) {
                    if (c.getPriority() == 0) {
                        mSpinnerPosition = c.getNum();
                        break;
                    }
                }
            }
            return data;
        }

        @Override
        protected void onPostExecute(ArrayList<Country> data) {
            countryList.clear();
            countryList.addAll(data);
//            etCountryCode.setText(countryList.get(mSpinnerPosition).getCountryCode() + "");

        }
    }

    @Override
    public void onCountryClick(int position) {
        etCountryCode.setText(countryList.get(position).getCountryCode() + "");
        if (countryDialog != null)
            countryDialog.cancel();
    }

    private void callChangeNumberWebservice(final String mobile_number, final String country_code) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {

            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("language", MySharedPreferences.getLanguage(context));
            jsonObject.put("mobile", MySharedPreferences.getPhoneNumber(context));
            jsonObject.put("new_mobile", mobile_number);
            jsonObject.put("country_code", country_code);

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.updateContactNumber(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            JSONObject jsonObject1 = mJsonObj.getJSONObject("response").getJSONArray("data").getJSONObject(0);

                            Bundle bundle = new Bundle();
                            bundle.putString("user_id", MySharedPreferences.getUserId(context));
                            bundle.putString("mobile", mobile_number);
                            bundle.putString("country_code", country_code);

                            Intent intent = new Intent(context, OTPActivity.class);
                            intent.putExtras(bundle);
                            intent.putExtra("otp", jsonObject1.getString("mobile_otp"));
                            intent.putExtra("type", "updateNumber");
                            startActivity(intent);
                            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

                        } else {
                            Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(progressDialog);
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }

    }

}
