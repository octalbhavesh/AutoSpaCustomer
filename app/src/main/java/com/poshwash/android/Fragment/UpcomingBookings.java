package com.poshwash.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Adapter.UpcomingBookingAdapter;
import com.poshwash.android.Beans.MyBookingChildModal;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.DividerItemDecoration;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by anandj on 3/20/2017.
 */

public class UpcomingBookings extends Fragment
{

    private ImageView toggle_icon;
    private RecyclerView recyclerview;
    private TextView no_data_tv;
    private TextView action_title;
    private UpcomingBookingAdapter adapter;
    private List<MyBookingChildModal> transactionList;
    private MyProgressDialog myProgressDialog;
    private final String TAG = "myProgressDialog.java";
    Context context;

    public static UpcomingBookings newInstance(Context contex)
    {
        UpcomingBookings f = new UpcomingBookings();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_transaction_history, container, false);
        context=getActivity();
        initView(view);

        ((MainActivity) getActivity()).setUpcommingBookingFragment();
        return view;
    }

    private void initView(View view) {
        toggle_icon = (ImageView) view.findViewById(R.id.toggle_icon);
        no_data_tv = (TextView) view.findViewById(R.id.no_data_tv);
        action_title = (TextView) view.findViewById(R.id.action_title);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        transactionList = new ArrayList<>();
        adapter = new UpcomingBookingAdapter(getActivity(), transactionList,UpcomingBookings.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerview.setAdapter(adapter);

        myProgressDialog = new MyProgressDialog(getActivity());
        myProgressDialog.setCancelable(false);

        // call webservice
        callWebServiceShowUpcomingBookings();

        action_title.setText(getString(R.string.upcoming_bookings));
        no_data_tv.setText(getString(R.string.no_data_found));

    }

    // call webservice for show Transaction history
    public void callWebServiceShowUpcomingBookings()
    {
        myProgressDialog.show();
        Call<ResponseBody> call = null;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject.put("language", MySharedPreferences.getLanguage(getActivity()));

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.myUpcomingBooking(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(myProgressDialog);
                    try {
                        JSONObject jsonObject1 = new JSONObject(Util.convertRetrofitResponce(response));
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {
                            JSONArray jsonArray = jsonObject1.getJSONObject("response").getJSONArray("data");

                            if (jsonArray.length() > 0) {
                                no_data_tv.setVisibility(View.GONE);
                                recyclerview.setVisibility(View.VISIBLE);
                            } else {
                                no_data_tv.setVisibility(View.VISIBLE);
                                recyclerview.setVisibility(View.GONE);
                            }
                            // pass json array
                            prepareData(jsonArray);
                        } else {
                            no_data_tv.setVisibility(View.VISIBLE);
                            recyclerview.setVisibility(View.GONE);
                            Util.Toast(getActivity(), jsonObject1.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(myProgressDialog);
                }
            });
        } catch (Exception e) {
            Util.dismissPrograssDialog(myProgressDialog);
            Log.e(TAG, e.toString());
        }
    }

    private void prepareData(JSONArray jsonArray)
    {
        transactionList.clear();

        for (int j = 0; j < jsonArray.length(); j++)
        {
            try {

                JSONObject jsonObject = jsonArray.getJSONObject(j);
                MyBookingChildModal myBookingChildModal = new MyBookingChildModal();

                myBookingChildModal.setId(jsonObject.getString("Booking_id"));
                myBookingChildModal.setOwner_name(jsonObject.getString("first_name")+" "+jsonObject.getString("last_name"));
                myBookingChildModal.setPrice(jsonObject.getString("booking_amount"));
                myBookingChildModal.setService_type(jsonObject.getString("payment_type"));
                myBookingChildModal.setBooking_date(jsonObject.getString("booking_date"));
                myBookingChildModal.setBooking_time(jsonObject.getString("booking_time"));
                myBookingChildModal.setBooking_long(jsonObject.getString("booking_address"));
                myBookingChildModal.setUser_phone(jsonObject.getString("mobile"));
                myBookingChildModal.setFareReview(jsonObject.getString("fair_review"));
                myBookingChildModal.setVehicleDetail(jsonObject);

                String vehicleName = "";
                if (jsonObject.getJSONArray("BookingUserVehicle").length() > 0)
                {
                    myBookingChildModal.setImage(jsonObject.getJSONArray("BookingUserVehicle").getJSONObject(0).getJSONObject("UserVehicle").getString("vehicle_picture"));
                    if (jsonObject.getJSONArray("BookingUserVehicle").length() == 1) {
                        vehicleName = jsonObject.getJSONArray("BookingUserVehicle").getJSONObject(0).getJSONObject("UserVehicle").getString("model") + " (" + jsonObject.getJSONArray("BookingUserVehicle").getJSONObject(0).getJSONObject("UserVehicle").getJSONObject("VehicleType").getString("name") + ")";
                    } else {
                        vehicleName = jsonObject.getJSONArray("BookingUserVehicle").getJSONObject(0).getJSONObject("UserVehicle").getString("model") + " (" + jsonObject.getJSONArray("BookingUserVehicle").getJSONObject(0).getJSONObject("UserVehicle").getJSONObject("VehicleType").getString("name") + ") + " + ((jsonObject.getJSONArray("BookingUserVehicle").length()) - 1);
                    }

                } else  if (jsonObject.getJSONArray("BookingVehicleType").length() > 0)
                {
                    if (jsonObject.getJSONArray("BookingVehicleType").length() == 1) {
                        vehicleName = jsonObject.getJSONArray("BookingVehicleType").getJSONObject(0).getJSONObject("VehicleType").getString("name");
                    } else {
                        vehicleName = jsonObject.getJSONArray("BookingVehicleType").getJSONObject(0).getJSONObject("VehicleType").getString("name") + " + " + ((jsonObject.getJSONArray("BookingVehicleType").length()) - 1);
                    }
                }
                myBookingChildModal.setCar_name(vehicleName);


                myBookingChildModal.setProfile_image_url(jsonObject.getString("customer_img"));

                transactionList.add(myBookingChildModal);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }
}
