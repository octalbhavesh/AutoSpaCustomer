package com.poshwash.android.Activity;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.poshwash.android.Adapter.TimeSlotAdapter;
import com.poshwash.android.Beans.PackagesData;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestActivity extends BaseActivity implements OnDateSelectedListener, OnMonthChangedListener, TimeSlotAdapter.OnTimeSlotSelect {
    private TextView dateTv, timeTv, tvPackageName, tvPackageDes, tvAmount, tvAmountDiscount, txt_offer_success, btnApply;
    private EditText promoCodeEt;
    private String mSelectedId;
    private PackagesData packagesData;
    private TextView next_btn;
    private RelativeLayout layoutDiscount, promoRl;
    List<PackagesData.ResponseBean.DataBean> dataBeansList;
    private CardView card_view;
    private ImageView ivBack;
    Dialog cal_dialog;
    private String mSelectedDate;
    MaterialCalendarView calendarView;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;
    private String mBookingDate, mBookingTime, mBookingReferCode, mBookingType, mBookingPkgName, mBookingPkgDes, mBookingPkgAmount, mBookingPkgAmountDiscount;
    private long btnNext = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        init();
    }

    private void init() {
        ivBack = (ImageView) findViewById(R.id.backIv);
        promoRl = (RelativeLayout) findViewById(R.id.promoRl);
        layoutDiscount = (RelativeLayout) findViewById(R.id.layout_discount);
        card_view = (CardView) findViewById(R.id.card_view);
        txt_offer_success = (TextView) findViewById(R.id.txt_offer_success);
        promoCodeEt = (EditText) findViewById(R.id.promoCodeEt);
        dateTv = (TextView) findViewById(R.id.date_tv);
        timeTv = (TextView) findViewById(R.id.time_tv);
        tvPackageName = (TextView) findViewById(R.id.txtPackageName);
        tvPackageDes = (TextView) findViewById(R.id.txtDescription);
        tvAmountDiscount = (TextView) findViewById(R.id.txtAmountDiscount);
        tvAmount = (TextView) findViewById(R.id.txtAmount);
        btnApply = (TextView) findViewById(R.id.applyBtn);
        next_btn = (TextView) findViewById(R.id.next_btn);
        try {
            callPackageDetailsApi(CountrySelectListener.APIRequest.get_package_details(
                    MySharedPreferences.getUserId(mContext), MySharedPreferences.getLanguage(mContext)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mSelectedId = MySharedPreferences.getBookingVehicleTypeId(mContext);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestActivity.this, MainActivity.class));
                finish();
            }
        });
        txt_offer_success.setVisibility(View.GONE);
        dateTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    getCalanderDates(1);

                }
                return true;
            }
        });

        timeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(RequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        String time = null;
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        if (hourOfDay > 12) {
                            if (hourOfDay == 13) {
                                time = "01";
                            } else if (hourOfDay == 14) {
                                time = "02";
                            } else if (hourOfDay == 15) {
                                time = "03";
                            } else if (hourOfDay == 16) {
                                time = "04";
                            } else if (hourOfDay == 17) {
                                time = "05";
                            } else if (hourOfDay == 18) {
                                time = "06";
                            } else if (hourOfDay == 19) {
                                time = "07";
                            } else if (hourOfDay == 20) {
                                time = "08";
                            } else if (hourOfDay == 21) {
                                time = "09";
                            } else if (hourOfDay == 22) {
                                time = "10";
                            } else if (hourOfDay == 23) {
                                time = "11";
                            } else if (hourOfDay == 24) {
                                time = "12";
                            }
                        } else {
                            time = String.valueOf(hourOfDay);
                        }


                        /* timeTv.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);*/
                        timeTv.setText(time + ":" + minutes + amPm);
                        mBookingTime = hourOfDay + ":" + minutes + ":00";
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });

        btnApply.setEnabled(true);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(promoCodeEt.getText().toString().trim())) {
                    Util.errorToast(mContext, getResources().getString(R.string.enter_refer_code));
                } else {
                    try {
                        callApiReferCodeApply(CountrySelectListener.APIRequest.
                                apply_code(MySharedPreferences.getUserId(mContext),
                                        promoCodeEt.getText().toString().trim(), MySharedPreferences.getLanguage(mContext)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SystemClock.elapsedRealtime() - btnNext < 1000) {
                    return;
                }
                btnNext = SystemClock.elapsedRealtime();

                if (Util.isConnectingToInternet(mContext)) {
                    if (TextUtils.isEmpty(mBookingDate)) {
                        Util.errorToast(mContext, "Please select booking date");
                    } else if (TextUtils.isEmpty(mBookingTime)) {
                        Util.errorToast(mContext, "Please select booking time");
                    } else {
                        MySharedPreferences.setBookingCode(mContext, promoCodeEt.getText().toString().trim());
                        mBookingReferCode = promoCodeEt.getText().toString().trim();
                        Intent i = new Intent(RequestActivity.this, ConfirmActivity.class);
                        i.putExtra("booking_date", mBookingDate);
                        i.putExtra("booking_time", mBookingTime);
                        i.putExtra("booking_date_new", dateTv.getText().toString().trim());
                        i.putExtra("booking_time_new", timeTv.getText().toString().trim());
                        i.putExtra("booking_code", mBookingReferCode);
                        i.putExtra("package_name", mBookingPkgName);
                        i.putExtra("package_des", mBookingPkgDes);
                        i.putExtra("package_amount", mBookingPkgAmount);
                        i.putExtra("package_amount_discount", mBookingPkgAmountDiscount);
                        i.putExtra("booking_type", mBookingType);
                        i.putExtra("language", MySharedPreferences.getLanguage(mContext));
                        startActivity(i);
                    }
                } else {
                    Util.errorToast(mContext, getResources().getString(R.string.no_internet_connection));
                }


            }
        });
    }

    private void getCalanderDates(final int num_dates) {
        cal_dialog = new Dialog(mContext);
        cal_dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cal_dialog.setContentView(R.layout.calenderview_layout);

        calendarView = (MaterialCalendarView) cal_dialog.findViewById(R.id.calendarView);
        final TextView heading_tv = (TextView) cal_dialog.findViewById(R.id.heading_tv);
        heading_tv.setText(R.string.please_select_a_date);

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), instance1.get(Calendar.MONTH), instance1.get(Calendar.DATE));

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), instance1.get(Calendar.MONTH), instance1.get(Calendar.DATE) + 7);
        calendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .commit();

        calendarView.state().edit()
                .setMaximumDate(instance2.getTime())
                .commit();

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.setDynamicHeightEnabled(true);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        cal_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cal_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = cal_dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        cal_dialog.show();
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        // String dat = date.getYear() + "-" + pad((date.getMonth() + 1)) + "-" + pad(date.getDay());
        String dat = pad((date.getMonth() + 1)) + "-" + pad(date.getDay()) + "-" + date.getYear();
        // dateTv.setText(dat);
        mSelectedDate = dat;
        timeTv.setText("");
        dateTv.setText(dat);

        mBookingDate = date.getYear() + "-" + pad((date.getMonth() + 1)) + "-" + pad(date.getDay());

        Calendar cal = Calendar.getInstance();
        int date1 = cal.get(Calendar.DATE);
        int month1 = cal.get(Calendar.MONTH) + 1;

        if (date.getDay() > date1 || (date.getMonth() + 1) > month1) {
            mBookingType = "2";
        } else {
            mBookingType = "1";
        }
        // myRequestDataBean.setRequest_date(dat);
        //Dynamic Data
