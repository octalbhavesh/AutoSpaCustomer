package com.poshwash.android.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.poshwash.android.Adapter.PackagesAdapter;
import com.poshwash.android.Beans.PackagesData;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.ActivityPackageBinding;
import com.poshwash.android.databinding.PackagesFragmentBinding;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageActivity extends BaseActivity {
    private ActivityPackageBinding mBinding;
    private String selectedVehicle, selectedId;
    PackagesFragmentBinding binding;
    private PackagesData packagesData;
    Set<String> setDataBeansList = new HashSet<>();
    List<PackagesData.ResponseBean.DataBean> dataBeansList;
    List<PackagesData.ResponseBean.DataBean> seletedTypedDataBeansList = new ArrayList<>();
    private PackagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_package);
        Env.currentActivity = PackageActivity.this;

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.rvPackage.setLayoutManager(mLayoutManager);
        mBinding.backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        selectedVehicle = getIntent().getExtras().getString("name");
        selectedId = getIntent().getExtras().getString("id");
        if (Util.isConnectingToInternet(mContext))
            try {
                callPackageDetailsApi(CountrySelectListener.APIRequest.get_package_details(
                        MySharedPreferences.getUserId(mContext),
                        MySharedPreferences.getLanguage(mContext)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            Util.errorToast(mContext, mContext.getString(R.string.no_internet_connection));

    }

    private void callPackageDetailsApi(Map<String, Object> package_details) {
        showProgress(Env.currentActivity);
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
                        for (PackagesData.ResponseBean.DataBean dataBean : dataBeansList) {
                            setDataBeansList.add(dataBean.getVehicle_type_name());
                        }
                        setPackageSpinner();
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

    private void setPackageSpinner() {


        String selectedTextValue = selectedId;
        seletedTypedDataBeansList.clear();
        for (PackagesData.ResponseBean.DataBean data : dataBeansList) {
            if (data.getVehicle_type_id().equals(selectedTextValue)&& data.getIs_purchase()==2) {
                Intent i=new Intent(PackageActivity.this,RequestActivity.class);
                startActivity(i);
            }else if(data.getVehicle_type_id().equals(selectedTextValue)&& data.getIs_purchase()==1){
                seletedTypedDataBeansList.add(data);
            }
        }
        setPackagesData();
    }

    private void setPackagesData() {


        if (seletedTypedDataBeansList.size() > 0) {
            mBinding.rvPackage.setVisibility(View.VISIBLE);
            mBinding.noDataTv.setVisibility(View.GONE);


            adapter = new PackagesAdapter(seletedTypedDataBeansList, mContext, selectedId);
            mBinding.rvPackage.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } else {
            mBinding.rvPackage.setVisibility(View.GONE);
            mBinding.noDataTv.setVisibility(View.VISIBLE);
        }
    }
}
