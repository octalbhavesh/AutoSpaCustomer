package com.poshwash.android.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.poshwash.android.R;
import com.poshwash.android.Utils.Env;
import com.poshwash.android.Utils.JsonUtil;
import com.poshwash.android.Utils.Util;
import com.poshwash.android.databinding.ActivityTrackingBinding;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrackingActivity extends BaseActivity implements OnMapReadyCallback, LocationListener {

    private String driverImage,driverId, driverName, driverPhone;
    private ActivityTrackingBinding mBinding;
    public static String GOOGLE_DIRECTION_SERVERKEY = "AIzaSyDYMR6SlKbqP3PZ_YMN1DDmnyl-naAV_Js";
    private LatLng sourceLatLong, destLatLong;
    LatLng coordinate = null;
    private LatLng newLocation;
    private Marker driverMarker;
    public static LocationProvider locationProvider;
    SupportMapFragment mapFragment;// MapView UI element
    private GoogleMap mGoogleMap;// object that represents googleMap and allows us to use Google Maps API features
    private String sourceLat, sourceLong;
    private String destLat, destLong;
    public final static String PUBNUB_CHANNEL_NAME = "drivers_location";
    public final static String PUBNUB_PUBLISH_KEY = "pub-c-45bf7d6a-2bf9-48a9-a2a5-a7b4571d6c9f";//pub-c-7896b501-ac58-4754-8b0d-67b4205bdaa6
    public final static String PUBNUB_SUBSCRIBE_KEY = "sub-c-029df010-41a6-11e9-863f-d2b2becee13e";//sub-c-ecce2f1c-2944-11e9-9836-922834e26a5b

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_tracking);
        driverImage = getIntent().getExtras().getString("driver_image");
        driverId = getIntent().getExtras().getString("driver_id");
        driverName = getIntent().getExtras().getString("driver_name");
        driverPhone = getIntent().getExtras().getString("driver_phone");
        sourceLat = getIntent().getExtras().getString("source_lat");
        sourceLong = getIntent().getExtras().getString("source_long");
        destLat = getIntent().getExtras().getString("dest_lat");
        destLong = getIntent().getExtras().getString("dest_long");
        // locationProvider = new LocationProvider(Env.currentActivity, this, errorCallback);
        DisplayImageOptions options = null;

        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.user)
                .showImageForEmptyUri(R.drawable.user)
                .considerExifParams(true)
                .build();
        ImageLoader.getInstance().displayImage(driverImage, mBinding.trackingDriverImage, options);
       /* Picasso.with(mContext).load(driverImage)
                .into(mBinding.ivTrackingDriverImage);*/
        mBinding.tvTrackingName.setText(driverName);
        initilizeMap();
        mBinding.ivTrackingCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + driverPhone));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);
            }
        });

        mBinding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        Env.currentActivity = TrackingActivity.this;
        if (mapFragment != null) {
            mapFragment.onResume();
        }
       /* if (locationProvider != null) {
            locationProvider.onResume();
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mapFragment != null) {
            mapFragment.onPause();
        }
    }

    private void initilizeMap() {
        try {
            //MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           /* mapFragment.setListener(new WorkaroundMapFragment.OnTouchListener() {
                @Override
                public void onTouch() {
                    binding.scrollView.requestDisallowInterceptTouchEvent(true);
                }
            });*/
            mapFragment.getMapAsync(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    /*
         This method gets the new location of driver and calls method animateCar
         to move the marker slowly along linear path to this location.
         Also moves camera, if marker is outside of map bounds.
      */
    private void updateUI(Map<String, String> newLoc) {

        try {
            Log.e("newLoc : ", "newLoc : " + newLoc);
            newLocation = new LatLng(Double.valueOf(newLoc.get("lat")), Double.valueOf(newLoc.get("lng")));

           /* if (!isStartRide && hasDrawLineFromDriverToPickUp && pickUpLatLong != null && mGoogleMap != null) {
                createRouteOfDriverToUser();
            }*/

            if (mGoogleMap != null ) {
                //animateCar(newLocation);
               /* if (brng != 0.0f) {
                    updateCameraBearing(mGoogleMap, brng, newLocation);
                }*/
//                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
//                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 18f));

            } else if (mGoogleMap != null) {

                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(newLocation, 5f);
                mGoogleMap.animateCamera(yourLocation);

                int height = 120;
                int width = 60;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.greenlocation);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                MarkerOptions markerOptionsCurrentLocation = new MarkerOptions()
                        .position(newLocation).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).flat(true);
                driverMarker = mGoogleMap.addMarker(markerOptionsCurrentLocation);

            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void getDriverLocationViaPubNub() {
        try {
            // This code adds the listener and subscribes passenger to channel with driver's location.
            Util.pubnub.addListener(new SubscribeCallback() {
                @Override
                public void status(PubNub pub, PNStatus status) {
                }

                @Override
                public void message(PubNub pub, final PNMessageResult message) {
                    try {
                        Log.e("message : ", "message : " + message.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                        Map<String, String> newLocation =
                                                JsonUtil.fromJson(message.getMessage().toString(), LinkedHashMap.class);
                                        updateUI(newLocation);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void presence(PubNub pub, PNPresenceEventResult presence) {

                }
            });

            String channelName = PUBNUB_CHANNEL_NAME + "_" + driverId;
            Log.e("channelName : ", "channelName : " + channelName);
            Util.pubnub.subscribe()
                    .channels(Arrays.asList(channelName)) // subscribe to channels //drivers_location_64
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
        mGoogleMap.setMapStyle(style);

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.setIndoorEnabled(false);
        mGoogleMap.setBuildingsEnabled(true);
//        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);

        sourceLatLong = new LatLng(Double.parseDouble(sourceLat), Double.parseDouble(sourceLong));
        destLatLong = new LatLng(Double.parseDouble(destLat), Double.parseDouble(destLong));
        createRoutesOfPickUpDropOff(sourceLatLong, destLatLong);

        getDriverLocationViaPubNub();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private void createRoutesOfPickUpDropOff(final LatLng fromLocation, final LatLng toLocation) {
        try {
            GoogleDirection.withServerKey(GOOGLE_DIRECTION_SERVERKEY)
                    .from(fromLocation)
                    .to(toLocation)
                    .avoid(AvoidType.FERRIES)
                    .avoid(AvoidType.HIGHWAYS)
                    .transportMode(TransportMode.DRIVING)
                    .execute(new DirectionCallback() {
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
//                            mGoogleMap.clear();
                            if (direction.isOK()) {
                                Route route = direction.getRouteList().get(0);
                                MarkerOptions markerOptionsPickUp = new MarkerOptions().position(fromLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenlocation));
                                MarkerOptions markerOptionsDropOff = new MarkerOptions().position(toLocation)
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.greenlocation));
                                mGoogleMap.addMarker(markerOptionsPickUp);
                                mGoogleMap.addMarker(markerOptionsDropOff);
                                ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
                                mGoogleMap.addPolyline(DirectionConverter.createPolyline(Env.currentActivity, directionPositionList, 5, getResources()
                                        .getColor(R.color.skybluecolor)));
                                setCameraWithCoordinationBounds(route);
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
//                            Util.showCenteredToast("No Route found.");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setCameraWithCoordinationBounds(Route route) {
        if (mGoogleMap != null) {
            LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
            LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
            LatLngBounds bounds = new LatLngBounds(southwest, northeast);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }
}
