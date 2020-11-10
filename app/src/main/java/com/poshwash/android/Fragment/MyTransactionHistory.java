package com.poshwash.android.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Adapter.TransactionAdapter;
import com.poshwash.android.Beans.TransactionModel;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.DividerItemDecoration;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.response.TransactionResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MyTransactionHistory extends Fragment implements PaymentMethodNonceCreatedListener {

    private ImageView toggle_icon;
    private RecyclerView recyclerview;
    private Button btnSubmit;
    private TextView no_data_tv, tvBalance;
    private TransactionAdapter adapter;
    private MyProgressDialog myProgressDialog;
    private final String TAG = "myProgressDialog.java";
    private ArrayList<TransactionModel> transactionModalList;
    Context context;
    private EditText etAmount;
    private int BRAINTREE_REQUEST_CODE = 4949;
    private long btnNext = 0;
    private String token;

    public static MyTransactionHistory newInstance(Context contex) {
        MyTransactionHistory f = new MyTransactionHistory();
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
        context = getActivity();
        initView(view);
        ((MainActivity) getActivity()).setTransactionHistoryFragment();
        return view;
    }

    private void initView(View view) {
        tvBalance = (TextView) view.findViewById(R.id.tv_balance);
        etAmount = (EditText) view.findViewById(R.id.et_amount);
        btnSubmit = (Button) view.findViewById(R.id.btn_submit);
        toggle_icon = (ImageView) view.findViewById(R.id.toggle_icon);
        no_data_tv = (TextView) view.findViewById(R.id.no_data_tv);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        transactionModalList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        if (Util.isConnectingToInternet(getActivity()))
            try {
                callApiTransaction(CountrySelectListener.APIRequest.
                        notification_list(MySharedPreferences.getUserId(getActivity()), "1",
                                MySharedPreferences.getLanguage(getActivity())));

           /* callApiTransaction(CountrySelectListener.APIRequest.
                    notification_list("20", "1"));*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            Util.errorToast(getActivity(), getActivity().getString(R.string.no_internet_connection));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - btnNext < 1000) {
                    return;
                }
                btnNext = SystemClock.elapsedRealtime();
                if (TextUtils.isEmpty(etAmount.getText().toString().trim())) {
                    Util.errorToast(getActivity(), "Enter amount");
                } else {
                    fetchToken();
                }
            }
        });

    }

    private void fetchToken() {
        try {
            callApiToken(CountrySelectListener.APIRequest.fetch_token
                    (MySharedPreferences.getUserId(getActivity()),
                            MySharedPreferences.getLanguage(getActivity())));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callApiToken(Map<String, Object> categories) {
        Util.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.generate_braintree_token(categories);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Util.hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    boolean statusCode = jsonObject1.optBoolean("status");
                    String message = jsonObject1.optString("message");
                    if (statusCode) {
                        token = jsonObject1.optString("data");

                       /* try {
                            BraintreeFragment m = BraintreeFragment.newInstance(getActivity(), token);
                            CardBuilder cardBuilder = new CardBuilder()
                                    .cardNumber("4242424242424242")
                                    .expirationDate("06/2023");

                            Card.tokenize(m, cardBuilder);



                        } catch (InvalidArgumentException e) {
                            e.printStackTrace();
                        }*/
                         callPayPal(token);

                    } else {
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


    private void callPayPal(String token) {


        DropInRequest dropInRequest = new DropInRequest()
                .amount(etAmount.getText().toString().trim())
                .clientToken(token)
                .disablePayPal()
                .collectDeviceData(true)//for card and paypal data  save
                .androidPayPhoneNumberRequired(true)
                .androidPayShippingAddressRequired(true);


        startActivityForResult(dropInRequest.getIntent(getActivity()), BRAINTREE_REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BRAINTREE_REQUEST_CODE) {

            DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);


            Log.e("payment_nonce", result.getPaymentMethodNonce().getNonce());
            try {
                //send to your server
                callNonceSendApi(result.getPaymentMethodNonce().getNonce());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }


        // Log.e("payment_status", "Testing the app here")
    }

    private void callNonceSendApi(String nonce) {

        try {
            callApiNonce(CountrySelectListener.APIRequest.
                    send_nonce(MySharedPreferences.getUserId(getActivity()),
                            nonce
                            , etAmount.getText().toString().trim(),
                            MySharedPreferences.getLanguage(getActivity())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void callApiNonce(Map<String, Object> notification) {
        Util.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.add_wallet_amount(notification);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Util.hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");

                    boolean statusCode = jsonObject1.optBoolean("status");
                    String message = jsonObject1.optString("message");
                    if (statusCode) {
                        etAmount.setText("");
                       /* JSONObject jsonObject1 = jsonObject.optJSONObject("data");
                        String wallet = jsonObject1.optString("wallet");*/

                        try {
                            callApiTransaction(CountrySelectListener.APIRequest.
                                    notification_list(MySharedPreferences.getUserId(getActivity()), "1", MySharedPreferences.getLanguage(getActivity())));

                          /*  callApiTransaction(CountrySelectListener.APIRequest.
                                    notification_list("20", "1"));*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
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
    public void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }

    private void callApiTransaction(Map<String, Object> login) {
        Util.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<TransactionResponse> call = null;
        call = myApiEndpointInterface.transaction_list(login);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                try {
                    Util.hideProgress();
                    transactionModalList.clear();
                    if (response.body().getResponse().isStatus()) {
                        // Util.sucessToast(getActivity(), response.body().getResponse().getMessage());
                        if (response.body().getResponse().getData().size() == 0) {
                            no_data_tv.setVisibility(View.VISIBLE);
                            recyclerview.setVisibility(View.GONE);
                            tvBalance.setText("Available balance: " + "$" + "0");
                        } else {
                            no_data_tv.setVisibility(View.GONE);
                            recyclerview.setVisibility(View.VISIBLE);
                            tvBalance.setText("Available balance: " + "$" + response.body().getResponse().getWallet());
                            for (int i = 0; i < response.body().getResponse().getData().size(); i++) {
                                TransactionModel model = new TransactionModel();
                                String trans_id = response.body().getResponse().getData().get(i).getTransaction_id();
                                model.setTransaction_id(trans_id);
                                String amount = response.body().getResponse().getData().get(i).getAmount();
                                model.setAmount(amount);
                                String id = response.body().getResponse().getData().get(i).getId();
                                model.setId(id);
                                String date = response.body().getResponse().getData().get(i).getCreated();
                                model.setDate(date);
                                String payment_status = response.body().getResponse().getData().get(i).getPayment_status();
                                model.setPayment_status(payment_status);
                              /*  String payment_type = response.body().getResponse().getData().get(i);
                                model.setPayment_type(payment_type);*/
                                transactionModalList.add(model);
                            }
                            adapter = new TransactionAdapter(context, transactionModalList);
                            recyclerview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                    } else {
                        Util.errorToast(getActivity(), response.body().getResponse().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                Util.hideProgress();
            }
        });

    }

    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        String ss = paymentMethodNonce.getNonce();
        Log.e("new_nonce", ss);
    }
}
