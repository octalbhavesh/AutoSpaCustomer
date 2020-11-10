package com.poshwash.android.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Adapter.MyNewBookingAdapter;
import com.poshwash.android.Beans.PastBookingModel;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.DividerItemDecoration;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyBooking extends Fragment implements View.OnClickListener {

    private BroadcastReceiver broadcastReceiver;
    static Context context;
    private ImageView toggle_icon;
    private TextView no_data_tv;
    private RecyclerView expandable_list;
    private ArrayList<PastBookingModel> bookinglist = new ArrayList<>();
    private MyNewBookingAdapter bookingAdapter;
    private TextView heading_tv;
    private MyProgressDialog myProgressDialog;
    private final String TAG = "MyBooking.java";

    public static MyBooking newInstance(Context contex) {
        MyBooking f = new MyBooking();
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
        View view = inflater.inflate(R.layout.my_booking, container, false);
        context = getActivity();
        initView(view);
        return view;
    }

    private void initView(View view) {
        toggle_icon = (ImageView) view.findViewById(R.id.toggle_icon);
        no_data_tv = (TextView) view.findViewById(R.id.no_data_tv);
        expandable_list = (RecyclerView) view.findViewById(R.id.expandable_list);
        heading_tv = (TextView) view.findViewById(R.id.heading_tv);
        bookinglist = new ArrayList<>();
       /* bookingAdapter = new MyBookingAdapter(getActivity(), bookinglist, expandable_list, true);
        expandable_list.setAdapter(bookingAdapter);*/

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        expandable_list.setLayoutManager(mLayoutManager);
        expandable_list.setItemAnimator(new DefaultItemAnimator());
        expandable_list.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        bookingAdapter = new MyNewBookingAdapter(getActivity(), bookinglist);
        expandable_list.setAdapter(bookingAdapter);
        myProgressDialog = new MyProgressDialog(getActivity());
        myProgressDialog.setCancelable(false);
        toggle_icon.setOnClickListener(this);
        ((MainActivity) getActivity()).setPastBookingFragment();
        heading_tv.setText(getString(R.string.my_booking));
        toggle_icon.setOnClickListener(this);

        ((MainActivity) getActivity()).setMyBookingFragment();


    }

    @Override
    public void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
//        callWebServicesMyBooking();
        if (Util.isConnectingToInternet(getActivity())) {
            try {
                callApiPastBooking(CountrySelectListener.APIRequest.
                        my_booking(MySharedPreferences.getUserId(getActivity()),
                                MySharedPreferences.getLanguage(getActivity())));

                   /* callApiNotification(CountrySelectListener.APIRequest.
                            notification_list("20", "1"));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Util.errorToast(getActivity(), getActivity().getString(R.string.no_internet_connection));
        }
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,
                new IntentFilter("initiated"));

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    if (Util.isConnectingToInternet(getActivity())) {
                        try {
                            callApiPastBooking(CountrySelectListener.APIRequest.
                                    my_booking(MySharedPreferences.getUserId(getActivity()),
                                            MySharedPreferences.getLanguage(getActivity())));

                   /* callApiNotification(CountrySelectListener.APIRequest.
                            notification_list("20", "1"));*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Util.errorToast(getActivity(), getActivity().getString(R.string.no_internet_connection));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private void callApiPastBooking(Map<String, Object> login) {
        Util.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.my_bookings(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    bookinglist.clear();
                    Util.hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    String message = jsonObject1.optString("message");
                    boolean status1 = jsonObject1.optBoolean("status");

                    if (status1) {
                        expandable_list.setVisibility(View.VISIBLE);
                        JSONArray jsonArray = jsonObject1.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                            PastBookingModel model = new PastBookingModel();
                            String date = jsonObject2.optString("booking_date");
                            model.setDate(date);
                            String booking_driver_id = jsonObject2.optString("booking_driver_id");
                            model.setDriver_id(booking_driver_id);
                            String booking_id = jsonObject2.optString("booking_id");
                            model.setBooking_id(booking_id);
                            String status = jsonObject2.optString("status");
                            model.setStatus(status);

                            JSONArray jsonArray1 = jsonObject2.optJSONArray("BookingUserVehicle");
                            JSONObject jsonObject3 = jsonArray1.optJSONObject(0);
                            JSONObject jsonObject4 = jsonObject3.optJSONObject("UserVehicle");

                            String plate_number = jsonObject4.optString("plate_number");
                            model.setPlate_number(plate_number);

                            String img = jsonObject4.optString("vehicle_picture");
                            model.setImg(img);

                            String car_name = jsonObject4.optString("make_name") + " " + jsonObject4.optString("model_name");
                            model.setCar_name(car_name);

                            bookinglist.add(model);
                        }

                        if (bookinglist.size() > 0) {
                            bookingAdapter = new MyNewBookingAdapter(getActivity(), bookinglist);
                            expandable_list.setAdapter(bookingAdapter);
                            bookingAdapter.notifyDataSetChanged();
                        }
                    } else {
                        expandable_list.setVisibility(View.GONE);
                        Util.errorToast(getActivity(), message);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toggle_icon:
                ((MainActivity) context).OpenDrawer();
                break;
        }
    }
}