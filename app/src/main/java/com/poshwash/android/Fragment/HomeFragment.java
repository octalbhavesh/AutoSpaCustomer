package com.poshwash.android.Fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.Activity.AddVehicle;
import com.poshwash.android.Activity.MainActivity;
import com.poshwash.android.Activity.PackageActivity;
import com.poshwash.android.Adapter.AppliedPromocodeAdapter;
import com.poshwash.android.Adapter.GetfarePromosAdapter;
import com.poshwash.android.Adapter.GetfareReviewAdapter;
import com.poshwash.android.Adapter.SelectAddressAdpater;
import com.poshwash.android.Adapter.SelectedCardAdapterNew;
import com.poshwash.android.Adapter.SelectedCarsAdapter;
import com.poshwash.android.Adapter.TimeSlotAdapter;
import com.poshwash.android.Beans.BeanAddresses;
import com.poshwash.android.Beans.BeanCarsNew;
import com.poshwash.android.Beans.BeanDrivers;
import com.poshwash.android.Beans.BeanPromoCodes;
import com.poshwash.android.Beans.BeanReviewData;
import com.poshwash.android.Beans.BeanTimeSlot;
import com.poshwash.android.Beans.MyBookingChildModal;
import com.poshwash.android.Beans.MyRequestDataBean;
import com.poshwash.android.Beans.ResponseNearestDriver;
import com.poshwash.android.Constant.AutoSpaApplication;
import com.poshwash.android.Constant.AutoSpaConstant;
import com.poshwash.android.Constant.GlobalControl;
import com.poshwash.android.Delegate.CountrySelectListener;
import com.poshwash.android.Delegate.MyApiEndpointInterface;
import com.poshwash.android.R;
import com.poshwash.android.Utils.AppUtils;
import com.poshwash.android.Utils.ConvertJsonToMap;
import com.poshwash.android.Utils.DividerItemDecoration;
import com.poshwash.android.Utils.FetchAddressIntentService;
import com.poshwash.android.Utils.LocationService;
import com.poshwash.android.Utils.MyProgressDialog;
import com.poshwash.android.Utils.MySharedPreferences;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.customViews.CustomButtonBold;
import com.poshwash.android.customViews.CustomTexView;
import com.poshwash.android.customViews.CustomTexViewBold;
import com.poshwash.android.customViews.CustomTexViewRegular;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.GraphRequest.TAG;
import static com.poshwash.android.Constant.AutoSpaConstant.CUREENTLOCATION;

public class HomeFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        OnDateSelectedListener, OnMonthChangedListener, TimeSlotAdapter.OnTimeSlotSelect {
    ArrayList<ResponseNearestDriver> listNearestDriver = new ArrayList<>();
    public static String mSelectedVehicleTypeId = null;
    public static String mSelectedVId = null;
    public static String mSelectedVNAME = null;
    public static String mSelectedCarId;
    public static String mSelectedCarName;
    public static String mSelectedCarPlate;
    public static DisplayImageOptions options;
    public static Context context;
    public GoogleMap googleMap;
    public ArrayList<BeanCarsNew> cars_list;
    View rootview;
    MyProgressDialog progressDialog;
    Place selectedPlace = null;
    private RelativeLayout actionbar;
    private ImageView toggleIcon;
    private TextView serviceProgressCancelTv;
    private LinearLayout serviceProgressLayout;
    private TextView serviceProgressStatusTv, serviceProgressTimeTv, progressReviewTextTv, progressReviewNameTv, viewDetailTv, selectVehicleServiceTv, selectLocationTv, reviewTextTv;
    private RatingBar progressRatingBar;
    private ImageView seviceProviderCallIv;
    private CircleImageView progressUserPicIv;
    private LinearLayout selectVehicleServiceLayout;
    // private TextView spAvailTv;
    private RelativeLayout userInfoLayout;
    private LinearLayout userInfoLl;
    private ImageView pinIv;
    private RatingBar ratingBar;
    private CircleImageView userPicIv;
    public MyRequestDataBean myRequestDataBean;
    String booking_id = "";
    //  String reference_id = "";
    String booking_driver_id = "";
    CountDownTimer countDownTimer;
    long timer_limit = 9999999;
    TimeSlotAdapter.OnTimeSlotSelect onTimeSlotSelect;

    private String fname;
    private Uri outputFileUri;
    private String photoFile = "";
    private String photoname = "";
    String wash_complete_duration;
    boolean is_cancel = true;
    long wait_time = 0;
    Dialog waitingdialog1;
    String provider_call_number = "";
    ObjectAnimator waiting_anim;
    Bundle customer_info_bundle = new Bundle();
    String ondemand_amount = "0";
    String membership_amount = "0";
    int time_slot = 0;
    String cars_amount = "0";
    MaterialCalendarView calendarView;
    TextView locationEt;
    String mBookingAddress, mBookingLat, mBookingLong;

    public static Object recive_object;
    ImageView imgDirection;
    public static String custumer_lat = "", custumerLng = "";
    TextView applyBtn;
    CustomTexView dateTv;
    CustomTexView timeTv;
    Dialog cal_dialog;
    ArrayList<BeanAddresses> address_list;
    SelectAddressAdpater selectSerivceAdpater;
    EditText promoCodeEt;
    //  ArrayList<BeanDrivers> driverList;
//    int currentDriverPosition = 0;
    RecyclerView appliedPromoRecylerView;
    ArrayList<BeanPromoCodes> promoCodeList;
    AppliedPromocodeAdapter appliedPromocodeAdapter;
    String paymentMode = "1";
    private AddressResultReceiver mResultReceiver;

    private LatLng mCenterLatLong;
    int PLACE_PICKER_REQUEST = 200;
    public Location mlastlocation;
    SelectedCardAdapterNew selectWeekdaysAdpater;
    ArrayList<BeanTimeSlot> timeSlotArray;
    Dialog timeSlotdialog;
    static Dialog ratingDialog;
    View mapView;
    private boolean hasPickUpLocationResult = false;
    private LinearLayout ll_booking_operation;
    private RelativeLayout rl_main;
    private Button btn_done;
    private long dialogCar = 0;
    private Marker marker = null;
    private Bitmap smallMarker;
    private String selectedVehicle;
    private String selectedId;

    public static HomeFragment newInstance(Context contex, Object obj) {
        HomeFragment f = new HomeFragment();
        recive_object = obj;
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (rootview == null) {
            rootview = inflater.inflate(R.layout.home_fragment, container, false);
        }
        autoOnGps();
        context = getActivity();
        onTimeSlotSelect = this;
        initView(rootview);
        hideSoftKeyboard();
        checkRequestPermission();

        onLoad();
        setonClick();
        SsetOnLoadData();

        ((MainActivity) getActivity()).SetHomeFragment();
//        callWebServiceShowVehicleList();
        int height = 50;
        int width = 50;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().
                getDrawable(R.drawable.pingreen);
        Bitmap b = bitmapdraw.getBitmap();
        smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return rootview;
    }

    private void showRatingDialog() {
        final Dialog dialog = new Dialog(getActivity());
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        View view = LayoutInflater.from(context).inflate(R.layout.rating_dialog, null, false);
        dialog.setContentView(view);

        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();

    }

    private void autoOnGps() {

        AutoSpaApplication.mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        AutoSpaApplication.mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(AutoSpaApplication.mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void SsetOnLoadData() {

        if (MySharedPreferences.getRatingStatus(context)) {
            try {
                if (ratingDialog == null) {
                    showRatingDialog();
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
        selectVehicleServiceLayout.setVisibility(View.VISIBLE);
        selectLocationTv.setVisibility(View.VISIBLE);
        pinIv.setVisibility(View.VISIBLE);
        serviceProgressLayout.setVisibility(View.GONE);
        serviceProgressCancelTv.setVisibility(View.GONE);
        seviceProviderCallIv.setVisibility(View.GONE);
        Util.setProfilePic(getActivity(), progressUserPicIv);

        progressReviewTextTv.setText(MySharedPreferences.getFirstName(getActivity()) + " " + MySharedPreferences.getLastName(getActivity()));
        try {
            ratingBar.setRating(Float.parseFloat(MySharedPreferences.getRating(getActivity())));
            progressReviewNameTv.setText(Util.SetRating(MySharedPreferences.getRating(getActivity())) + "/5");
            progressRatingBar.setRating(Float.parseFloat(MySharedPreferences.getRating(getActivity())));
        } catch (Exception e) {
        }

        if (MySharedPreferences.getRequest(context).compareTo("") != 0) {
            AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
            aBuilder.setTitle(context.getString(R.string.app_name))
                    .setMessage(context.getString(R.string.we_found_a_request_which_is_not_submited_by_you_please_submit_to_send_booking_request))
                    .setIcon(R.drawable.appicon)
                    .setPositiveButton(context.getString(R.string.submit)
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    callWebserviceBookingFromSkip();
                                }
                            }).setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MySharedPreferences.setRequest(context, "");
                    dialog.dismiss();
                }
            });

            AlertDialog alert = aBuilder.create();
            alert.show();
            Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            button.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
        }

        if (!MySharedPreferences.getNotification(context).equals("")) {
            getNoticationData(MySharedPreferences.getNotification(context));
        }

    }


