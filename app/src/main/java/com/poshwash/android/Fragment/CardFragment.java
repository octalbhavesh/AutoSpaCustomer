package com.poshwash.android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.poshwash.android.Activity.AddCardInfoActivity;
import com.poshwash.android.Adapter.CardAdapter;
import com.poshwash.android.Beans.CardModal;
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


public class CardFragment extends Fragment {
    static Context context;
    private RecyclerView rvList;
    TextView no_data_tv;
    private CardAdapter adapter;
    private ArrayList<CardModal> cardModalList = new ArrayList<>();
    private MyProgressDialog myProgressDialog;
    public static final String TAG = "CardFragment.java";
    private Button btnAdd;

    public static CardFragment newInstance(Context contex) {
        CardFragment f = new CardFragment();
        context = contex;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_card, container, false);
        context = getActivity();
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        rvList = (RecyclerView) rootView.findViewById(R.id.rv_card);
        no_data_tv = (TextView) rootView.findViewById(R.id.no_data_tv);
        btnAdd = (Button) rootView.findViewById(R.id.btn_add);
        adapter = new CardAdapter(context, cardModalList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvList.setLayoutManager(mLayoutManager);
        rvList.setItemAnimator(new DefaultItemAnimator());
        rvList.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        rvList.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddCardInfoActivity.class));
            }
        });

        if (Util.isConnectingToInternet(getActivity()))
            try {
                callApiCardInfo(CountrySelectListener.APIRequest.
                        card_list(MySharedPreferences.getUserId(getActivity()),
                                MySharedPreferences.getLanguage(getActivity())));

                   /* callApiNotification(CountrySelectListener.APIRequest.
                            notification_list("20", "1"));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    private void callApiCardInfo(Map<String, Object> login) {
        Util.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.get_card_details(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Util.hideProgress();
                    cardModalList.clear();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    String message = jsonObject1.optString("message");
                    boolean status1 = jsonObject1.optBoolean("status");
                    if (status1) {
                        rvList.setVisibility(View.VISIBLE);
                        no_data_tv.setVisibility(View.GONE);
                        JSONArray jsonArray = jsonObject1.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            CardModal model = new CardModal();
                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                            JSONObject jsonObject3 = jsonObject2.optJSONObject("CardDetail");
                            String id = jsonObject3.optString("id");
                            model.setId(id);
                            String card_name = jsonObject3.optString("card_type");
                            model.setCard_name(card_name);
                            String holder_name = jsonObject3.optString("card_holder_name");
                            model.setCard_user_name(holder_name);
                            String card_number = jsonObject3.optString("card_number");
                            model.setCard_number(card_number);
                            String card_month = jsonObject3.optString("month");
                            model.setCard_month(card_month);
                            String card_year = jsonObject3.optString("year");
                            model.setCard_year(card_year);
                            cardModalList.add(model);
                        }

                        if (cardModalList.size() > 0) {
                            adapter = new CardAdapter(getActivity(), cardModalList);
                            rvList.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Util.errorToast(getActivity(), message);
                        rvList.setVisibility(View.GONE);
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
}
