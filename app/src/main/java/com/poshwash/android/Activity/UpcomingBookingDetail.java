package com.poshwash.android.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Adapter.GetfarePromosAdapter;
import com.poshwash.android.Adapter.GetfareReviewAdapter;
import com.poshwash.android.Adapter.VehiclePagerAdapter;
import com.poshwash.android.Beans.BeanCars;
import com.poshwash.android.Beans.BeanReviewData;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.R;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.customViews.CustomTexViewBold;
import com.poshwash.android.customViews.CustomTexViewRegular;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by abhinava on 2/28/2018.
 */

public class UpcomingBookingDetail extends AppCompatActivity implements View.OnClickListener {
    Context context;
    private ImageView backIcon;
    private CustomTexViewRegular dateTimeTv;
    private CustomTexViewRegular statusTv;
    private CircleImageView userImageIv;
    private CustomTexViewRegular userNameTv;
    private CustomTexViewRegular mobileNumberTv;
    private CustomTexViewBold amountTv;
    private CustomTexViewRegular locationTv;
    private CustomTexViewRegular paymentModeTv;
    private CustomTexViewRegular getFareTv;
    ViewPager pager;
    ArrayList<BeanCars> vehicleList;
    VehiclePagerAdapter vehiclePagerAdapter;
    CirclePageIndicator circlePageIndicator;
    DisplayImageOptions options;
    MyProgressDialog progressDialog;