    private void onLoad() {
        mResultReceiver = new AddressResultReceiver(new Handler());
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

    private void setonClick() {
        toggleIcon.setOnClickListener(this);
        seviceProviderCallIv.setOnClickListener(this);
        selectVehicleServiceTv.setOnClickListener(this);
        serviceProgressCancelTv.setOnClickListener(this);
        viewDetailTv.setOnClickListener(this);

        locationEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    ((Activity) context).startActivityForResult(builder.build(((Activity) context)), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        AutoSpaApplication.getInstance().setLocale(MySharedPreferences.getLanguage(context), context);
       /* getProfile();
        getSiteSettings();*/

        if (Util.isConnectingToInternet(getActivity())) {
            try {
                callApiNearestDriver(CountrySelectListener.APIRequest.
                        nearest_driver(MySharedPreferences.getUserId(getActivity()),
                                String.valueOf(LocationService.LATEST_LOC_OBJ.getLatitude()),
                                String.valueOf(LocationService.LATEST_LOC_OBJ.getLongitude()),
                                MySharedPreferences.getLanguage(getActivity())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Util.errorToast(getActivity(), getString(R.string.no_internet_connection));
        }
        Util.hideKeyboard(context, rootview);
    }

    private void callApiNearestDriver(Map<String, Object> login) {
        Util.showProgress(getActivity());
        MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
        Call<ResponseBody> call = null;
        call = myApiEndpointInterface.get_nearest_sp(login);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Util.hideProgress();
                    listNearestDriver.clear();
                    String response1 = response.body().string();
                    JSONObject jsonObject = new JSONObject(response1);
                    JSONObject jsonObject1 = jsonObject.optJSONObject("response");
                    String message = jsonObject1.optString("message");
                    boolean status1 = jsonObject1.optBoolean("status");
                    if (status1) {

                        JSONArray jsonArray = jsonObject1.optJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject2 = jsonArray.optJSONObject(i);
                            ResponseNearestDriver model = new ResponseNearestDriver();
                            String lat1 = jsonObject2.optString("latitude");
                            model.setLat1(Double.parseDouble(lat1));
                            String long1 = jsonObject2.optString("longitude");
                            model.setLong1(Double.parseDouble(long1));
                            listNearestDriver.add(model);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                googleMap.clear();
                            }
                        });
                        if (listNearestDriver.size() > 0) {
                            float min = 10, max = 30;
                            for (int i = 0; i < listNearestDriver.size(); i++) {
                                Random r = new Random();
                                float random = min + r.nextFloat() * (max - min);
                                createMarker(listNearestDriver.get(i).getLat1(), listNearestDriver.get(i).getLong1(),
                                        random);
                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(new LatLng(listNearestDriver.get(i).getLat1(),
                                                listNearestDriver.get(i).getLong1()))
                                        .zoom(8)
                                        .build();
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            }

                        }
                    } else {
                        //  Util.errorToast(getActivity(), message);

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

    protected Marker createMarker(double latitude, double longitude, float random) {


        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .rotation(random)
                .flat(true)
                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));

        return marker;

    }

    private void getSiteSettings() {
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("language", MySharedPreferences.getLanguage(context));

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.getSiteSetting(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject mJsonObj = new JSONObject(response.body().string());
                        Log.e("tg", "response from server = " + mJsonObj.toString());
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            JSONArray jsonArray = mJsonObj.getJSONObject("response").getJSONArray("data");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if (jsonObject1.has("on_demand"))
                                ondemand_amount = jsonObject1.getString("on_demand");
                            if (jsonObject1.has("membership"))
                                membership_amount = jsonObject1.getString("membership");
                            if (jsonObject1.has("schedule_time"))
                                time_slot = Integer.parseInt(jsonObject1.getString("schedule_time"));
                            if (jsonObject1.has("request-validity"))
                                wait_time = (Integer.parseInt(jsonObject1.getString("request-validity"))) * 1000;
                            time_slot = time_slot * 60 * 1000;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getProfile() {

        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("language", MySharedPreferences.getLanguage(context));

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.getProfile(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject mJsonObj = new JSONObject(response.body().string());
                        Log.e("tg", "response from server = " + mJsonObj.toString());
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {

                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1 = mJsonObj.getJSONObject("response").getJSONObject("data");

                            MySharedPreferences.setFirstName(context, jsonObject1.getString("first_name"));
                            MySharedPreferences.setLastName(context, jsonObject1.getString("last_name"));
                            MySharedPreferences.setUserEmail(context, jsonObject1.getString("email"));
                            MySharedPreferences.setPhoneNumber(context, jsonObject1.getString("mobile"));
                            MySharedPreferences.setCountryCode(context, jsonObject1.getString("country_code"));
                            MySharedPreferences.setLoginType(context, jsonObject1.getString("login_type"));
                            MySharedPreferences.setLanguage(context, jsonObject1.getString("language"));
                            MySharedPreferences.setNotificationSetting(context, jsonObject1.getString("notification"));
                            MySharedPreferences.setProfileImage(context, GlobalControl.IMAGE_BASE_URL + jsonObject1.getString("img"));
                            MySharedPreferences.setNotificationCount(context, jsonObject1.getString("unread_badge_count"));

                            // MainActivity.SetProfile();

                        } else {
                            ((MainActivity) getActivity()).LogoutWebService(getActivity(), true);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        if (v == toggleIcon) {
            ((MainActivity) context).OpenDrawer();
        }

        if (v == selectVehicleServiceTv) {
            Log.e("lat", String.valueOf(mBookingLat));
            checkArea();
           /* if (SystemClock.elapsedRealtime() - dialogCar < 1000) {
                return;
            }
            dialogCar = SystemClock.elapsedRealtime();
            selectCarDialog();*/
        }

       /* if (v == seviceProviderCallIv) {
            callDriver();
        }
*/
       /* if (v == viewDetailTv) {
            Intent intent = new Intent(context, BookingDetail.class);
            intent.putExtra("bookingId", booking_id);
            intent.putExtra("bookingDriverId", booking_driver_id);
            context.startActivity(intent);
        }

        if (v == serviceProgressCancelTv) {
            Intent intent = new Intent(getActivity(), CancelBooking.class);
            intent.putExtra("booking_id", booking_id);
            intent.putExtra("booking_driver_id", booking_driver_id);
            startActivityForResult(intent, 102);
            getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }*/
    }

    private void checkArea() {
        Util.showProgress(getActivity());
        Call<ResponseBody> call = null;
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject();
            jsonObject1.put("latitude", mBookingLat);
            jsonObject1.put("longitude", mBookingLong);
            jsonObject1.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject1.put("language", MySharedPreferences.getLanguage(getActivity()));

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.check_user_location(new ConvertJsonToMap().jsonToMap(jsonObject1));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Util.hideProgress();
                        JSONObject jsonObject11 = new JSONObject(response.body().string());
                        if (jsonObject11.getJSONObject("response").getBoolean("status")) {
                            Log.e("check_area", "Success");
                            if (SystemClock.elapsedRealtime() - dialogCar < 1000) {
                                return;
                            }

                            dialogCar = SystemClock.elapsedRealtime();
                            selectCarDialog();
                        } else {
                            Log.e("check_area", "failer");
                            Toast.makeText(getActivity(), jsonObject11.getJSONObject("response").optString("message"), Toast.LENGTH_LONG).show();
                            // callWebServiceShowVechicleType();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public void callDriver() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CALL_PHONE

            }, 2);
        } else {
            if (provider_call_number.compareTo("") != 0) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + provider_call_number));
                startActivity(callIntent);
            }
        }
    }

    public void resetValues() {
        myRequestDataBean = null;
        paymentMode = "1";
        promoCodeList.clear();
        //   currentDriverPosition = 0;
        cars_amount = "0";
        provider_call_number = "";
        booking_id = "";
        booking_driver_id = "";
    }


    private void selectCarDialog() {
        Log.e("dialog", "dialog");
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.select_car_dialog);
        dialog1.setCancelable(true);
        ImageView ivClose = (ImageView) dialog1.findViewById(R.id.iv_close);
        TextView heading = (TextView) dialog1.findViewById(R.id.heading_tv);
        TextView done_tv = (TextView) dialog1.findViewById(R.id.next_btn);
        TextView addVehicle_btn = (TextView) dialog1.findViewById(R.id.addVehicle_btn);
        RecyclerView listView = (RecyclerView) dialog1.findViewById(R.id.listview);
        dialog1.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();
        callWebServiceShowVehicleList();
      /*  selectWeekdaysAdpater = new SelectCarsAdpater(context, cars_list);
        listView.setAdapter(selectWeekdaysAdpater);*/
        selectWeekdaysAdpater = new SelectedCardAdapterNew(context, cars_list);
        listView.setAdapter(selectWeekdaysAdpater);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        listView.setAdapter(selectWeekdaysAdpater);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        done_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   SelectRequestTypeDialog(false);

                if (mSelectedVehicleTypeId != null) {
                    MySharedPreferences.setBookingAddress(getActivity(), mBookingAddress);
                    MySharedPreferences.setBookingLat(getActivity(), mBookingLat);
                    MySharedPreferences.setBookingLong(getActivity(), mBookingLong);
                    MySharedPreferences.setBookingVehicleTypeId(getActivity(), mSelectedVehicleTypeId);
                    MySharedPreferences.setBookingUserVehicleId(getActivity(), mSelectedCarId);
                    MySharedPreferences.setBookingUserVehicleName(getActivity(), mSelectedCarName);
                    MySharedPreferences.setBookingUserPlate(getActivity(), mSelectedCarPlate);
                  /*  Intent i = new Intent(getActivity(), RequestActivity.class);
                    getActivity().startActivity(i);*/
                    Intent i = new Intent(getActivity(), PackageActivity.class);
                    i.putExtra("name", mSelectedVNAME);
                    i.putExtra("id", mSelectedVId);
                    getActivity().startActivity(i);
                    mSelectedVehicleTypeId = null;
                    dialog1.dismiss();
                } else {
                    Util.errorToast(getActivity(), "Please select Vehicle");
                }

            }
        });

        addVehicle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    Util.showAlertDialogForSignin(context);
