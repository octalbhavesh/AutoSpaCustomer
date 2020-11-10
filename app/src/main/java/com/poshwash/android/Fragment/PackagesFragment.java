package com.poshwash.android.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Adapter.PackagesAdapter;
import com.poshwash.android.Beans.PackagesData;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.PackagesFragmentBinding;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.poshwash.android.Utils.Util.errorToast;
import static com.poshwash.android.Utils.Util.hideProgress;
import static com.poshwash.android.Utils.Util.showProgress;

public class PackagesFragment extends Fragment {
    private final String TAG = getClass().getName();
    PackagesFragmentBinding binding;
    private PackagesData packagesData;
    Set<String> setDataBeansList = new HashSet<>();
    List<PackagesData.ResponseBean.DataBean> dataBeansList;
    List<PackagesData.ResponseBean.DataBean> seletedTypedDataBeansList = new ArrayList<>();
    private PackagesAdapter adapter;

    public static PackagesFragment newInstance(Context contex) {
        return new PackagesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.packages_fragment, container, false);
        ((MainActivity) Objects.requireNonNull(getActivity())).setPackagesFragment();
        Env.currentActivity = getActivity();

        if (Util.isConnectingToInternet(getActivity()))
            try {
                callPackageDetailsApi(CountrySelectListener.APIRequest.get_package_details(
                        MySharedPreferences.getUserId(getActivity()), MySharedPreferences.getLanguage(getActivity())));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        else
            Util.errorToast(getActivity(), getActivity().getString(R.string.no_internet_connection));
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Env.currentActivity = getActivity();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(Env.currentActivity), Env.currentActivity);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.recyclerview.setLayoutManager(mLayoutManager);
    }

    /*APIs*/
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Env.currentActivity,
                R.layout.spinner_row, new ArrayList<String>(setDataBeansList));
        binding.spinnerPackage.setAdapter(adapter);

        binding.spinnerPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedTextValue = adapterView.getItemAtPosition(position).toString();
                seletedTypedDataBeansList.clear();
                for (PackagesData.ResponseBean.DataBean data : dataBeansList) {
                    if (data.getVehicle_type_name().equals(selectedTextValue)) {
                        seletedTypedDataBeansList.add(data);
                    }
                }
                setPackagesData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setPackagesData() {
        if (seletedTypedDataBeansList.size() > 0) {
            binding.recyclerview.setVisibility(View.VISIBLE);
            binding.noDataTv.setVisibility(View.GONE);


          /*  adapter = new PackagesAdapter(seletedTypedDataBeansList, getActivity(), selectedVehicle);
            binding.recyclerview.setAdapter(adapter);
            adapter.notifyDataSetChanged();*/

        } else {
            binding.recyclerview.setVisibility(View.GONE);
            binding.noDataTv.setVisibility(View.VISIBLE);
        }
    }


}