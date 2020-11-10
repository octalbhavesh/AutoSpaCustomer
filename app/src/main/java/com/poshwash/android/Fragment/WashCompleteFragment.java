package com.poshwash.android.Fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.AutoSpaConstant;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;

import org.json.JSONObject;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by abhinava on 3/15/2017.
 */

public class WashCompleteFragment extends Fragment implements View.OnClickListener {


    public static DisplayImageOptions options;
    static Context context;
    View rootview;
    MyProgressDialog progressDialog;


    public static Object image_obj, booking_id_obj;
    String booking_id = "";
    String Booking_sp_id = "";
    public Bundle bundle = new Bundle();
    String rate = "";
    String amount = "";
    String profile_image = "";
    String service_type = "";
    private CardView actionbar;
    private ImageView toggleIcon;
    private ImageView imageIv;
    private RelativeLayout rateLayout;
    private CircleImageView driverPicIv;
    private TextView driverNameTv;
    private RatingBar driverRateBar;
    private TextView completeRatingTv;


    public static WashCompleteFragment newInstance(Context contex, Object image_ob, Object booking_id_ob) {
        WashCompleteFragment f = new WashCompleteFragment();
        context = contex;
        image_obj = image_ob;
        booking_id_obj = booking_id_ob;
        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootview == null) {
            rootview = inflater.inflate(R.layout.wash_complete_and_review, container, false);
        }
        initView(rootview);
        context = getActivity();
        hideSoftKeyboard();
        onLoad();
        SetFont();
        setonClick();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject((String) image_obj);
            booking_id = jsonObject.getString("booking_id");
            Booking_sp_id = jsonObject.getString("Booking_sp_id");

            try {
                service_type = jsonObject.getString("service_type");
            } catch (Exception e) {
            }
            driverNameTv.setText(jsonObject.getString("sp_name"));
            try {
                if (jsonObject.getString("sp_profile") != null) {
                    profile_image = jsonObject.getString("sp_profile");
                    ImageLoader.getInstance().displayImage(jsonObject.getString("sp_profile"), driverPicIv, options);
                } else {
                    driverPicIv.setImageResource(R.drawable.user);
                }
            } catch (Exception e) {
                driverPicIv.setImageResource(R.drawable.user);
            }
        } catch (Exception e) {

        }


        return rootview;
    }

    private void onLoad() {

        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.defalut_bg);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageForEmptyUri(new BitmapDrawable(context.getResources(), default_bitmap))
                .showImageOnFail(new BitmapDrawable(context.getResources(), default_bitmap))
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        progressDialog = new MyProgressDialog(context);
        progressDialog.setCancelable(false);

    }

    private void SetFont() {
    }

    private void setonClick() {
        toggleIcon.setOnClickListener(this);
        completeRatingTv.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        if (v == toggleIcon) {
            ((MainActivity) context).OpenDrawer();
        }

        if (v == completeRatingTv) {
            rate = driverRateBar.getRating() + "";
            if (driverRateBar.getRating() > 1) {

                callWebserviceSendRating();

            } else {
                Util.Toast(context, "Please rate at least 1");
            }
        }
    }

    private void showRatingBar() {
        MySharedPreferences.setRatingStatus(context, true);
    }

    private void callWebserviceSendRating() {
        progressDialog.show();
        JSONObject jsonObject = null;
        HashMap<String, RequestBody> map = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            map = new HashMap<>();
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("rate", rate);
            jsonObject.put("Booking_sp_id", Booking_sp_id);

            MySharedPreferences.setRatingStatus(context, false);

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.ratings(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(response.body().string());
                        Log.e("tg", "response from server = " + mJsonObj.toString());
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                            ((MainActivity) context).displayView(AutoSpaConstant.HOMEFRAGMENT, null, null);

                        } else {
                            Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                        }

                    } catch (Exception e) {
                        Log.e("", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Util.dismissPrograssDialog(progressDialog);
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Util.dismissPrograssDialog(progressDialog);
            Log.e("", e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }

    public void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }


    private void initView(View rootview) {
        actionbar = (CardView) rootview.findViewById(R.id.actionbar);
        toggleIcon = (ImageView) rootview.findViewById(R.id.toggle_icon);
        imageIv = (ImageView) rootview.findViewById(R.id.image_iv);
        rateLayout = (RelativeLayout) rootview.findViewById(R.id.rateLayout);
        driverPicIv = (CircleImageView) rootview.findViewById(R.id.driverPicIv);
        driverNameTv = (TextView) rootview.findViewById(R.id.driverNameTv);
        driverRateBar = (RatingBar) rootview.findViewById(R.id.driverRateBar);
        completeRatingTv = (TextView) rootview.findViewById(R.id.completeRatingTv);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                Drawable progressDrawable = driverRateBar.getProgressDrawable();
                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable, ContextCompat.getColor(getActivity(), R.color.whitecolor));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            try {
                Drawable progressDrawable = driverRateBar.getProgressDrawable();
                if (progressDrawable != null) {
                    DrawableCompat.setTint(progressDrawable, ContextCompat.getColor(getActivity(), R.color.whitecolor));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}