//                } else {
                Intent intent = new Intent(getActivity(), AddVehicle.class);
                intent.putExtra("type", "add");
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
//                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void SelectRequestTypeDialog(boolean isBack) {

        CustomTexViewBold headingTv;
        final RadioButton onDemandRb;
        final RadioButton orderLaterRb;
        final RelativeLayout memberTypeFormLayout;
        View sap;

        CheckBox remember_cb;
        RelativeLayout cardTypeLayout;
        CustomTexViewBold prmocodetv;
        CustomTexViewBold nextBtn;
        RecyclerView appliedPromoRecylerView;

        ImageView backIv;
        RelativeLayout onDemandRl;
        RelativeLayout orderLaterRl;
        final TextView paymentModeTv;


        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.select_request_type);

        headingTv = (CustomTexViewBold) dialog.findViewById(R.id.heading_tv);
        // onDemandRb = (RadioButton) dialog.findViewById(R.id.on_demand_rb);
        //  orderLaterRb = (RadioButton) dialog.findViewById(R.id.orderLater_rb);
        memberTypeFormLayout = (RelativeLayout) dialog.findViewById(R.id.member_type_form_layout);
        sap = (View) dialog.findViewById(R.id.sap);
        dateTv = (CustomTexView) dialog.findViewById(R.id.date_tv);
        timeTv = (CustomTexView) dialog.findViewById(R.id.time_tv);
        remember_cb = (CheckBox) dialog.findViewById(R.id.remember_cb);
        //  cardTypeLayout = (RelativeLayout) dialog.findViewById(R.id.card_type_layout);
        //  prmocodetv = (CustomTexViewBold) dialog.findViewById(R.id.prmocodetv);
        applyBtn = (TextView) dialog.findViewById(R.id.applyBtn);
        promoCodeEt = (EditText) dialog.findViewById(R.id.promoCodeEt);
        nextBtn = (CustomTexViewBold) dialog.findViewById(R.id.next_btn);
        backIv = (ImageView) dialog.findViewById(R.id.backIv);
        //  onDemandRl = (RelativeLayout) dialog.findViewById(R.id.on_demand_rl);
        //  orderLaterRl = (RelativeLayout) dialog.findViewById(R.id.orderLater_rl);
        paymentModeTv = (TextView) dialog.findViewById(R.id.paymentModeTv);
        appliedPromoRecylerView = (RecyclerView) dialog.findViewById(R.id.appliedPromoRecylerView);

        appliedPromocodeAdapter = new AppliedPromocodeAdapter(context, promoCodeList, true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        appliedPromoRecylerView.setLayoutManager(mLayoutManager);
        appliedPromoRecylerView.setItemAnimator(new DefaultItemAnimator());
        appliedPromoRecylerView.setAdapter(appliedPromocodeAdapter);


        paymentModeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPaymentMode(paymentModeTv, paymentMode);
            }
        });

      /*  onDemandRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!onDemandRb.isChecked()) {
                    onDemandRb.setChecked(true);
                    orderLaterRb.setChecked(false);
                    memberTypeFormLayout.setVisibility(View.GONE);
                }
            }
        });*/

       /* orderLaterRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!orderLaterRb.isChecked()) {
                    onDemandRb.setChecked(false);
                    orderLaterRb.setChecked(true);
                    memberTypeFormLayout.setVisibility(View.VISIBLE);
                }
            }
        });*/

        dateTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    getCalanderDates(1);

                }
                return true;
            }
        });

        timeTv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!TextUtils.isEmpty(dateTv.getText().toString())) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                       /* DialogFragment newFragment = new FromToPickerFragment();
                        newFragment.show(getActivity().getFragmentManager(), "TimePicker");*/


                        if (timeSlotArray.size() > 0) {

                            timeSlotdialog = new Dialog(context);
                            timeSlotdialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            timeSlotdialog.setContentView(R.layout.timeslot_layout);
                            RecyclerView listview;

                            listview = (RecyclerView) timeSlotdialog.findViewById(R.id.listview);

                            TimeSlotAdapter adapter = new TimeSlotAdapter(context, timeSlotArray, dateTv.getText().toString());
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            listview.setLayoutManager(mLayoutManager);
                            listview.setItemAnimator(new DefaultItemAnimator());
                            listview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                            listview.setAdapter(adapter);
                            adapter.setOnTimeSlotSelect(onTimeSlotSelect);

                            timeSlotdialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            timeSlotdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Window window = timeSlotdialog.getWindow();
                            window.setGravity(Gravity.CENTER);
                            timeSlotdialog.show();
                        } else {
                            AlertDialog.Builder aBuilder = new AlertDialog.Builder(context);
                            aBuilder.setTitle(context.getString(R.string.app_name))
                                    .setMessage(context.getString(R.string.there_is_no_service_provider_available_now))
                                    .setIcon(R.drawable.appicon)
                                    .setPositiveButton(context.getString(R.string.ok)
                                            , new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });

                            AlertDialog alert = aBuilder.create();
                            alert.show();
                            Button button = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                            button.setTextColor(ContextCompat.getColor(context, R.color.greencolor));
                        }
                    }
                } else {

                    Util.Toast(context, getString(R.string.please_select_date_first));
                }
                return true;
            }
        });

        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(context, applyBtn);
                String promoCode = promoCodeEt.getText().toString().trim();
                if (!TextUtils.isEmpty(promoCode)) {
                    boolean isValid = true;
                    for (int i = 0; i < promoCodeList.size(); i++) {
                        if (promoCodeList.get(i).getPromoCode().compareToIgnoreCase(promoCode) == 0) {
                            isValid = false;
                        }
                    }
                    if (isValid) {
                        callApplyPromoCode(promoCode);
                    } else {
                        Util.Toast(context, getString(R.string.same_promocode_issue));
                    }
                } else {
                    Util.Toast(context, getString(R.string.please_enter_the_promo_code));
                }
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCarDialog();
                dialog.cancel();
            }
        });


        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRequestDataBean.setPromoCodeList(promoCodeList);
//Dynamic Data
//                if (onDemandRb.isChecked()) {
//                    myRequestDataBean.setRequest_type("1");
//                    myRequestDataBean.setRequest_time("");
//                    myRequestDataBean.setRequest_date("");
//                    myRequestDataBean.setPaymentMode(paymentMode);
//                    calculateAmount();
//                } else {
                if (TextUtils.isEmpty(dateTv.getText().toString().trim())) {
                    Util.Toast(context, getResources().getString(R.string.select_date_error));
                    return;
                }
                if (TextUtils.isEmpty(timeTv.getText().toString().trim())) {
                    Util.Toast(context, getResources().getString(R.string.request_time_error));
                    return;
                }
                if (Util.CheckDate(dateTv.getText().toString().trim() + " " + Util.Time12to24(timeTv.getText().toString().trim()).split(" - ")[0])) {
                    Util.Toast(context, getResources().getString(R.string.please_select_upcoming_date_time));
                    return;
                }
                //Dynamic Data