//        callNearByServiceProvider(mCenterLatLong.latitude, mCenterLatLong.longitude);
        //Static Data
        // getTimeSlotArray(null);
        cal_dialog.cancel();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private void callApiReferCodeApply(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.apply_refer_code(login);
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
                        btnApply.setEnabled(false);
                        promoCodeEt.setClickable(false);
                        MySharedPreferences.setBookingCode(mContext, promoCodeEt.getText().toString().trim());
                        sucessToast(mContext, message);
                        txt_offer_success.setVisibility(View.VISIBLE);
                    } else {
                        btnApply.setEnabled(true);
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

    private void callPackageDetailsApi(Map<String, Object> package_details) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<PackagesData> call = null;
        call = myApiEndpointInterface.get_package_details(package_details);
        call.enqueue(new Callback<PackagesData>() {
            @Override
            public void onResponse(Call<PackagesData> call, Response<PackagesData> response) {
                try {
                    hideProgress();
                    packagesData = response.body();
                    if (packagesData.getResponse().isStatus()) {
                        dataBeansList = packagesData.getResponse().getData();
                        for (int i = 0; i < dataBeansList.size(); i++) {
                            String vehicleTypeId = dataBeansList.get(i).getVehicle_type_id();
                            int isPurchase = dataBeansList.get(i).getIs_purchase();
                            if (vehicleTypeId.equalsIgnoreCase(mSelectedId) && isPurchase == 2) {
                                card_view.setVisibility(View.VISIBLE);
                                tvPackageName.setText(dataBeansList.get(i).getName());
                                tvPackageDes.setText(dataBeansList.get(i).getDescription());
                                mBookingPkgName = dataBeansList.get(i).getName();
                                mBookingPkgDes = dataBeansList.get(i).getDescription();
                                mBookingPkgAmount = dataBeansList.get(i).getAmount();
                                mBookingPkgAmountDiscount = dataBeansList.get(i).getRefer_amount();

                                if (TextUtils.isEmpty(dataBeansList.get(i).getRefer_amount())) {
                                    layoutDiscount.setVisibility(View.GONE);
                                    tvAmountDiscount.setText("$" + dataBeansList.get(i).getAmount());
                                } else {
                                    layoutDiscount.setVisibility(View.VISIBLE);
                                    tvAmount.setText("$" + dataBeansList.get(i).getAmount());
                                    tvAmountDiscount.setText("$" + dataBeansList.get(i).getRefer_amount());

                                }
                                break;
                            } else {
                                if (dataBeansList.size() - 1 == i) {
                                    card_view.setVisibility(View.GONE);
                                    next_btn.setVisibility(View.INVISIBLE);
                                    promoRl.setVisibility(View.INVISIBLE);
                                    errorToast(mContext, getResources().getString(R.string.msg));
                                    // break;
                                }

                            }
                        }

                    } else {
                        errorToast(Env.currentActivity, response.body().getResponse().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<PackagesData> call, Throwable t) {
                hideProgress();
            }
        });
    }

    @Override
    public void timeSlotSelect(int slot) {

    }
}
