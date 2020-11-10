package com.poshwash.android.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.poshwash.android.Beans.CardModal;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONObject;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by anand jain on 3/20/2017.
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private List<CardModal> moviesList;
    DisplayImageOptions options;
    Context context;
    CardAdapter notification;
    MyProgressDialog myProgressDialog;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public CircleImageView circleimageview;
        public TextView tvCardNumber, tvCardHolderName, tvCardType;
        public ImageView ivDelete;
        //  FrameLayout frameDelete;
        // SwipeRevealLayout swipeRevealLayout;

        public MyViewHolder(View view) {
            super(view);
            tvCardNumber = (TextView) view.findViewById(R.id.card_number);
            tvCardHolderName = (TextView) view.findViewById(R.id.card_holder_name);
            tvCardType = (TextView) view.findViewById(R.id.card_type);
            ivDelete = (ImageView) view.findViewById(R.id.iv_card_delete);

        }


        @Override
        public void onClick(View view) {

        }
    }

    public CardAdapter(Context context, List<CardModal> moviesList) {
        this.moviesList = moviesList;
        this.notification = notification;
        this.context = context;
        myProgressDialog = new MyProgressDialog(this.context);
        myProgressDialog.setCancelable(false);


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_card, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.tvCardHolderName.setText(moviesList.get(position).getCard_user_name());
        holder.tvCardType.setText(moviesList.get(position).getCard_name());

        String number = moviesList.get(position).getCard_number().replace(" ", "").replaceAll("\\w(?=\\w{4})", "*");
        holder.tvCardNumber.setText(number);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDelete(moviesList.get(position).getId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moviesList.size(); //Dynamic Data moviesList.size();
    }


    private void dialogDelete(final String cardId, final int position) {
        final Dialog dialog;
        dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_card_delete);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();

        TextView dialog_yes = (TextView) dialog.findViewById(R.id.dialog_yes);
        TextView dialog_no = (TextView) dialog.findViewById(R.id.dialog_no);

        dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeCardItem(cardId,position);
                dialog.dismiss();
            }
        });

        dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void removeCardItem(final String pos, final int position) {
        Util.showProgress(context);
        Call<ResponseBody> call = null;
        JSONObject jsonObject = null;
        String ss = MySharedPreferences.getLanguage(context);
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("card_id", pos);
            jsonObject.put("language", ss);

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.delete_card_details(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.hideProgress();
                    try {
                        JSONObject jsonObject1 = new JSONObject(Util.convertRetrofitResponce(response));
                        if (jsonObject1.getJSONObject("response").getBoolean("status")) {
                            Util.Toast(context, jsonObject1.getJSONObject("response").getString("message"));
                            moviesList.remove(position);
                            notifyItemChanged(position);
                        } else {
                            Util.Toast(context, jsonObject1.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.hideProgress();
                }
            });
        } catch (Exception e) {

            Util.hideProgress();
        }
    }
}