//                myRequestDataBean.setRequest_type("2");
//                myRequestDataBean.setRequest_time(Util.Time12to24(timeTv.getText().toString().trim()));
//                myRequestDataBean.setRequest_date(dateTv.getText().toString().trim());
//                myRequestDataBean.setPaymentMode(paymentMode);
//                calculateAmount();
//                }
                dialog.dismiss();
                ShowJobPreview();
            }
        });

        if (isBack) {
            if (myRequestDataBean.getRequest_type().equals("1")) {
                //  onDemandRb.setChecked(true);
            } else {
                //  orderLaterRb.setChecked(true);
                dateTv.setText(myRequestDataBean.getRequest_date());
                timeTv.setText(Util.Time24to12(myRequestDataBean.getRequest_time()));
            }

            if (myRequestDataBean.getPaymentMode().equals("1")) {
                paymentModeTv.setText(getString(R.string.cash));
            } else {
                paymentModeTv.setText(getString(R.string.span_payment));
            }
            promoCodeList = myRequestDataBean.getPromoCodeList();
            appliedPromocodeAdapter.notifyDataSetChanged();
        }


        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }

    private void callApplyPromoCode(String promoCode) {

        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("promo_code", promoCode);
            jsonObject.put("language", MySharedPreferences.getLanguage(context));

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.applyPromoCode(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1 = mJsonObj.getJSONObject("response").getJSONObject("data");

                            setAppliedPromocode(jsonObject1);

                        } else {
                            Util.Toast(context, mJsonObj.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
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

    private void setAppliedPromocode(JSONObject jsonObject1) {
        try {
            BeanPromoCodes beanPromoCodes = new BeanPromoCodes();
            beanPromoCodes.setPromoCodeId(jsonObject1.getString("id"));
            beanPromoCodes.setPromoCode(jsonObject1.getString("code"));
            beanPromoCodes.setDiscount(jsonObject1.getString("discount"));
            promoCodeList.add(beanPromoCodes);
            promoCodeEt.setText("");
            appliedPromocodeAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void selectPaymentMode(final TextView paymentModeTv, String defaultSelected) {
        final String[] items = {getString(R.string.cash), getString(R.string.online)};
        final Dialog dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.singlechoiceselectitem);

        final RadioButton c1 = (RadioButton) dialog1.findViewById(R.id.radio1);
        final RadioButton c2 = (RadioButton) dialog1.findViewById(R.id.radio2);
        TextView txtTitle = (TextView) dialog1.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.select_payment_mode);
        c1.setText(items[0]);
        c2.setText(items[1]);

        if (defaultSelected.compareTo("1") == 0) {
            c1.setChecked(true);
            c2.setChecked(false);
        } else {
            c1.setChecked(false);
            c2.setChecked(true);
        }

        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMode = "2";
                paymentModeTv.setText(items[1]);
                c1.setChecked(false);
                dialog1.dismiss();
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paymentMode = "1";
                paymentModeTv.setText(items[0]);
                c2.setChecked(false);
                dialog1.dismiss();
            }
        });


        dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog1.show();
    }


    private void getCalanderDates(final int num_dates) {
        cal_dialog = new Dialog(context);
        cal_dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cal_dialog.setContentView(R.layout.calenderview_layout);

        calendarView = (MaterialCalendarView) cal_dialog.findViewById(R.id.calendarView);
        final TextView heading_tv = (TextView) cal_dialog.findViewById(R.id.heading_tv);
        heading_tv.setText(R.string.please_select_a_date);

        Calendar instance1 = Calendar.getInstance();
        instance1.set(instance1.get(Calendar.YEAR), instance1.get(Calendar.MONTH), instance1.get(Calendar.DATE));

        Calendar instance2 = Calendar.getInstance();
        instance2.set(instance2.get(Calendar.YEAR), instance1.get(Calendar.MONTH), instance1.get(Calendar.DATE) + 7);
        calendarView.state().edit()
                .setMinimumDate(instance1.getTime())
                .commit();

        calendarView.state().edit()
                .setMaximumDate(instance2.getTime())
                .commit();

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        calendarView.setShowOtherDates(MaterialCalendarView.SHOW_ALL);
        calendarView.setDynamicHeightEnabled(true);
        calendarView.setSelectionMode(MaterialCalendarView.SELECTION_MODE_MULTIPLE);

        cal_dialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        cal_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = cal_dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        cal_dialog.show();
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        String dat = date.getYear() + "-" + pad((date.getMonth() + 1)) + "-" + pad(date.getDay());
        dateTv.setText(dat);
        timeTv.setText("");
        myRequestDataBean.setRequest_date(dat);
        //Dynamic Data
//        callNearByServiceProvider(mCenterLatLong.latitude, mCenterLatLong.longitude);
        //Static Data
        getTimeSlotArray(null);
        cal_dialog.cancel();
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

    }

    private void SelectRequestAddress() {
        ArrayList<String> dummy_address = new ArrayList<>();
        dummy_address.clear();
        address_list = new ArrayList<>();
        address_list.clear();
        /*BeanAddresses beanAddresses = new BeanAddresses();
        beanAddresses.setAddress(MySharedPreferences.getAddress(context));
        beanAddresses.setLatitude(MySharedPreferences.getLatitude(context));
        beanAddresses.setLongitude(MySharedPreferences.getLongitude(context));
        beanAddresses.setIs_selected(true);
        address_list.add(beanAddresses);
        dummy_address.add(MySharedPreferences.getAddress(context));*/

        final Dialog dialog1 = new Dialog(context);
        dialog1.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.select_address_screen);
        TextView next_btn = (TextView) dialog1.findViewById(R.id.next_btn);
        ListView listview = (ListView) dialog1.findViewById(R.id.listview);
        ImageView backIv = (ImageView) dialog1.findViewById(R.id.backIv);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < address_list.size(); i++) {
                    if (address_list.get(i).is_selected()) {
                        myRequestDataBean.setRequest_address(address_list.get(i).getAddress());
                        myRequestDataBean.setRequest_lat(address_list.get(i).getLatitude());
                        myRequestDataBean.setRequest_long(address_list.get(i).getLongitude());
                    }
                }

                if (myRequestDataBean.getRequest_address().compareTo("") != 0) {
                    ShowJobPreview();
                    //GoForBooking();
                    dialog1.cancel();
                } else {
                    Util.Toast(context, getResources().getString(R.string.request_address_error));
                    return;
                }
            }
        });

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectRequestTypeDialog(true);
                dialog1.cancel();
            }
        });


        String address = "";

        try {
            if (AutoSpaApplication.mLastLocation != null) {
                try {

                    HashMap map = Util.reverseGeocode(AutoSpaApplication.mLastLocation.getLatitude(), AutoSpaApplication.mLastLocation.getLongitude(), context);
                    address = map.get("address").toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (address != null && address.compareTo("") != 0) {
                BeanAddresses beanAddress = new BeanAddresses();
                beanAddress.setAddress(address);
                beanAddress.setLatitude(AutoSpaApplication.mLastLocation.getLatitude() + "");
                beanAddress.setLongitude(AutoSpaApplication.mLastLocation.getLongitude() + "");
                if (myRequestDataBean.getRequest_address().compareToIgnoreCase(address) == 0) {
                    beanAddress.setIs_selected(true);
                } else {
                    beanAddress.setIs_selected(false);
                }

                address_list.add(beanAddress);
                dummy_address.add(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectSerivceAdpater = new SelectAddressAdpater(context, address_list);
        listview.setAdapter(selectSerivceAdpater);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < address_list.size(); i++) {
                    if (i == position) {
                        address_list.get(position).setIs_selected(true);

                    } else {
                        address_list.get(i).setIs_selected(false);

                    }
                }

                selectSerivceAdpater.notifyDataSetChanged();
            }
        });

        dialog1.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog1.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog1.show();

    }

    public void ShowJobPreview() {

        CustomTexViewBold jobPriceTv;
        final CircleImageView driverPicIv;
        final CustomTexViewBold driverNameTv;
        final TextView driverPhoneTv;
        final MaterialRatingBar driverRatingBar;
        CustomButtonBold changeDriverBtn;
        CustomTexViewBold requestTypeTv;
        TextView promocodeTv;
        TextView promo;
        RecyclerView carsRecyclerView;
        ImageView backIv;
        TextView locationTv;
        TextView paymentModeTv;
        final RecyclerView appliedPromoRecyler;

        CustomTexViewBold confirmbBtn;
        CustomTexViewBold cancelBtn;

        final Dialog dialog = new Dialog(context);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.job_preview_layout);

        jobPriceTv = (CustomTexViewBold) dialog.findViewById(R.id.jobPriceTv);
        driverPicIv = (CircleImageView) dialog.findViewById(R.id.driverPicIv);
        driverNameTv = (CustomTexViewBold) dialog.findViewById(R.id.driverNameTv);
        driverRatingBar = (MaterialRatingBar) dialog.findViewById(R.id.driverRatingBar);
        changeDriverBtn = (CustomButtonBold) dialog.findViewById(R.id.changeDriverBtn);
        requestTypeTv = (CustomTexViewBold) dialog.findViewById(R.id.requestTypeTv);
        confirmbBtn = (CustomTexViewBold) dialog.findViewById(R.id.confirmbBtn);
        cancelBtn = (CustomTexViewBold) dialog.findViewById(R.id.cancelBtn);
        driverPhoneTv = (CustomTexViewRegular) dialog.findViewById(R.id.driverPhoneTv);
        backIv = (ImageView) dialog.findViewById(R.id.backIv);
        locationTv = (TextView) dialog.findViewById(R.id.locationTv);
        paymentModeTv = (TextView) dialog.findViewById(R.id.paymentModeTv);

        promocodeTv = (CustomTexViewRegular) dialog.findViewById(R.id.promocodeTv);
        promo = (TextView) dialog.findViewById(R.id.promo);
        carsRecyclerView = (RecyclerView) dialog.findViewById(R.id.carsRecyclerView);

        confirmbBtn = (CustomTexViewBold) dialog.findViewById(R.id.confirmbBtn);
        cancelBtn = (CustomTexViewBold) dialog.findViewById(R.id.cancelBtn);
        appliedPromoRecyler = (RecyclerView) dialog.findViewById(R.id.appliedPromoRecylerView);


        if (myRequestDataBean.getPromoCodeList().size() == 0) {
            promo.setVisibility(View.GONE);
        } else {
            promo.setVisibility(View.VISIBLE);
        }

        appliedPromocodeAdapter = new AppliedPromocodeAdapter(context, myRequestDataBean.getPromoCodeList(), false);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getApplicationContext());
        appliedPromoRecyler.setLayoutManager(mLayoutManager1);
        appliedPromoRecyler.setItemAnimator(new DefaultItemAnimator());
        appliedPromoRecyler.setAdapter(appliedPromocodeAdapter);

        confirmbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dynamic Data
