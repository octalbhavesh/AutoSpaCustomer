
package com.poshwash.android.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.AutoSpaConstant;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.Delegate.NavigationDelegate;
import com.poshwash.android.Fragment.CardFragment;
import com.poshwash.android.Fragment.FreeWashFragment;
import com.poshwash.android.Fragment.HomeFragment;
import com.poshwash.android.Fragment.MyBooking;
import com.poshwash.android.Fragment.MyProfile;
import com.poshwash.android.Fragment.MyTransactionHistory;
import com.poshwash.android.Fragment.Notification;
import com.poshwash.android.Fragment.PackagesFragment;
import com.poshwash.android.Fragment.PastBooking;
import com.poshwash.android.Fragment.Settings;
import com.poshwash.android.Fragment.UpcomingBookings;
import com.poshwash.android.Fragment.VechicleList;
import com.poshwash.android.Fragment.WashCompleteFragment;
import com.poshwash.android.R;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.LocationService;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.response.LoginResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationDelegate, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    DrawerLayout drawer;
    View nav_view;
    View container;
    public static Context context;
    LinearLayout nav_home, nav_card, nav_free_wash, nav_manage_vehicle, nav_booking_vehicle, nav_pastBooking, nav_upcommingBooking, nav_transaction_history, nav_packages, nav_notification, nav_settings, nav_logout;
    TextView card_menu, home_menu, free_wash_menu, manage_vehicle_menu, booking_vehicle_menu, pastBooking_menu, upcommingBooking_menu, transaction_history_menu, packages_menu, notification_menu, settings_menu, logout_menu;
    static SwitchCompat notification_switch;
    private ImageView img;
    public static TextView user_profile_name_tv, welcome_tv, notificationCountTv;
    static CircleImageView user_profile_pic_iv;
    Toolbar toolbar;
    public FragmentManager mFragmentManager = null;
    FragmentTransaction fragmentTransaction = null;
    public NavigationDelegate mNavigationDelegate;
    Fragment mFragment = null;
    FrameLayout frameLayout;
    public static DisplayImageOptions options;
    MyProgressDialog progressDialog;
    public static FragmentManager fragmentManager;
    public GoogleApiClient googleApiClient;

    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        fragmentManager = getSupportFragmentManager();
        mNavigationDelegate = (NavigationDelegate) this;
        AutoSpaApplication.recent_activity = MainActivity.this;


        context = this;
        findViewByID();
        setOnClick();
        SetProfile();
        autoOnGPS();


        if (getIntent().getIntExtra("fragmentNumber", 0) == 1) {
            displayView(AutoSpaConstant.WASHPRODUCTsORDERLIST, null, null);
        } else {
            displayView(AutoSpaConstant.HOMEFRAGMENT, null, null);
        }
    }

    public void findViewByID() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        frameLayout = (FrameLayout) findViewById(R.id.mainFrame);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        container = findViewById(R.id.container);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setElevation(0);

        Util.hideKeyboard(context, container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerClosed(View view) {

                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {

                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                toolbar.setTranslationX(slideOffset * drawerView.getWidth());
                frameLayout.setTranslationX(slideOffset * drawerView.getWidth());
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);

        nav_card = (LinearLayout) headerView.findViewById(R.id.nav_card);
        nav_manage_vehicle = (LinearLayout) headerView.findViewById(R.id.nav_manage_vehicle);
        nav_free_wash = (LinearLayout) headerView.findViewById(R.id.nav_free_wash);
        nav_booking_vehicle = (LinearLayout) headerView.findViewById(R.id.nav_booking_vehicle);
        nav_transaction_history = (LinearLayout) headerView.findViewById(R.id.nav_transaction_history);
        nav_packages = (LinearLayout) headerView.findViewById(R.id.nav_packages);
        nav_settings = (LinearLayout) headerView.findViewById(R.id.nav_settings);
        nav_logout = (LinearLayout) headerView.findViewById(R.id.nav_logout);
        nav_home = (LinearLayout) headerView.findViewById(R.id.nav_home);
        nav_notification = (LinearLayout) headerView.findViewById(R.id.nav_notification);
        nav_pastBooking = (LinearLayout) headerView.findViewById(R.id.nav_pastBooking);
        nav_upcommingBooking = (LinearLayout) headerView.findViewById(R.id.nav_upcommingBooking);
        notification_switch = (SwitchCompat) headerView.findViewById(R.id.notification_switch);

        user_profile_pic_iv = (CircleImageView) headerView.findViewById(R.id.user_profile_pic_iv);
        user_profile_pic_iv.setOnClickListener(this);
        user_profile_name_tv = (TextView) headerView.findViewById(R.id.user_profile_name_tv);
        img = (ImageView) headerView.findViewById(R.id.header_img);
        welcome_tv = (TextView) headerView.findViewById(R.id.welcome_tv);

        card_menu = (TextView) headerView.findViewById(R.id.card_menu);
        manage_vehicle_menu = (TextView) headerView.findViewById(R.id.manage_vehicle_menu);
        free_wash_menu = (TextView) headerView.findViewById(R.id.free_wash_menu);
        booking_vehicle_menu = (TextView) headerView.findViewById(R.id.booking_vehicle_menu);
        transaction_history_menu = (TextView) headerView.findViewById(R.id.transaction_history_menu);
        packages_menu = (TextView) headerView.findViewById(R.id.packages_menu);
        settings_menu = (TextView) headerView.findViewById(R.id.settings_menu);
        logout_menu = (TextView) headerView.findViewById(R.id.logout_menu);
        notification_menu = (TextView) headerView.findViewById(R.id.notification_menu);
        home_menu = (TextView) headerView.findViewById(R.id.home_menu);
        pastBooking_menu = (TextView) headerView.findViewById(R.id.pastBooking_menu);
        upcommingBooking_menu = (TextView) headerView.findViewById(R.id.upcommingBooking_menu);
        notificationCountTv = (TextView) headerView.findViewById(R.id.notificationCountTv);

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

//        if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//            logout_menu.setText(getString(R.string.signin));
//        } else {
//        logout_menu.setText(getString(R.string.logout));
//        }
    }

    public void setOnClick() {
        nav_manage_vehicle.setOnClickListener(this);
        nav_booking_vehicle.setOnClickListener(this);
        nav_transaction_history.setOnClickListener(this);
        nav_packages.setOnClickListener(this);
        nav_settings.setOnClickListener(this);
        nav_logout.setOnClickListener(this);
        nav_home.setOnClickListener(this);
        nav_notification.setOnClickListener(this);
        nav_pastBooking.setOnClickListener(this);
        nav_upcommingBooking.setOnClickListener(this);
        nav_free_wash.setOnClickListener(this);
        nav_card.setOnClickListener(this);


        notification_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    try {
                        MySharedPreferences.setNotificationSetting(mContext, "1");
                        callApiNotificationSetting(CountrySelectListener.APIRequest.
                                notification(MySharedPreferences.getUserId(MainActivity.this), "1"
                                        , "1", "eng"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // NotificationSettingWebService("1");
                } else {
                    try {
                        MySharedPreferences.setNotificationSetting(mContext, "0");
                        callApiNotificationSetting(CountrySelectListener.APIRequest.notification(MySharedPreferences.getUserId(MainActivity.this), "1"
                                , "0", "eng"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //NotificationSettingWebService("0");
                }
            }
        });
    }

    private void callApiNotificationSetting(Map<String, Object> login) {
        showProgress(mContext);
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<LoginResponse> call = null;
        call = myApiEndpointInterface.update_notification_setting(login);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    hideProgress();
                    if (response.body().getResponse().isStatus()) {
                        sucessToast(mContext, response.body().getResponse().getMessage());

                    } else {
                        errorToast(mContext, response.body().getResponse().getMessage());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                hideProgress();
            }
        });

    }

    public static void SetProfile() {
        if (MySharedPreferences.getProfileImage(context).compareTo("") != 0) {
            try {
                ImageLoader.getInstance().displayImage(MySharedPreferences.getProfileImage(context), user_profile_pic_iv, options);
            } catch (Exception e) {
                user_profile_pic_iv.setImageResource(R.drawable.no_image);
            }
        } else {
            user_profile_pic_iv.setImageResource(R.drawable.no_image);
        }

        user_profile_name_tv.setText(MySharedPreferences.getFirstName(context) + " " + MySharedPreferences.getLastName(context));

        if (MySharedPreferences.getNotificationSetting(context).compareTo("1") == 0) {
            notification_switch.setChecked(true);
        } else {
            notification_switch.setChecked(false);
        }


        // notificationCountTv.setVisibility(View.GONE);
    }

    public void displayView(String fragmentName, Object obj, Object obj1) {

        fragmentTransaction = mFragmentManager.beginTransaction();

        if (fragmentName.equals(AutoSpaConstant.HOMEFRAGMENT)) {
            mFragment = HomeFragment.newInstance(context, obj);
            SetHomeFragment();
        } else if (fragmentName.equals((AutoSpaConstant.ADDFRAGMENT))) {
            mFragment = VechicleList.newInstance(context);
            setManageVechileFragment();
        } else if (fragmentName.equals((AutoSpaConstant.CARD))) {
            mFragment = CardFragment.newInstance(context);
            setCardFragment();
        } else if (fragmentName.equals(AutoSpaConstant.NOTIFICATION)) {
            mFragment = Notification.newInstance(context);
            setNotificationFragment();
        } else if (fragmentName.equals(AutoSpaConstant.PROFILE)) {
            mFragment = MyProfile.newInstance(context);
        } else if (fragmentName.equals(AutoSpaConstant.SETTING)) {
            mFragment = Settings.newInstance(context);
            setSettingFragment();
        } else if (fragmentName.equals(AutoSpaConstant.BOOKINGHISTORY)) {
            mFragment = MyBooking.newInstance(context);
            setMyBookingFragment();
        } else if (fragmentName.equals(AutoSpaConstant.PASTBOOKING)) {
            mFragment = PastBooking.newInstance(context);
            setPastBookingFragment();
        } else if (fragmentName.equals(AutoSpaConstant.TRANSACTIONHISTORY)) {
            mFragment = MyTransactionHistory.newInstance(context);
            setTransactionHistoryFragment();
        } else if (fragmentName.equals(AutoSpaConstant.PACKAGESFRAGMENT)) {
            mFragment = PackagesFragment.newInstance(context);
            setPackagesFragment();
        } else if (fragmentName.equals(AutoSpaConstant.UPCOMINGBOOKINGS)) {
            mFragment = UpcomingBookings.newInstance(context);
            setUpcommingBookingFragment();
        } else if (fragmentName.equals(AutoSpaConstant.WASHCOMPLETEFRAGMENT)) {
            mFragment = WashCompleteFragment.newInstance(context, obj, obj1);
            SetHomeFragment();
        } else if (fragmentName.equals(AutoSpaConstant.FREE)) {
            mFragment = FreeWashFragment.newInstance(context);
            SetFreeFragment();
        }
        if (mFragment != null) {
            try {
                fragmentTransaction.replace(R.id.mainFrame, mFragment).addToBackStack(fragmentName);
                fragmentTransaction.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void NotificationSettingWebService(final String status) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("user_type", "1");
            jsonObject.put("not_status", status);
            jsonObject.put("language", MySharedPreferences.getLanguage(context));

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.notificationStatus(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        Log.e("tg", "response from server = " + mJsonObj.toString());

                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            MySharedPreferences.setNotificationSetting(context, status);
                            SetProfile();
                        } else {
                            // Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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
            e.printStackTrace();
        }
    }

    public void OpenDrawer() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerVisible(GravityCompat.END)) {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //  Toast.makeText(context, "calll", Toast.LENGTH_SHORT).show();
            //updateDate(intent);
        }
    };

    public static void goToHomeFragment() {
        fragmentManager.popBackStack();
    }

    public static void popTwoFragment() {
        fragmentManager.popBackStack();
        fragmentManager.popBackStack();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_home:
                displayView(AutoSpaConstant.HOMEFRAGMENT, null, null);
                DrawerClose();
                break;
            case R.id.nav_manage_vehicle:

//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                displayView(AutoSpaConstant.ADDFRAGMENT, null, null);
                DrawerClose();
//                }

                break;
            case R.id.nav_booking_vehicle:
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                displayView(AutoSpaConstant.BOOKINGHISTORY, null, null);
                DrawerClose();
//                }
                break;
            case R.id.nav_pastBooking:
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                displayView(AutoSpaConstant.PASTBOOKING, null, null);
                DrawerClose();
//                }
                break;
            case R.id.nav_transaction_history:
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                displayView(AutoSpaConstant.TRANSACTIONHISTORY, null, null);
                DrawerClose();
//                }
                break;
            case R.id.nav_packages: {
                displayView(AutoSpaConstant.PACKAGESFRAGMENT, null, null);
                DrawerClose();
            }
            break;
            case R.id.nav_card: {
                displayView(AutoSpaConstant.CARD, null, null);
                DrawerClose();
            }
            break;
            case R.id.nav_upcommingBooking:
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                displayView(AutoSpaConstant.UPCOMINGBOOKINGS, null, null);
                DrawerClose();
//                }
                break;
            case R.id.nav_notification:
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                displayView(AutoSpaConstant.NOTIFICATION, null, null);
                DrawerClose();
//                }
                break;
            case R.id.nav_settings:
                displayView(AutoSpaConstant.SETTING, null, null);
                DrawerClose();
                break;
            case R.id.nav_free_wash:
                displayView(AutoSpaConstant.FREE, null, null);
                DrawerClose();
                break;
            case R.id.user_profile_pic_iv:
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                displayView(AutoSpaConstant.PROFILE, null, null);
                DrawerClose();
//                }
                break;
            case R.id.nav_logout:

//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    SetLogout(context);
//                } else {

                final Dialog dialog;

                dialog = new Dialog(MainActivity.this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_logout);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Window window = dialog.getWindow();
                window.setGravity(Gravity.CENTER);
                dialog.show();

                TextView dialog_yes = (TextView) dialog.findViewById(R.id.dialog_yes);
                TextView dialog_no = (TextView) dialog.findViewById(R.id.dialog_no);
                TextView dialog_twoaction_descss = (TextView) dialog.findViewById(R.id.dialog_twoaction_descss);
                dialog_twoaction_descss.setText(getResources().getString(R.string.logout_message));

                dialog_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                            SetLogout(context);
//                        } else {
//                            LogoutWebService(getApplicationContext(), true);
//                        }

                        SetLogout(context);

                        dialog.dismiss();
                    }
                });

                dialog_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
