package com.poshwash.android.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by monikab on 7/17/2015.
 */
public class MySharedPreferences {

    private final static String AUTOSPA_SHARED_PREFERENCE = "autospa";

    public static void ClearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void setUserId(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("UserId", unit).apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("UserId", "");
    }

    public static void setNotificationStatus(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("NotificationStatus", unit).apply();
    }

    public static String getNotificationStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("NotificationStatus", "");
    }


    //  Booking data //

    public static void setBookingAddress(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingAddress", unit).apply();
    }

    public static String getBookingAddress(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingAddress", "");
    }


    public static void setBookingLat(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingLat", unit).apply();
    }

    public static String getBookingLat(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingLat", "");
    }

    public static void setBookingLong(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingLong", unit).apply();
    }

    public static String getBookingLong(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingLong", "");
    }

    public static void setBookingVehicleTypeId(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingVehicleTypeId", unit).apply();
    }

    public static String getBookingVehicleTypeId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingVehicleTypeId", "");
    }

    public static void setBookingUserVehicleId(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingUserVehicleId", unit).apply();
    }

    public static String getBookingUserVehicleId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingUserVehicleId", "");
    }

    public static void setBookingUserVehicleName(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingUserVehicleName", unit).apply();
    }

    public static String getBookingUserVehicleName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingUserVehicleName", "");
    }

    public static void setBookingCode(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingCode", unit).apply();
    }

    public static String getBookingCode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingCode", "");
    }

    public static void setBookingUserPlate(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingUserPlate", unit).apply();
    }

    public static String getBookingUserPlate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingUserPlate", "");
    }

    public static void setBookingDate(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingDate", unit).apply();
    }

    public static String getBookingDate(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingDate", "");
    }

    public static void setBookingTime(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingTime", unit).apply();
    }

    public static String getBookingTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingTime", "");
    }

    ////////////////////////////////////
    public static void setLoginStatus(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("LoginStatus", unit).apply();
    }

    public static String getLoginStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("LoginStatus", "");
    }

    public static void setFirstName(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("FirstName", unit).apply();
    }

    public static String getFirstName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("FirstName", "");
    }

    public static void setLastName(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("LastName", unit).apply();
    }

    public static String getLastName(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("LastName", "");
    }

    public static void setUserEmail(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("UserEmail", unit).apply();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("UserEmail", "");
    }

    public static void setPhoneNumber(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("PhoneNumber", unit).apply();
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("PhoneNumber", "");
    }


    public static void setLanguage(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("Language", unit).apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("Language", "");
    }

    public static void setAddress(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("address", unit).apply();
    }

    public static String getAddress(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("address", "");
    }

    public static void setDeviceId(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("DeviceId", unit).apply();

    }

    public static String getDeviceId(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("DeviceId", "");
    }

    public static void setReferCode(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("ReferCode", unit).apply();

    }

    public static String getReferCode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("ReferCode", "");
    }


    public static void setIsFirstTime(Context context, String unit) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("isfirstTime", unit).apply();
    }

    public static String getIsFirstTime(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("isfirstTime", "");
    }

    public static void setProfileImage(Context context, String unit1) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("ProfileImage", unit1).apply();
    }

    public static String getProfileImage(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("ProfileImage", "");
    }

    public static void setLongitude(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("Longitude", UserName).apply();
    }

    public static String getLongitude(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("Longitude", "");
    }

    public static void setLatitude(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("Latitude", UserName).apply();
    }

    public static String getLatitude(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("Latitude", "");
    }

    public static void setRating(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("Rating", UserName).apply();
    }

    public static String getRating(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("Rating", "");
    }

    public static void setNotificationSetting(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("notificationSetting", UserName).apply();
    }

    public static String getNotificationSetting(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("notificationSetting", "");
    }

    public static void setSP_status(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("SP_status", UserName).apply();
    }

    public static String getSP_status(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("SP_status", "");
    }

    public static void setNotification(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("Notification", UserName).apply();
    }

    public static String getNotification(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("Notification", "");
    }

    public static void setRatingStatus(Context context, boolean UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putBoolean("RatingStatus", UserName).apply();
    }

    public static boolean getRatingStatus(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getBoolean("RatingStatus", false);
    }

    public static void setBookingDetails(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("BookingDetails", UserName).apply();
    }

    public static String getBookingDetails(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("BookingDetails", "");
    }

    public static void setLoginType(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("LoginType", UserName).apply();
    }

    public static String getLoginType(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("LoginType", "");
    }

    public static void setCountryCode(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("CountryCode", UserName).apply();
    }

    public static String getCountryCode(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("CountryCode", "");
    }

    public static void setNotificationCount(Context context, String NotificationCount) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("notificationCount", NotificationCount).apply();
    }

    public static String getNotificationCount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("notificationCount", "");
    }

    public static void setRequest(Context context, String UserName) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        sp.edit().putString("Request", UserName).apply();
    }

    public static String getRequest(Context context) {
        SharedPreferences sp = context.getSharedPreferences(AUTOSPA_SHARED_PREFERENCE, Context.MODE_PRIVATE);
        return sp.getString("Request", "");
    }
}