//                if (MySharedPreferences.getUserId(context).compareTo("") == 0) {
//                    saveRequest();
//                    Util.showAlertDialogForSignin(context);
//                } else {
//                    MySharedPreferences.setRequest(context, "");
//                    callWebserviceBooking(dialog);
//                }
                //Static Data

                ll_booking_operation.setVisibility(View.VISIBLE);
                dialog.dismiss();
                btn_done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ll_booking_operation.setVisibility(View.GONE);
                    }
                });


            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        jobPriceTv.setText("" + Util.DisplayAmount(myRequestDataBean.getRequest_amount()));

        locationTv.setText(myRequestDataBean.getRequest_address());
        paymentModeTv.setText(myRequestDataBean.getPaymentMode());
        if (myRequestDataBean.getRequest_type().equals("1")) {
            requestTypeTv.setText(getString(R.string.on_demand));
        } else {
            requestTypeTv.setText(Util.Time24to12(myRequestDataBean.getRequest_time()) + " " + getString(R.string.on) + " " + Util.convertTimeInDetail(myRequestDataBean.getRequest_date()));
        }

        if (myRequestDataBean.getPaymentMode().equals("1")) {
            paymentModeTv.setText(getString(R.string.cash));
        } else {
            paymentModeTv.setText(getString(R.string.span_payment));
        }

        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectRequestTypeDialog(true);
                dialog.cancel();
            }
        });

        SelectedCarsAdapter selectedCarsAdapter = new SelectedCarsAdapter(context, myRequestDataBean.getSelected_cars());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        carsRecyclerView.setLayoutManager(mLayoutManager);
        carsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        carsRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        carsRecyclerView.setAdapter(selectedCarsAdapter);


        /*driverNameTv.setText(driverList.get(currentDriverPosition).getFirstName() + " " + driverList.get(currentDriverPosition).getLastName());
        driverPhoneTv.setText(driverList.get(currentDriverPosition).getMobile());
        driverRatingBar.setRating(Float.parseFloat(driverList.get(currentDriverPosition).getRating()));
        if (!driverList.get(currentDriverPosition).getImg().equals(""))

            ImageLoader.getInstance().displayImage(driverList.get(currentDriverPosition).getImg(), driverPicIv, options);
        else
            driverPicIv.setImageResource(R.drawable.defalut_bg);

        changeDriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentDriverPosition < driverList.size() - 1) {
                    currentDriverPosition++;
                } else {
                    currentDriverPosition = 0;
                }

                driverNameTv.setText(driverList.get(currentDriverPosition).getFirstName() + " " + driverList.get(currentDriverPosition).getLastName());
                driverPhoneTv.setText(driverList.get(currentDriverPosition).getMobile());
                driverRatingBar.setRating(Float.parseFloat(driverList.get(currentDriverPosition).getRating()));
                if (!driverList.get(currentDriverPosition).getImg().equals(""))

                    ImageLoader.getInstance().displayImage(driverList.get(currentDriverPosition).getImg(), driverPicIv, options);
                else
                    driverPicIv.setImageResource(R.drawable.defalut_bg);
            }
        });*/


        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.show();
    }


    private void saveRequest() {
        try {
            JSONObject jsonObject = null;

            jsonObject = new JSONObject();
            jsonObject.put("booking_type", myRequestDataBean.getRequest_type());
            jsonObject.put("booking_date", myRequestDataBean.getRequest_date());
            jsonObject.put("booking_time", Util.Time24to12WithLocale(myRequestDataBean.getRequest_time().split(" - ")[0]));
            jsonObject.put("booking_address", myRequestDataBean.getRequest_address());
            jsonObject.put("latitude", myRequestDataBean.getRequest_lat());
            jsonObject.put("longitude", myRequestDataBean.getRequest_long());
            jsonObject.put("charges", new BigDecimal(Util.PayAmount(myRequestDataBean.getRequest_amount())));
            jsonObject.put("fair_review", myRequestDataBean.getFareReviewObj().toString());
            jsonObject.put("start_time", Util.Time24to12WithLocale(myRequestDataBean.getRequest_time().split(" - ")[0]));
            jsonObject.put("end_time", Util.Time24to12WithLocale(myRequestDataBean.getRequest_time().split(" - ")[1]));
            jsonObject.put("booking_time_zone", TimeZone.getDefault().getID().toString());
            jsonObject.put("service_provider_id", myRequestDataBean.getDriverId());

            if (myRequestDataBean.isVehicle()) {
                jsonObject.put("user_vehicle_id", TextUtils.join(",", myRequestDataBean.getSelect_car_ids()));
                jsonObject.put("vehicle_type_id", "");
            } else {
                jsonObject.put("user_vehicle_id", "");
                jsonObject.put("vehicle_type_id", TextUtils.join(",", myRequestDataBean.getSelect_car_ids()));
            }

            jsonObject.put("payment_type", myRequestDataBean.getPaymentMode().toString());
            ArrayList<String> promo_ids = new ArrayList<>();
            ArrayList<String> promo_percentages = new ArrayList<>();
            ArrayList<String> promo_amounts = new ArrayList<>();

            for (int i = 0; i < myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").length(); i++) {
                promo_ids.add(myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").getJSONObject(i).getString("promoCodeId"));
                promo_percentages.add(myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").getJSONObject(i).getString("discount"));
                promo_amounts.add(myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").getJSONObject(i).getString("discountAmount"));
            }

            jsonObject.put("promo_id", TextUtils.join(",", promo_ids));
            jsonObject.put("promo_percentage", TextUtils.join(",", promo_percentages));
            if (promo_amounts.size() > 0) {
                jsonObject.put("promo_amount", TextUtils.join(",", promo_amounts));
            } else {
                jsonObject.put("promo_amount", "0");
            }

            MySharedPreferences.setRequest(context, jsonObject.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callWebserviceBooking(final Dialog booking_dialog) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject.put("language", MySharedPreferences.getLanguage(getActivity()));
            jsonObject.put("booking_type", myRequestDataBean.getRequest_type());
            jsonObject.put("booking_date", myRequestDataBean.getRequest_date());
            jsonObject.put("booking_time", Util.Time24to12WithLocale(myRequestDataBean.getRequest_time().split(" - ")[0]));
            jsonObject.put("booking_address", myRequestDataBean.getRequest_address());
            jsonObject.put("latitude", myRequestDataBean.getRequest_lat());
            jsonObject.put("longitude", myRequestDataBean.getRequest_long());
            jsonObject.put("charges", new BigDecimal(Util.PayAmount(myRequestDataBean.getRequest_amount())));
            jsonObject.put("fair_review", myRequestDataBean.getFareReviewObj().toString());
            jsonObject.put("start_time", Util.Time24to12WithLocale(myRequestDataBean.getRequest_time().split(" - ")[0]));
            jsonObject.put("end_time", Util.Time24to12WithLocale(myRequestDataBean.getRequest_time().split(" - ")[1]));
            jsonObject.put("booking_time_zone", TimeZone.getDefault().getID().toString());
            jsonObject.put("service_provider_id", myRequestDataBean.getDriverId());

            if (myRequestDataBean.isVehicle()) {
                jsonObject.put("user_vehicle_id", TextUtils.join(",", myRequestDataBean.getSelect_car_ids()));
                jsonObject.put("vehicle_type_id", "");
            } else {
                jsonObject.put("user_vehicle_id", "");
                jsonObject.put("vehicle_type_id", TextUtils.join(",", myRequestDataBean.getSelect_car_ids()));
            }

            jsonObject.put("payment_type", myRequestDataBean.getPaymentMode().toString());
            ArrayList<String> promo_ids = new ArrayList<>();
            ArrayList<String> promo_percentages = new ArrayList<>();
            ArrayList<String> promo_amounts = new ArrayList<>();

            for (int i = 0; i < myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").length(); i++) {
                promo_ids.add(myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").getJSONObject(i).getString("promoCodeId"));
                promo_percentages.add(myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").getJSONObject(i).getString("discount"));
                promo_amounts.add(myRequestDataBean.getFareReviewObj().getJSONArray("promoCodes").getJSONObject(i).getString("discountAmount"));
            }

            jsonObject.put("promo_id", TextUtils.join(",", promo_ids));
            jsonObject.put("promo_percentage", TextUtils.join(",", promo_percentages));
            if (promo_amounts.size() > 0) {
                jsonObject.put("promo_amount", TextUtils.join(",", promo_amounts));
            } else {
                jsonObject.put("promo_amount", "0");
            }
            Log.e("req : ", "req : " + jsonObject);


            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.bookingSavesWithSlot(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mjJsonObject = new JSONObject(new String(response.body().bytes()));

                        if (mjJsonObject.getJSONObject("response").getBoolean("status")) {
                            booking_dialog.dismiss();

                            JSONObject jsonObject1 = new JSONObject();
                            jsonObject1 = mjJsonObject.getJSONObject("response").getJSONArray("data").getJSONObject(0);

                            final Dialog dialog = new Dialog(context);
                            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.order_complete_dialog);
                            CustomTexViewRegular orderCompleteTv;
                            CustomTexViewBold okTv;
                            orderCompleteTv = (CustomTexViewRegular) dialog.findViewById(R.id.orderCompleteTv);
                            okTv = (CustomTexViewBold) dialog.findViewById(R.id.okTv);
                            orderCompleteTv.setText(mjJsonObject.getJSONObject("response").getString("message"));
                            okTv.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    cancelbookingCustomer();
                                    dialog.dismiss();
                                }
                            });

                            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            Window window = dialog.getWindow();
                            window.setGravity(Gravity.CENTER);
                            dialog.show();

                            /*if (myRequestDataBean.getRequest_type().compareTo("1") == 0) {
                                //reference_id = mjJsonObject.getJSONObject("response").getJSONArray("data").getJSONObject(0).getString("reference_id");
                                booking_id = mjJsonObject.getJSONObject("response").getJSONArray("data").getJSONObject(0).getString("booking_id");

                                if (serviceProgressLayout.getVisibility() != View.VISIBLE) {
                                    CustomerWaitingForAccept();
                                }

                            } else
                            {
                                //   cancelbookingCustomer();
                                Util.showAlertDialog(context, getString(R.string.alert), mjJsonObject.getJSONObject("response").getString("message"));

                            }*/

                        } else {
                            //  cancelbookingCustomer();
                            Util.showAlertDialog(context, getString(R.string.app_name), mjJsonObject.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        // cancelbookingCustomer();
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // cancelbookingCustomer();
                    Util.dismissPrograssDialog(progressDialog);
                }
            });
        } catch (Exception e) {
            // cancelbookingCustomer();
            Log.e(TAG, e.toString());
        }
    }

    private void callWebserviceBookingFromSkip() {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject(MySharedPreferences.getRequest(context));
            jsonObject.put("user_id", MySharedPreferences.getUserId(getActivity()));

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.bookingSavesWithSlot(new ConvertJsonToMap().jsonToMap(jsonObject));
            call.enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mjJsonObject = new JSONObject(new String(response.body().bytes()));
                        MySharedPreferences.setRequest(context, "");

                        if (mjJsonObject.getJSONObject("response").getBoolean("status")) {


                            //   cancelbookingCustomer();
                            Util.showAlertDialog(context, getString(R.string.alert), mjJsonObject.getJSONObject("response").getString("message"));
                            cancelbookingCustomer();
                        } else {
                            //  cancelbookingCustomer();
                            Util.Toast(context, mjJsonObject.getJSONObject("response").getString("message"));
                        }
                    } catch (Exception e) {
                        // cancelbookingCustomer();
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // cancelbookingCustomer();
                    Util.dismissPrograssDialog(progressDialog);
                }
            });
        } catch (Exception e) {
            // cancelbookingCustomer();
            Log.e(TAG, e.toString());
        }
    }

    public byte[] getBytesFromInputStream(InputStream inputStream) throws IOException {
        try {
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } catch (OutOfMemoryError error) {
            return null;
        }
    }


    @Override
    public void timeSlotSelect(int slot) {
        timeTv.setText(Util.Time24to12(timeSlotArray.get(slot).getTime_slot()));
        myRequestDataBean.setDriverId(timeSlotArray.get(slot).getServiceProviderId());
        if (timeSlotdialog != null)
            timeSlotdialog.dismiss();

    }

    public static class FromToPickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String from_time = pad(hourOfDay) + ":" + pad(minute) + ":00";
            //timeTv.setText(from_time);
        }
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void checkRequestPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            }, 1);
        } else {
            getLastLocation();

            AutoSpaApplication.location = LocationServices.FusedLocationApi.getLastLocation(AutoSpaApplication.mGoogleApiClient);
            initilizeMap();
            // GoNext();
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        AutoSpaApplication.mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            AutoSpaApplication.mLastLocation = task.getResult();

                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        int i = 0;
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                checkRequestPermission();

            } else {
            }
        } else if (requestCode == 2) {
            callDriver();
        }
    }

    private void initilizeMap() {
        try {
            //MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
            SupportMapFragment mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
            mapFragment.getMapAsync(this);

            mapView = mapFragment.getView();


        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    public void onMapReady(GoogleMap Map) {
        googleMap = Map;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        SetMapLocation();

        if (mapView != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            // position on right bottom
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlp.setMargins(0, 0, 30, selectVehicleServiceTv.getHeight() + 50);

           /* if (locationEt.getHeight() == 0) {
                rlp.setMargins(0, 0, 30, 400);
            } else {
                rlp.setMargins(0, 0, 30, locationEt.getHeight() + selectVehicleServiceTv.getHeight() + 50);
            }*/

        }

        if (recive_object != null) {
            MyBookingChildModal modal = (MyBookingChildModal) recive_object;
            SetCustomerWash(modal);
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (AutoSpaApplication.mLastLocation != null) {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(AutoSpaApplication.mLastLocation.getLatitude(), AutoSpaApplication.mLastLocation.getLongitude()), 15), 1000, null);
                }
            }
        }, 2000);


        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                if (booking_id.compareTo("") == 0) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            hasPickUpLocationResult = false;
                        }
                    }, 500);
                    mCenterLatLong = googleMap.getCameraPosition().target;

                    try {

                        Location mLocation = new Location("");
                        mLocation.setLatitude(mCenterLatLong.latitude);
                        mLocation.setLongitude(mCenterLatLong.longitude);

                        if (mlastlocation == null) {
                            setLocationMap(mLocation);
                        } else {
                            if (mlastlocation.distanceTo(mLocation) > 50) {
                                setLocationMap(mLocation);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        if (booking_id.compareTo("") == 0) {
            //  getNearBySerivceProvider();
        }


    }

    private void getNearBySerivceProvider() {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("latitude", AutoSpaApplication.mLastLocation.getLatitude());
            jsonObject.put("longitude", AutoSpaApplication.mLastLocation.getLongitude());
            jsonObject.put("language", MySharedPreferences.getLanguage(context));

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.finNearServiceProvider(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            SetNearSericeProvider(mJsonObj.getJSONObject("response").getJSONArray("data"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
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

    public void setLocationMap(Location locationMap) {

        startIntentService(locationMap);
        //  googleMap.clear();
     /*   if (AutoSpaApplication.mLastLocation != null) {
            for (int i = 0; i < 5; i++) {
                googleMap.addMarker(new MarkerOptions().position(getLocation(mCenterLatLong.latitude, mCenterLatLong.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.pingreen)));
            }
        }
*/
        //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mCenterLatLong.latitude,mCenterLatLong.longitude), 15), 1000, null);
        mlastlocation = locationMap;
    }


    protected void startIntentService(Location mLocation) {
        try {
            if (getActivity() != null) {
                Intent intent = new Intent(getActivity(), FetchAddressIntentService.class);
                intent.putExtra(AppUtils.LocationConstants.RECEIVER, mResultReceiver);
                intent.putExtra(AppUtils.LocationConstants.LOCATION_DATA_EXTRA, mLocation);
                getActivity().startService(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SetMapLocation() {
        if (AutoSpaApplication.mLastLocation != null) {
            LatLng sydney = new LatLng(AutoSpaApplication.mLastLocation.getLatitude(), AutoSpaApplication.mLastLocation.getLongitude());
            /*googleMap.addMarker(new MarkerOptions().position(sydney).title(MySharedPreferences.getUserName(context))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))).setTag(-1);*/
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12), 1000, null);
            CUREENTLOCATION = true;
        }
    }

    private void changeMapLocation(LatLng latLng) {
        if (latLng != null) {
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(latLng).title(MySharedPreferences.getFirstName(context) + " " + MySharedPreferences.getLastName(context))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenlocation))).setTag(-1);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12), 1000, null);
            CUREENTLOCATION = true;
        }
    }

    private void initView(View rootview) {

        ll_booking_operation = rootview.findViewById(R.id.ll_booking_operation);
        rl_main = rootview.findViewById(R.id.rl_main);
        btn_done = rootview.findViewById(R.id.btn_done);

        imgDirection = (ImageView) rootview.findViewById(R.id.imgDirection);
        actionbar = (RelativeLayout) rootview.findViewById(R.id.actionbar);
        toggleIcon = (ImageView) rootview.findViewById(R.id.toggle_icon);
        serviceProgressCancelTv = (TextView) rootview.findViewById(R.id.service_progress_cancel_tv);
        serviceProgressLayout = (LinearLayout) rootview.findViewById(R.id.service_progress_layout);
        serviceProgressStatusTv = (TextView) rootview.findViewById(R.id.service_progress_status_tv);
        serviceProgressTimeTv = (TextView) rootview.findViewById(R.id.service_progress_time_tv);
        progressReviewTextTv = (TextView) rootview.findViewById(R.id.progress_review_text_tv);
        viewDetailTv = (TextView) rootview.findViewById(R.id.viewDetailTv);
        progressRatingBar = (RatingBar) rootview.findViewById(R.id.progress_rating_bar);
        progressReviewNameTv = (TextView) rootview.findViewById(R.id.progress_review_name_tv);
        seviceProviderCallIv = (ImageView) rootview.findViewById(R.id.sevice_provider_call_iv);
        progressUserPicIv = (CircleImageView) rootview.findViewById(R.id.progress_user_pic_iv);
        selectVehicleServiceLayout = (LinearLayout) rootview.findViewById(R.id.select_vehicle_service_layout);
        selectVehicleServiceTv = (TextView) rootview.findViewById(R.id.select_vehicle_service_tv);
        reviewTextTv = (TextView) rootview.findViewById(R.id.review_text_tv);
        // spAvailTv = (TextView) rootview.findViewById(R.id.spAvailTv);
        ratingBar = (RatingBar) rootview.findViewById(R.id.rating_bar);
        userPicIv = (CircleImageView) rootview.findViewById(R.id.user_pic_iv);
        locationEt = (TextView) rootview.findViewById(R.id.locationEt);
        selectLocationTv = (TextView) rootview.findViewById(R.id.selectLocationTv);
        pinIv = (ImageView) rootview.findViewById(R.id.pinIv);

        timeSlotArray = new ArrayList<>();
        timeSlotArray.clear();
        promoCodeList = new ArrayList<>();
        promoCodeList.clear();
        //   driverList = new ArrayList<>();
        myRequestDataBean = new MyRequestDataBean();
        cars_list = new ArrayList<>();
        cars_list.clear();

        locationEt.getHeight();
        selectVehicleServiceTv.getHeight();

        imgDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lat = "", lng = "";
                if (LocationService.LATEST_LOC_OBJ != null) {
                    lat = String.valueOf(LocationService.LATEST_LOC_OBJ.getLatitude());
                    lng = String.valueOf(LocationService.LATEST_LOC_OBJ.getLongitude());
                } else {
                    lat = MySharedPreferences.getLatitude(context);
                    lng = MySharedPreferences.getLongitude(context);
                }
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + lat + "," + lng + "&daddr=" + custumer_lat + "," + custumerLng;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "Select an application"));
            }
        });


    }

    private void callWebServiceShowVehicleList() {
        Util.showProgress(getActivity());
        Call<ResponseBody> call = null;
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = new JSONObject();
            jsonObject1.put("user_id", MySharedPreferences.getUserId(getActivity()));
            jsonObject1.put("language", MySharedPreferences.getLanguage(getActivity()));

            MyApiEndpointInterface myApiEndpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = myApiEndpointInterface.vehicleList(new ConvertJsonToMap().jsonToMap(jsonObject1));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Util.hideProgress();
                        JSONObject jsonObject11 = new JSONObject(response.body().string());
                        if (jsonObject11.getJSONObject("response").getBoolean("status")) {
                            JSONArray jsonArray = jsonObject11.getJSONObject("response").
                                    getJSONArray("data");
                            receiveVehicleList(jsonArray);
                        } else {
                            // callWebServiceShowVechicleType();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }


    public void receiveVehicleList(JSONArray jsonArray) {
        cars_list.clear();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BeanCarsNew vechicleListModal = new BeanCarsNew();
                JSONObject jsonObject1 = jsonObject.optJSONObject("VehicleType");
                vechicleListModal.setName(jsonObject1.optString("name"));
                vechicleListModal.setV_id(jsonObject1.optString("id"));
                vechicleListModal.setV_name(jsonObject1.optString("name"));

                JSONObject jsonMake = jsonObject.optJSONObject("Make");
                vechicleListModal.setMake(jsonMake.optString("name"));

                JSONObject jsonModel = jsonObject.optJSONObject("VehicleModel");
                vechicleListModal.setModel(jsonModel.optString("name"));

                JSONObject jsonInfo = jsonObject.optJSONObject("UserVehicle");
                Log.e("vehicle_data", String.valueOf(jsonInfo));
                vechicleListModal.setId(jsonInfo.getString("id"));
                vechicleListModal.setMake_id(jsonInfo.getString("make_id"));
                vechicleListModal.setPlate_number(jsonInfo.getString("plate_number"));
                vechicleListModal.setColor(jsonInfo.getString("color"));
                vechicleListModal.setPhoto(jsonInfo.getString("vehicle_picture"));
                vechicleListModal.setVehicle_type_id(jsonInfo.optString("vehicle_type_id"));//Vehicle_type
                vechicleListModal.setClick(false);
                cars_list.add(vechicleListModal);
            }

            /*for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BeanCars beanCars = new BeanCars();
                beanCars.setCar_name(jsonObject.getString("model") + " (" + jsonObject.getJSONObject("vehicle_type_id").getString("name") + ")");
                beanCars.setCar_id(jsonObject.getString("id"));
                beanCars.setCar_image(jsonObject.getString("vehicle_picture"));
                beanCars.setCar_amount(jsonObject.getJSONObject("vehicle_type_id").getString("amount"));
                beanCars.setIs_available(false);
                beanCars.setVehicle(true);

                if (i == 0) {
                    beanCars.setHeader(true);
                } else {
                    beanCars.setHeader(false);
                }


                if (jsonObject.getJSONObject("vehicle_type_id").getString("status").compareTo("1") == 0) {
                    cars_list.add(beanCars);
                }
            }
*/
            if (cars_list.size() > 0) {
                selectWeekdaysAdpater.notifyDataSetChanged();

            }


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void callNearByServiceProvider(double lat, double lng) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("latitude", lat);
            jsonObject.put("longitude", lng);
            jsonObject.put("language", MySharedPreferences.getLanguage(context));
            jsonObject.put("sel_date", myRequestDataBean.getRequest_date());

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.getNearestSpByBranch(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            getTimeSlotArray(mJsonObj);

                            //SetNearSericeProvider(mJsonObj.getJSONObject("response").getJSONArray("data"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //  Util.dismissPrograssDialog(progressDialog);
                    t.printStackTrace();
                }
            });

        } catch (Exception e) {
            // Util.dismissPrograssDialog(progressDialog);
            e.printStackTrace();
        }
    }

    private void getTimeSlotArray(JSONObject mJsonObj) {
        try {
            timeSlotArray.clear();
//Dynamic Data
            /*JSONArray jsonArray = mJsonObj.getJSONObject("response").getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                BeanTimeSlot beanTimeSlot = new BeanTimeSlot();
                JSONObject jsonObject = new JSONObject();
                jsonObject = jsonArray.getJSONObject(i);
                beanTimeSlot.setTime_slot(jsonObject.getString("slot_start_time") + " - " + jsonObject.getString("slot_end_time"));
                beanTimeSlot.setServiceProviderId(jsonObject.getString("service_provider_id"));
                beanTimeSlot.setAvailStatus(jsonObject.getString("avail_status"));
                timeSlotArray.add(beanTimeSlot);
            }*/
//Static Data
            for (int i = 0; i < 10; i++) {
                BeanTimeSlot beanTimeSlot = new BeanTimeSlot();
                beanTimeSlot.setTime_slot("10:00 AM" + " - " + "11:00 PM");
                beanTimeSlot.setServiceProviderId("1");
                beanTimeSlot.setAvailStatus("1");
                timeSlotArray.add(beanTimeSlot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CallWebserviceCancelBooking(String booking_id) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("language", MySharedPreferences.getLanguage(context));

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.autoCancelBooking(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            //SetNearSericeProvider(mJsonObj.getJSONObject("response").getJSONArray("data"));
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
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

    private void SetNearSericeProvider(JSONArray jsonArray) {
        //  driverList.clear();
        googleMap.clear();
        if (jsonArray.length() > 0) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject driverObject = new JSONObject();
                    driverObject = jsonArray.getJSONObject(i);
                    BeanDrivers beanDrivers = new BeanDrivers();

                    if (!driverObject.getString("latitude").equalsIgnoreCase("null") && !driverObject.getString("longitude").equalsIgnoreCase("null")) {
                        LatLng latlang = new LatLng(Double.parseDouble(driverObject.getString("latitude")), Double.parseDouble(driverObject.getString("longitude")));
                        Marker marker;
                        int height = 300;
                        int width = 300;
                        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.greenlocation);
                        Bitmap b = bitmapdraw.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                        marker = googleMap.addMarker(new MarkerOptions().position(latlang).title(driverObject.
                                getString("first_name") + " " + driverObject.getString("last_name"))
                                .icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
                        marker.setTag("1");
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            }
            //  googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(AutoSpaApplication.mLastLocation.getLatitude(), AutoSpaApplication.mLastLocation.getLongitude()), 12), 1000, null);
            //  googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBuilder.build(), 10));
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
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

    public void getNoticationData(String notification_data) {
        try {
            JSONObject jsonObject = new JSONObject(notification_data);
            int notification_type = 0;
            notification_type = Integer.parseInt(jsonObject.getString("type"));
            MySharedPreferences.setNotification(context, "");

            if (notification_type == 2)// notification for complete wash
            {
                MySharedPreferences.setBookingDetails(context, notification_data);
                MySharedPreferences.setRatingStatus(context, true);

                SsetOnLoadData();
                // ((MainActivity) getActivity()).displayView(AutoSpaConstant.HOMEFRAGMENT, null, null);

            } else if (notification_type == 3) // Accepted your request
            {
                showRequestAcceptedDialog(jsonObject);
            } else if (notification_type == 4) // Cancel your request
            {
                CancelBookingResponse(notification_data);
            } else if (notification_type == 5) // Car wash Started
            {
                showRequestStartedDialog(notification_data);
            } else if (notification_type == 6) // Cancel your request
            {
                CancelBookingResponse(notification_data);
            } else if (notification_type == 9) // Cancel your request
            {
                showRequestReachedDialog(notification_data);
            } else if (notification_type == 11) // Cancel your request
            {
                CancelBookingResponse(notification_data);
            } else if (notification_type == 7) // Car wash Complete
            {
                //((MainActivity) context).displayView(AutoSpaConstant.WASHCOMPLETEFRAGMENT, notification_data, null);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void CancelBookingResponse(String notification_data) {
        try {
            JSONObject jsonObject = new JSONObject(notification_data);
            if (booking_id.compareTo(jsonObject.getString("booking_id")) == 0) {
                cancelbookingCustomer();
            }
            MySharedPreferences.setNotification(context, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelbookingCustomer() {

        ((MainActivity) getActivity()).displayView(AutoSpaConstant.HOMEFRAGMENT, null, null);

    }

    @SuppressLint("SetTextI18n")
    private void showRequestAcceptedDialog(JSONObject jsonObject) {
        try {
            myRequestDataBean = new MyRequestDataBean();
            is_cancel = false;
            if (waiting_anim != null) {
                waiting_anim.cancel();
            }
            if (waitingdialog1 != null) {
                if (waitingdialog1.isShowing()) {
                    waitingdialog1.cancel();
                }
            }
            selectVehicleServiceLayout.setVisibility(View.GONE);
            selectLocationTv.setVisibility(View.GONE);
            pinIv.setVisibility(View.GONE);
            serviceProgressLayout.setVisibility(View.VISIBLE);
            serviceProgressCancelTv.setVisibility(View.GONE);//VISIBLE
            serviceProgressStatusTv.setText(R.string.sweater_on_the_way);
            seviceProviderCallIv.setVisibility(View.VISIBLE);
            booking_id = jsonObject.getString("booking_id");
            booking_driver_id = jsonObject.getString("Booking_driver_id");

            progressReviewTextTv.setText(jsonObject.getString("sp_name"));
            progressRatingBar.setRating(Float.parseFloat(jsonObject.getString("sp_rating")));

            progressReviewNameTv.setText(Util.SetRating(jsonObject.getString("sp_rating")) + "/5");

            provider_call_number = jsonObject.getString("sp_mobile");


            setMapLocation(jsonObject.getString("sp_lat"), jsonObject.getString("sp_long"), jsonObject.getString("booing_latitude"), jsonObject.getString("booing_longitude"));

            try {
                ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + jsonObject.getString("sp_profile"), progressUserPicIv, options);
            } catch (Exception e) {
                progressUserPicIv.setImageResource(R.drawable.user);
            }
            try {
                findDistance(jsonObject.getString("booing_longitude"), jsonObject.getString("booing_latitude"), jsonObject.getString("sp_lat"), jsonObject.getString("sp_long"));
                //findDistance("26.855297", "75.7115217", jsonObject.getString("sp_latitude"), jsonObject.getString("sp_longitude"));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findDistance(String sourecelat, String sourcelng, String destinationlat, String destinationLng) {
        progressDialog.show();
        Ion.with(context)
                .load("http://maps.googleapis.com/maps/api/directions/json?origin=" + sourecelat + "," + sourcelng + "&destination=" + destinationlat + "," + destinationLng + "&sensor=false")
                .asString().setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                Util.dismissPrograssDialog(progressDialog);
                if (e != null) {
                    Log.e(TAG, e.toString());
                }
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);
                        JSONArray routeArray = json.getJSONArray("routes");
                        JSONObject routes = routeArray.getJSONObject(0);

                        JSONArray newTempARr = routes.getJSONArray("legs");
                        JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

                        JSONObject distOb = newDisTimeOb.getJSONObject("distance");
                        JSONObject timeOb = newDisTimeOb.getJSONObject("duration");

                        Log.i("Diatance :", distOb.getString("text"));
                        Log.i("Time :", timeOb.getString("text"));

                        serviceProgressTimeTv.setText(timeOb.getString("text") + " " + getString(R.string.eta));
                        serviceProgressTimeTv.setVisibility(View.VISIBLE);
                        //txt_distance.setText(distOb.getString("text"));

                    } catch (Exception e1) {
                        Log.e(TAG, e1.toString());
                    }


                }
            }
        });
    }

    private void setMapLocation(String sp_latitude, String sp_longitude, String booking_lat, String booking_long) {
        googleMap.clear();
        LatLng sydney = new LatLng(Double.parseDouble(sp_latitude), Double.parseDouble(sp_longitude));
        googleMap.addMarker(new MarkerOptions().position(sydney)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pinred))).setTag(-1);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12), 1000, null);

        LatLng bookingLatlang = new LatLng(Double.parseDouble(booking_lat), Double.parseDouble(booking_long));
        googleMap.addMarker(new MarkerOptions().position(bookingLatlang)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenlocation))).setTag(1);
    }

    private void showRequestStartedDialog(String notification_data) {

        try {
            JSONObject jsonObject = new JSONObject(notification_data);

            booking_id = jsonObject.getString("booking_id");
            booking_driver_id = jsonObject.getString("Booking_driver_id");

            selectVehicleServiceLayout.setVisibility(View.GONE);
            selectLocationTv.setVisibility(View.GONE);
            pinIv.setVisibility(View.GONE);
            serviceProgressLayout.setVisibility(View.VISIBLE);
            serviceProgressCancelTv.setVisibility(View.GONE);
            serviceProgressTimeTv.setVisibility(View.GONE);
            serviceProgressStatusTv.setText(R.string.sweater_in_progress);
            seviceProviderCallIv.setVisibility(View.GONE);
            progressReviewTextTv.setText(jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
            progressRatingBar.setRating(Float.parseFloat(jsonObject.getString("rating")));

            progressReviewNameTv.setText(Util.SetRating(jsonObject.getString("rating")) + "/5");

            //provider_call_number = jsonObject.getString("sp_mobile");

            setMapLocation(jsonObject.getString("latitude"), jsonObject.getString("longitude"), AutoSpaApplication.mLastLocation.getLatitude() + "", AutoSpaApplication.mLastLocation.getLongitude() + "");

            try {
                ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + jsonObject.getString("customer_profile"), progressUserPicIv, options);
            } catch (Exception e) {
                progressUserPicIv.setImageResource(R.drawable.user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showRequestReachedDialog(String notification_data) {
        try {
            JSONObject jsonObject = new JSONObject(notification_data);

            booking_id = jsonObject.getString("booking_id");
            booking_driver_id = jsonObject.getString("Booking_driver_id");

            selectVehicleServiceLayout.setVisibility(View.GONE);
            selectLocationTv.setVisibility(View.GONE);
            pinIv.setVisibility(View.GONE);
            serviceProgressLayout.setVisibility(View.VISIBLE);
            serviceProgressCancelTv.setVisibility(View.GONE);//VISIBLE
            serviceProgressTimeTv.setVisibility(View.GONE);
            serviceProgressStatusTv.setText(R.string.reached);
            seviceProviderCallIv.setVisibility(View.VISIBLE);
            progressReviewTextTv.setText(jsonObject.getString("first_name") + " " + jsonObject.getString("last_name"));
            progressRatingBar.setRating(Float.parseFloat(jsonObject.getString("rating")));

            progressReviewNameTv.setText(Util.SetRating(jsonObject.getString("rating")) + "/5");

            provider_call_number = jsonObject.getString("mobile");

            setMapLocation(jsonObject.getString("latitude"), jsonObject.getString("longitude"), jsonObject.getString("booking_lat"), jsonObject.getString("booking_long"));

            try {
                ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + jsonObject.getString("customer_profile"), progressUserPicIv, options);
            } catch (Exception e) {
                progressUserPicIv.setImageResource(R.drawable.user);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void hideVisionCurrentLocation(int type) {
        try {
            switch (type) {
                case 1:
                    imgDirection.setVisibility(View.VISIBLE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    googleMap.setMyLocationEnabled(false);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    break;
                case 2:
                    imgDirection.setVisibility(View.GONE);
                    googleMap.setMyLocationEnabled(true);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102) {
            if (data != null) {
                cancelbookingCustomer();
            }
        }

        if (requestCode == 108) {
            if (data != null) {
                cancelbookingCustomer();
            }
        }

        if (requestCode == 105) {
            try {
                if (data != null) {
                    //AcceptNotification(new JSONObject(data.getStringExtra("data")), data.getStringExtra("booking_id"), data.getStringExtra("booking_sp_id"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 1000) {
            getActivity().startService(new Intent(getActivity(), LocationService.class));
        }

        if (requestCode == 200) {

            hasPickUpLocationResult = true;

            Place place = PlacePicker.getPlace(context, data);
            BeanAddresses beanAddresses = new BeanAddresses();
            beanAddresses.setAddress(place.getName() + ", " + place.getAddress() + "");
            beanAddresses.setLatitude(place.getLatLng().latitude + "");
            beanAddresses.setLongitude(place.getLatLng().longitude + "");

            locationEt.setText(place.getAddress());
            mBookingAddress = locationEt.getText().toString().trim();
            mBookingLat = place.getLatLng().latitude + "";
            mBookingLong = place.getLatLng().longitude + "";

            mCenterLatLong = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);


            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(place.getLatLng().latitude, place.getLatLng().longitude), 17), 1000, null);
            //  address_list.add(beanAddresses);
            // selectSerivceAdpater.notifyDataSetChanged();

        }

        if (requestCode == 500) {
            if (resultCode == getActivity().RESULT_OK) {
                callWebServiceShowVehicleList();
            }
        }
    }


    public void getFareReview(JSONObject dataJsonObject) {
        try {
            JSONObject jsonObject = new JSONObject(dataJsonObject.getString("fair_review"));

            ArrayList<com.poshwash.android.Beans.BeanReviewData> review_list = new ArrayList<>();
            review_list.clear();

            review_list = CalculatefareReviewData(jsonObject);


            final Dialog dialog;
            GetfareReviewAdapter getfareReviewAdapter;
            GetfarePromosAdapter getfarePromosAdapter;

            dialog = new Dialog(context);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.getfare_layout);
            ImageView back_icon = (ImageView) dialog.findViewById(R.id.back_icon);
            RecyclerView recylerview = (RecyclerView) dialog.findViewById(R.id.recylerview);
            RecyclerView promoRecylerview = (RecyclerView) dialog.findViewById(R.id.promoRecylerview);
            TextView paid_amount = (TextView) dialog.findViewById(R.id.paid_amount);
            TextView total_amount = (TextView) dialog.findViewById(R.id.total_amount);

            paid_amount.setText("" + jsonObject.getString("paidAmount"));
            total_amount.setText("" + jsonObject.getString("totalAmount"));

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

    public void SetCustomerWash(MyBookingChildModal myBookingChildModal) {
        this.booking_id = myBookingChildModal.getId();
        this.booking_driver_id = myBookingChildModal.getBooking_Sp_id();

        if (myBookingChildModal.getStatus().compareTo("9") == 0) // Driver Reached
        {
            try {
                myRequestDataBean = new MyRequestDataBean();
                is_cancel = false;
                if (waiting_anim != null) {
                    waiting_anim.cancel();
                }
                if (waitingdialog1 != null) {

                    if (waitingdialog1.isShowing()) {
                        waitingdialog1.cancel();
                    }
                }

                selectVehicleServiceLayout.setVisibility(View.GONE);
                selectLocationTv.setVisibility(View.GONE);
                pinIv.setVisibility(View.GONE);
                serviceProgressLayout.setVisibility(View.VISIBLE);
                serviceProgressTimeTv.setVisibility(View.GONE);
                serviceProgressCancelTv.setVisibility(View.GONE);//VISIBLE
                seviceProviderCallIv.setVisibility(View.VISIBLE);
                serviceProgressStatusTv.setText(getString(R.string.reached));

                JSONObject jsonObject = new JSONObject();

                progressReviewTextTv.setText(myBookingChildModal.getOwner_name());
                progressRatingBar.setRating(Float.parseFloat(myBookingChildModal.getRating_count()));
                progressReviewNameTv.setText(Util.SetRating(myBookingChildModal.getRating_count()) + "/5");

                provider_call_number = myBookingChildModal.getUser_phone();
                setMapLocation(myBookingChildModal.getUser_lat(), myBookingChildModal.getUser_lon(), myBookingChildModal.getBooking_lat(), myBookingChildModal.getBooking_long());

                try {
                    ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + myBookingChildModal.getProfile_image_url(), progressUserPicIv, options);
                } catch (Exception e) {
                    progressUserPicIv.setImageResource(R.drawable.defalut_bg);
                }
                try {
                    //findDistance(myBookingChildModal.getBooking_lat(), myBookingChildModal.getBooking_long(), myBookingChildModal.getUser_lat(), myBookingChildModal.getUser_lon());
                    //findDistance("26.855297", "75.7115217", jsonObject.getString("sp_latitude"), jsonObject.getString("sp_longitude"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (myBookingChildModal.getStatus().compareTo("1") == 0) // Start Wash
        {
            try {
                myRequestDataBean = new MyRequestDataBean();
                is_cancel = false;
                if (waiting_anim != null) {
                    waiting_anim.cancel();
                }
                if (waitingdialog1 != null) {

                    if (waitingdialog1.isShowing()) {
                        waitingdialog1.cancel();
                    }
                }

                selectVehicleServiceLayout.setVisibility(View.GONE);
                selectLocationTv.setVisibility(View.GONE);
                pinIv.setVisibility(View.GONE);
                serviceProgressLayout.setVisibility(View.VISIBLE);

                serviceProgressCancelTv.setVisibility(View.GONE);//VISIBLE
                seviceProviderCallIv.setVisibility(View.VISIBLE);
                serviceProgressStatusTv.setText(getString(R.string.sweater_on_the_way));

                progressReviewTextTv.setText(myBookingChildModal.getOwner_name());
                progressRatingBar.setRating(Float.parseFloat(myBookingChildModal.getRating_count()));
                progressReviewNameTv.setText(Util.SetRating(myBookingChildModal.getRating_count()) + "/5");

                provider_call_number = myBookingChildModal.getUser_phone();
                setMapLocation(myBookingChildModal.getUser_lat(), myBookingChildModal.getUser_lon(), myBookingChildModal.getBooking_lat(), myBookingChildModal.getBooking_long());

                try {
                    ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + myBookingChildModal.getProfile_image_url(), progressUserPicIv, options);
                } catch (Exception e) {
                    progressUserPicIv.setImageResource(R.drawable.defalut_bg);
                }
                try {
                    findDistance(myBookingChildModal.getBooking_lat(), myBookingChildModal.getBooking_long(), myBookingChildModal.getUser_lat(), myBookingChildModal.getUser_lon());
                    //findDistance("26.855297", "75.7115217", jsonObject.getString("sp_latitude"), jsonObject.getString("sp_longitude"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (myBookingChildModal.getStatus().compareTo("5") == 0) // Inprocess Wash
        {
            try {

                selectVehicleServiceLayout.setVisibility(View.GONE);
                selectLocationTv.setVisibility(View.GONE);
                pinIv.setVisibility(View.GONE);
                serviceProgressLayout.setVisibility(View.VISIBLE);
                serviceProgressCancelTv.setVisibility(View.GONE);
                serviceProgressTimeTv.setVisibility(View.GONE);
                seviceProviderCallIv.setVisibility(View.GONE);
                serviceProgressStatusTv.setText(getString(R.string.sweater_in_progress));

                progressReviewTextTv.setText(myBookingChildModal.getOwner_name());
                progressRatingBar.setRating(Float.parseFloat(myBookingChildModal.getRating_count()));

                progressReviewNameTv.setText(Util.SetRating(myBookingChildModal.getRating_count()) + "/5");

                provider_call_number = myBookingChildModal.getUser_phone();
                setMapLocation(myBookingChildModal.getUser_lat(), myBookingChildModal.getUser_lon(), myBookingChildModal.getBooking_lat(), myBookingChildModal.getBooking_long());

                try {
                    ImageLoader.getInstance().displayImage(GlobalControl.IMAGE_BASE_URL + myBookingChildModal.getProfile_image_url(), progressUserPicIv, options);
                } catch (Exception e) {
                    progressUserPicIv.setImageResource(R.drawable.defalut_bg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*APIs*/
    private void callWebServiceBookingDetail(String bookingId, String bookingDriverId) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("type", "1");
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("booking_id", bookingId);
            jsonObject.put("language", MySharedPreferences.getLanguage(context));
            jsonObject.put("booking_driver_id", bookingDriverId);

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.booking_detail(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        Log.e("tg", "response from server = " + mJsonObj.toString());
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {
                            getFareReview(mJsonObj.getJSONObject("response").getJSONObject("data"));

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
            e.printStackTrace();
        }
    }

    private void callWebserviceSendRating(String booking_id, String booking_driver_id, String rating, String review) {
        progressDialog.show();
        JSONObject jsonObject = null;
        Call<ResponseBody> call = null;
        try {
            jsonObject = new JSONObject();
            jsonObject.put("user_id", MySharedPreferences.getUserId(context));
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("booking_driver_id", booking_driver_id);
            jsonObject.put("rating", rating);
            jsonObject.put("language", MySharedPreferences.getLanguage(context));
            jsonObject.put("review", review);

            MyApiEndpointInterface endpointInterface = AutoSpaApplication.getInstance().getRequestQueue().create(MyApiEndpointInterface.class);
            call = endpointInterface.ratings(new ConvertJsonToMap().jsonToMap(jsonObject));

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Util.dismissPrograssDialog(progressDialog);
                    try {
                        JSONObject mJsonObj = new JSONObject(new String(response.body().bytes()));
                        if (mJsonObj.getJSONObject("response").getBoolean("status")) {

                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
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

    public LatLng getLocation(double lat, double lon) {

        int radius = 500;

        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(lat);

        double foundLongitude = new_x + lon;
        double foundLatitude = y + lat;
        System.out.println("Longitude: " + foundLongitude + "  Latitude: "
                + foundLatitude);

        return new LatLng(foundLatitude, foundLongitude);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (!hasPickUpLocationResult) {
                Log.e("Data : ", "Data : " + resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY));
                locationEt.setText(resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY));
                mBookingAddress = locationEt.getText().toString().trim();
                mBookingLat = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_LATITUDE) + "";
                mBookingLong = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_LONGITUDE) + "";
            }
        }
    }

   /* class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        *//**
     * Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
     *//*
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            locationEt.setText(resultData.getString(AppUtils.LocationConstants.RESULT_DATA_KEY));
            //   bundle=resultData.getBundle("latitute");
            //   bundle=resultData.getBundle("longitute");
            //   bundle =resultData;
          *//*  mAreaOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_AREA);

            mCityOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_CITY);
            mStateOutput = resultData.getString(AppUtils.LocationConstants.LOCATION_DATA_STATE);*//*

            //   displayAddressOutput();

            // Show a toast message if an address was found.
//            if (resultCode == AppUtils.LocationConstants.SUCCESS_RESULT) {
//                //  showToast(getString(R.string.address_found));
//            }
        }
    }*/
}

