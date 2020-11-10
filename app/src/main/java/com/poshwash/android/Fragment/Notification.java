package com.poshwash.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.poshwash.android.Adapter.NotificationAdapter;
import com.poshwash.android.Beans.NotificationModal;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.DividerItemDecoration;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Notification extends Fragment {

    static Context context;
    private ImageView toggle_icon;
    private RecyclerView recylerview;
    TextView no_data_tv;
    private NotificationAdapter adapter;
    private List<NotificationModal> notificationModalList;
    private MyProgressDialog myProgressDialog;
    private SwipeRefreshLayout swipeRefresh;
    public static final String TAG = "Notification.java";

    public static Notification newInstance(Context contex) {
        Notification f = new Notification();
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
        View view = inflater.inflate(R.layout.notification, container, false);
        context = getActivity();
        initView(view);


        return view;
    }

    private void initView(View view) {
        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        toggle_icon = (ImageView) view.findViewById(R.id.toggle_icon);
        recylerview = (RecyclerView) view.findViewById(R.id.recylerview);
        no_data_tv = (TextView) view.findViewById(R.id.no_data_tv);
        notificationModalList = new ArrayList<>();
        adapter = new NotificationAdapter(context, notificationModalList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recylerview.setLayoutManager(mLayoutManager);
        recylerview.setItemAnimator(new DefaultItemAnimator());
        recylerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recylerview.setAdapter(adapter);


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                //Dynamic Data
//                callWebServiceShowNotification(false);
                if (Util.isConnectingToInternet(getActivity()))
                    try {
                        callApiNotification(CountrySelectListener.APIRequest.
                                notification_list(MySharedPreferences.getUserId(getActivity()), "1",
                                        MySharedPreferences.getLanguage(getActivity())));

                   /* callApiNotification(CountrySelectListener.APIRequest.
                            notification_list("20", "1"));*/
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                else
                    Util.errorToast(getActivity(), getActivity().getString(R.string.no_internet_connection));
                swipeRefresh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        MainActivity.notificationCountTv.setVisibility(View.GONE);
        MySharedPreferences.setNotificationCount(getActivity(), "0");
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
        if (Util.isConnectingToInternet(getActivity()))
            try {
                callApiNotification(CountrySelectListener.APIRequest.
                        notification_list(MySharedPreferences.getUserId(getActivity()), "1", MySharedPreferences.getLanguage(getActivity())));

           /* callApiNotification(CountrySelectListener.APIRequest.
                    notification_list("20", "1"));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            Util.errorToast(getActivity(), getActivity().getString(R.string.no_internet_connection));
    }

    private void callApiNotification(Map<String, Object> login) {
        Util.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.notification_list(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Util.hideProgress();
                    notificationModalList.clear();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    String message = jsonObject1.optString("message");
                    boolean status1 = jsonObject1.optBoolean("status");
                    if (status1) {
                        recylerview.setVisibility(View.VISIBLE);
                        no_data_tv.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject1.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                            NotificationModal model = new NotificationModal();
                            JSONObject jsonObject3 = jsonObject2.optJSONObject("message");
                            String name = jsonObject3.optString("customer_name");
                            model.setName(name);
                            model.setMessage(jsonObject3.optString("msg"));
                            model.setDate(jsonObject2.optString("created"));
                            notificationModalList.add(model);
                        }

                        if (notificationModalList.size() > 0) {
                            adapter = new NotificationAdapter(getActivity(), notificationModalList);
                            recylerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Util.errorToast(getActivity(), message);
                        recylerview.setVisibility(View.GONE);
                        no_data_tv.setVisibility(View.VISIBLE);
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

    public void callWebServiceShowNotification(boolean status) {
        if (status)
            myProgressDialog.show();
        Call<ResponseBody> call;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject.put("language", MySharedPreferences.getLanguage(getActivity()));
            jsonObject.put("type", "1");

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.showNotification(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (swipeRefresh.isRefreshing())
                        swipeRefresh.setRefreshing(false);
                    Util.dismissPrograssDialog(myProgressDialog);
                    try {
                        JSONObject jsonObject1 = new JSONObject(response.body().string());
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {


                        } else {
                            no_data_tv.setVisibility(View.VISIBLE);
                            recylerview.setVisibility(View.GONE);
                            //  Util.Toast(getActivity(), jsonObject1.getJSONObject("response").getString("message"));
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


}
