package com.poshwash.android.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.poshwash.android.Activity.RequestActivity;
import com.poshwash.android.Beans.PackagesData;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.poshwash.android.Utils.Util.hideProgress;
import static com.poshwash.android.Utils.Util.showProgress;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.Holder> {
    List<PackagesData.ResponseBean.DataBean> list;
    Context context;
    String selectedVehicle;

    public PackagesAdapter(List<PackagesData.ResponseBean.DataBean> dataBeansList, Context context,
                           String selectedVehicle) {
        this.list = dataBeansList;
        this.context = context;
        this.selectedVehicle = selectedVehicle;
    }

    @NonNull
    @Override
    public PackagesAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.packages_row, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(PackagesAdapter.Holder holder, final int position) {
        String package_name = list.get(position).getName();
        holder.txtCarName.setText(package_name);

        holder.txtDes.setText(list.get(position).getDescription());

        if (TextUtils.isEmpty(list.get(position).getRefer_amount())) {
            holder.layoutDiscount.setVisibility(View.GONE);
            holder.txtAmountDiscount.setText("$" + list.get(position).getAmount());
        } else {
            holder.layoutDiscount.setVisibility(View.VISIBLE);
            holder.txtAmount.setText("$" + list.get(position).getAmount());
            holder.txtAmountDiscount.setText("$" + list.get(position).getRefer_amount());

        }

        holder.layoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String referamount = list.get(position).getRefer_amount();
                String amount = list.get(position).getAmount();
                if (referamount.isEmpty() || referamount == null) {
                    dialogPaymentType(selectedVehicle, list.get(position).getId(),
                            amount);
                } else {
                    dialogPaymentType(selectedVehicle, list.get(position).getId(),
                            referamount);
                }

                Log.e("vehicle_id", selectedVehicle);

            }
        });

    }

    private void dialogPaymentType(final String vehicle_type_id,
                                   final String id, final String amount) {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_payment_selector);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();

        TextView dialog_card = (TextView) dialog.findViewById(R.id.dialog_card);
        TextView dialog_wallet = (TextView) dialog.findViewById(R.id.dialog_wallet);
        TextView dialog_cancel = (TextView) dialog.findViewById(R.id.dialog_cancel);

        dialog_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                    callPurchasePackage(CountrySelectListener.APIRequest.purchase_package
                            (MySharedPreferences.getUserId(context), vehicle_type_id, id,
                                    amount, "3", MySharedPreferences.getLanguage(context)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView txtCarName, txtAmount, txtAmountDiscount, txtDes;
        LinearLayout layoutMain, txtReNew;
        RelativeLayout layoutDiscount;

        public Holder(View itemView) {
            super(itemView);
            layoutMain = itemView.findViewById(R.id.layout_main1);
            txtCarName = itemView.findViewById(R.id.txtPackageName);
            txtDes = itemView.findViewById(R.id.txtDescription);
            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtAmountDiscount = itemView.findViewById(R.id.txtAmountDiscount);
            layoutDiscount = itemView.findViewById(R.id.layout_discount);

        }
    }

    private void callPurchasePackage(Map<String, Object> package_details) {
        showProgress(Env.currentActivity);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.purchase_package(package_details);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    hideProgress();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    boolean status1 = jsonObject1.optBoolean("status");
                    String message1 = jsonObject1.optString("message");
                    if (status1) {
                        Util.sucessToast(context, message1);
                        Intent i = new Intent(context, RequestActivity.class);
                        context.startActivity(i);
                    } else {
                        Util.errorToast(context, message1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgress();
            }
        });
    }
}