//                }
        }
    }

    public void LogoutWebService(final Context context, final boolean status) {
        if (progressDialog != null)
            progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("user_type", "1");

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.logOut(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(response.body().string());
                        Log.e("tg", "response from server = " + mJsonObj.toString());
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            if (status)
                                SetLogout(context);
                        }

                    } catch (Exception e) {
                        Log.e("error", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    Util.dismissPrograssDialog(progressDialog);
                }
            });
        } catch (Exception e) {
            Util.dismissPrograssDialog(progressDialog);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        setTitle(R.string.app_name);
    }

    public void DrawerClose() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainFrame);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (currentFragment instanceof HomeFragment) {
            finish();
        } else {
            super.onBackPressed();
        }
           /*

            if (currentFragment instanceof HomeFragment) {
                finish();
            } else {
                displayView(AutoSpaConstant.HOMEFRAGMENT, null, null);
            }*/

    }

    public void SetHomeFragment() {
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));

        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);

        getSupportActionBar().setTitle("");
    }

    public void SetFreeFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);

        getSupportActionBar().setTitle("");
    }

    public void setManageVechileFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void setCardFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void setMyBookingFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void setPastBookingFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void setUpcommingBookingFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void setTransactionHistoryFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void setPackagesFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void setNotificationFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_OVER);
    }

    public void setSettingFragment() {
        card_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_card.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        free_wash_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_free_wash.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        home_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        manage_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        booking_vehicle_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        pastBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        upcommingBooking_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        transaction_history_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        notification_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        settings_menu.setTextColor(ContextCompat.getColor(this, R.color.whitecolor));
        logout_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        packages_menu.setTextColor(ContextCompat.getColor(this, R.color.graycolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_home.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_manage_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_booking_vehicle.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_pastBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_upcommingBooking.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_transaction_history.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_packages.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_notification.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));
        nav_settings.setBackgroundColor(ContextCompat.getColor(this, R.color.greencolor));
        nav_logout.setBackgroundColor(ContextCompat.getColor(this, R.color.whitecolor));

        notificationCountTv.setTextColor(ContextCompat.getColor(context, R.color.white));
        notificationCountTv.getBackground().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_OVER);
    }

    public void SetLogout(Context context) {
        LoginManager.getInstance().logOut();
        String deivce_token = MySharedPreferences.getDeviceId(context);
        String requestSave = MySharedPreferences.getRequest(context);
        String language = MySharedPreferences.getLanguage(context);
        MySharedPreferences.ClearAll(context);
        MySharedPreferences.setUserEmail(context, "");
        MySharedPreferences.setIsFirstTime(context, "");
        MySharedPreferences.setUserId(context, "");
        MySharedPreferences.setDeviceId(context, deivce_token);
        MySharedPreferences.setRequest(context, requestSave);
        MySharedPreferences.setLanguage(context, language);
        MySharedPreferences.setLoginStatus(context, "false");
        MySharedPreferences.setIsFirstTime(MainActivity.this, "yes");

        Intent intent = new Intent(context, Login.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.back_slide_in_up, R.anim.back_slide_out_up);
    }

    @Override
    public void executeFragment(String fragmentName, Object obj) {

    }

    @Override
    public void goBack() {

    }

    public void autoOnGPS() {
        googleApiClient = null;
        AutoSpaApplication.mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        AutoSpaApplication.mGoogleApiClient.connect();

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(MainActivity.this).build();
        googleApiClient.connect();
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); // this is the key ingredient

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can
                        // initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be
                        // fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling
                            // startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, 1000);
                        } catch (Exception e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have
                        // no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                startService(new Intent(MainActivity.this, LocationService.class));
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainFrame);

            if (currentFragment instanceof HomeFragment) {
                currentFragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
        registerReceiver(broadcastReceiver, new IntentFilter(
                LocationService.BROADCAST_ACTION));

        if (MySharedPreferences.getNotificationSetting(context).compareTo("1") == 0) {
            notification_switch.setChecked(true);
        } else {
            notification_switch.setChecked(false);
        }

        try {
            if (Integer.parseInt(MySharedPreferences.getNotificationCount(context)) == 0) {
                notificationCountTv.setVisibility(View.GONE);
            } else {
                notificationCountTv.setText(MySharedPreferences.getNotificationCount(context));
                notificationCountTv.setVisibility(View.VISIBLE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void setHomeScreenFromNotification() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DrawerClose();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.mainFrame);

        if (currentFragment instanceof HomeFragment) {
            final HomeFragment home = (HomeFragment) currentFragment;
            if (!MySharedPreferences.getNotification(context).equals("")) {
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        home.getNoticationData(MySharedPreferences.getNotification(context));
                    }
                });
            }
        } else {
            displayView(AutoSpaConstant.HOMEFRAGMENT, null, null);
        }
    }
}