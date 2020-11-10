package com.poshwash.android.Activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.text.Html;
import android.view.View;

import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.TermsConditionBinding;
import com.poshwash.android.response.StaticPageResponse;

import org.json.JSONException;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anand j on 3/30/2017.
 */

public class StaticPage extends BaseActivity implements View.OnClickListener {

    private TermsConditionBinding mBinding;
    String page_slug = "";
    String page_name = "";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.terms_condition);
        initView();
        page_slug = getIntent().getStringExtra("pagetype");
        page_name = getIntent().getStringExtra("pagename");
        mBinding.txtTitle.setText(page_name);
        try {
            callApi(CountrySelectListener.APIRequest.static_page(MySharedPreferences.getUserId(this),
                    page_slug,MySharedPreferences.getLanguage(mContext)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView() {
        mBinding.ivBack.setOnClickListener(this);
        mBinding.txtTitle.setText(Util.getPageName(getIntent().getIntExtra("pagetype", -1), mContext));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void callApi(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<StaticPageResponse> call = null;
        call = myApiEndpointInterface.pages_view(login);
        call.enqueue(new Callback<StaticPageResponse>() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<StaticPageResponse> call, Response<StaticPageResponse> response) {
                try {
                    hideProgress();
                    if (response.body().getResponse().isStatus()) {
                        mBinding.txtTermCondition.setText(Html.fromHtml(response.body().getResponse().getData().get(0).getContents(),
                                Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        errorToast(mContext, response.body().getResponse().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<StaticPageResponse> call, Throwable t) {
                hideProgress();
            }
        });

    }
}