    private LinearLayout txnLl;
    private CustomTexViewRegular transactionIdTv;
    private CustomTexViewRegular reasonTv;
    private LinearLayout jobratingLl;
    private MaterialRatingBar ratingBar;
    private MaterialRatingBar user_rating_bar;
    private CustomTexViewRegular jobRatingTv;
    private LinearLayout jobDurationLl;
    private LinearLayout cancelLl;
    private LinearLayout detailLL;
    private CustomTexViewRegular jobDurationTv;
    JSONObject jsonObject = new JSONObject();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_detail);
        context = this;
        /*initView();*/
        try {
            jsonObject = new JSONObject((String) getIntent().getExtras().getString("myBookingChildModal"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setData();
    }

   /* private void initView() {
        backIcon = (ImageView) findViewById(R.id.backIcon);
        pager = (ViewPager) findViewById(R.id.pager);
        dateTimeTv = (CustomTexViewRegular) findViewById(R.id.dateTimeTv);
        statusTv = (CustomTexViewRegular) findViewById(R.id.statusTv);
        userImageIv = (CircleImageView) findViewById(R.id.userImageIv);
        userNameTv = (CustomTexViewRegular) findViewById(R.id.userNameTv);
        mobileNumberTv = (CustomTexViewRegular) findViewById(R.id.mobileNumberTv);
        amountTv = (CustomTexViewBold) findViewById(R.id.amountTv);
        locationTv = (CustomTexViewRegular) findViewById(R.id.locationTv);
        paymentModeTv = (CustomTexViewRegular) findViewById(R.id.paymentModeTv);
        getFareTv = (CustomTexViewRegular) findViewById(R.id.getFareTv);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.mIndicator);

        txnLl = (LinearLayout) findViewById(R.id.txnLl);
        detailLL = (LinearLayout) findViewById(R.id.detailLL);
        transactionIdTv = (CustomTexViewRegular) findViewById(R.id.transactionIdTv);
        reasonTv = (CustomTexViewRegular) findViewById(R.id.reasonTv);
        jobratingLl = (LinearLayout) findViewById(R.id.jobratingLl);
        ratingBar = (MaterialRatingBar) findViewById(R.id.rating_bar);
        user_rating_bar = (MaterialRatingBar) findViewById(R.id.user_rating_bar);
        jobRatingTv = (CustomTexViewRegular) findViewById(R.id.jobRatingTv);
        jobDurationLl = (LinearLayout) findViewById(R.id.jobDurationLl);
        cancelLl = (LinearLayout) findViewById(R.id.cancelLl);
        jobDurationTv = (CustomTexViewRegular) findViewById(R.id.jobDurationTv);

        vehicleList = new ArrayList<>();

        Bitmap default_bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.user);
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

        vehiclePagerAdapter = new VehiclePagerAdapter(this, vehicleList);
        pager.setAdapter(vehiclePagerAdapter);
        circlePageIndicator.setViewPager(pager);
        clickListner();
    }*/

    private void clickListner() {
        backIcon.setOnClickListener(this);
        getFareTv.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        try {
            detailLL.setVisibility(View.VISIBLE);

            dateTimeTv.setText(Util.convertUtcTimeToLocal(jsonObject.getString("booking_date") + " " + jsonObject.getString("booking_time"), false, context));
            userNameTv.setText(jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
            user_rating_bar.setVisibility(View.GONE);
            locationTv.setText(jsonObject.getString("booking_address"));
            if (jsonObject.getString("payment_type").equals("1")) {
                paymentModeTv.setText(getString(R.string.cash));
            } else {
                paymentModeTv.setText(getString(R.string.span_payment));
            }
            amountTv.setText("" + jsonObject.getString("booking_amount"));
            // 0-> initiated 1->scheduled 2->completed 3->cancelled 4->refunded 5->start wash,6=>Cancel By Service Provider,7=>Cancel By Admin,8=>Cancel By Customer
            statusTv.setText(getString(R.string.pending));
            mobileNumberTv.setText(jsonObject.getString("mobile"));

            if (!jsonObject.getString("customer_img").equals("")) {
                ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + jsonObject.getString("customer_img"), userImageIv, options);
            } else {
                userImageIv.setImageResource(R.drawable.defalut_bg);
            }
         //   setVehicleData(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*private void setVehicleData(JSONObject jsonObject) {
        vehicleList.clear();
        try {
            for (int i = 0; i < jsonObject.getJSONArray("BookingUserVehicle").length(); i++) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1 = jsonObject.getJSONArray("BookingUserVehicle").getJSONObject(i);
                BeanCars beanCars = new BeanCars();
                beanCars.setCar_name(jsonObject1.getJSONObject("UserVehicle").getString("model") + " (" + jsonObject1.getJSONObject("UserVehicle").getJSONObject("VehicleType").getString("name") + ")");
                beanCars.setCar_id(jsonObject1.getString("id"));
                beanCars.setCar_address(jsonObject1.getJSONObject("UserVehicle").getString("plate_number"));
                beanCars.setCar_color(jsonObject1.getJSONObject("UserVehicle").getString("color"));
                beanCars.setCar_image(jsonObject1.getJSONObject("UserVehicle").getString("vehicle_picture"));
                vehicleList.add(beanCars);
            }

            for (int i = 0; i < jsonObject.getJSONArray("BookingVehicleType").length(); i++) {
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1 = jsonObject.getJSONArray("BookingVehicleType").getJSONObject(i);
                BeanCars beanCars = new BeanCars();
                beanCars.setCar_id(jsonObject1.getJSONObject("VehicleType").getString("id"));
                beanCars.setCar_name(jsonObject1.getJSONObject("VehicleType").getString("name"));
                vehicleList.add(beanCars);
            }
            vehiclePagerAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backIcon:
                finish();
                break;
           /* case R.id.getFareTv:
                try {
                    getFareReview(jsonObject.getString("fair_review"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
               /* break;*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
    }

    @SuppressLint("SetTextI18n")
    public void getFareReview(String reviewData) {
        try {
            JSONObject jsonObject = new JSONObject(reviewData);
            ArrayList<BeanReviewData> review_list = new ArrayList<>();
            review_list.clear();
            review_list = CalculatefareReviewData(jsonObject);
            final Dialog dialog;
            GetfareReviewAdapter getfareReviewAdapter;
            GetfarePromosAdapter getfarePromosAdapter;

            dialog = new Dialog(context);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.getfare_layout);
            ImageView back_icon = (ImageView) dialog.findViewById(R.id.back_icon);
            if (MySharedPreferences.getLanguage(context).equalsIgnoreCase("eng")) {
                back_icon.setRotation(0);
            } else {
                back_icon.setRotation(180);
            }
            RecyclerView recylerview = (RecyclerView) dialog.findViewById(R.id.recylerview);
            RecyclerView promoRecylerview = (RecyclerView) dialog.findViewById(R.id.promoRecylerview);
            TextView paid_amount = (TextView) dialog.findViewById(R.id.paid_amount);
            TextView total_amount = (TextView) dialog.findViewById(R.id.total_amount);

            paid_amount.setText("" + Util.DisplayAmount(jsonObject.getString("paidAmount")));
            total_amount.setText("" + Util.DisplayAmount(jsonObject.getString("totalAmount")));

            getfareReviewAdapter = new GetfareReviewAdapter(context, review_list);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recylerview.setLayoutManager(mLayoutManager);
            recylerview.setItemAnimator(new DefaultItemAnimator());
            recylerview.setAdapter(getfareReviewAdapter);

            getfarePromosAdapter = new GetfarePromosAdapter(context, jsonObject.getJSONArray("promoCodes"));
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
            promoRecylerview.setLayoutManager(mLayoutManager1);
            promoRecylerview.setItemAnimator(new DefaultItemAnimator());
            promoRecylerview.setAdapter(getfarePromosAdapter);

            back_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });

            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<BeanReviewData> CalculatefareReviewData(JSONObject jsonObject) {
        ArrayList<BeanReviewData> beanReviewDataArrayList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonObject.getJSONArray("vehicleData").length(); i++) {
                BeanReviewData beanReviewData = new BeanReviewData();
                beanReviewData.setRequest_type(jsonObject.getString("request_type"));
                beanReviewData.setRequest_type_amount(jsonObject.getString("request_type_amount"));
                beanReviewData.setVehicle_id(jsonObject.getJSONArray("vehicleData").getJSONObject(i).getString("vehicle_id"));
                beanReviewData.setVehicle_name(jsonObject.getJSONArray("vehicleData").getJSONObject(i).getString("vehicle_name"));
                beanReviewData.setVehicle_amount(jsonObject.getJSONArray("vehicleData").getJSONObject(i).getString("vehicle_amount"));
                beanReviewDataArrayList.add(beanReviewData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beanReviewDataArrayList;
    }
}