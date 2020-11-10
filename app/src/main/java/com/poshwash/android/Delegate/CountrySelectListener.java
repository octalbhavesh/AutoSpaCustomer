package com.poshwash.android.Delegate;

import com.google.firebase.iid.FirebaseInstanceId;
import com.poshwash.android.Constant.AutoSpaConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by anandj on 3/15/2017.
 */

public interface CountrySelectListener {

    public void onCountryClick(int position);

    class APIRequest {

        public static Map<String, Object> register(String refercode, String mobile, String email, String first_name,
                                                   String last_name, String location, String language, String latitude,
                                                   String longtitude, String user_type, String social_id, String countryCode) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("referral_code", refercode);
            jsonObject.put("device_id", FirebaseInstanceId.getInstance().getToken());
            jsonObject.put("device_type", "Android");
            jsonObject.put("mobile", mobile);
            jsonObject.put("email", email);
            jsonObject.put("first_name", first_name);
            jsonObject.put("last_name", last_name);
            jsonObject.put("location", location);
            jsonObject.put("language", language);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longtitude);
            jsonObject.put("login_type", user_type);
            jsonObject.put("social_id", social_id);
            jsonObject.put("country_code", countryCode);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        /*customer-1,driver-2*/
        public static Map<String, Object> send_otp(String user_id, String otp, String lat1, String long1, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("otp", otp);
            jsonObject.put("language", language);
            jsonObject.put("device_type", "Android");
            jsonObject.put("device_id", FirebaseInstanceId.getInstance().getToken());
            jsonObject.put("location", "location name");
            jsonObject.put("latitude", lat1);
            jsonObject.put("longitude", long1);
            jsonObject.put("type", 1);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> re_send_otp(String mobile, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", mobile);
            jsonObject.put("language", language);
            jsonObject.put("country_code", "+91");
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> send_nonce(String user_id, String nonce, String amount, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("nonce", nonce);
            jsonObject.put("amount", amount);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> login(String mobile, String lat1, String long1,
                                                String language,String countryCode) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("mobile", mobile);
            jsonObject.put("device_id", FirebaseInstanceId.getInstance().getToken());
            jsonObject.put("device_type", "Android");
            jsonObject.put("language", language);
            jsonObject.put("location", "Manual");
            jsonObject.put("latitude", lat1);
            jsonObject.put("longitude", long1);
            jsonObject.put("country_code", countryCode);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> add_card(String user_id, String card_number, String month,
                                                String year,String card_type,String holder_name) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("language", "eng");
            jsonObject.put("user_id", user_id);
            jsonObject.put("card_number", card_number);
            jsonObject.put("month", month);
            jsonObject.put("year", year);
            jsonObject.put("card_type", card_type);
            jsonObject.put("card_holder_name", holder_name);

            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> static_page(String user_id, String page_slug, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("page_slug", page_slug);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> notification(String user_id, String user_type, String not_status, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("user_type", user_type);
            jsonObject.put("not_status", not_status);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> fetch_token(String user_id, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }


        public static Map<String, Object> notification_list(String user_id, String user_type, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("type", user_type);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> card_list(String user_id, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> rating(String user_id, String language, String booking_driver_id,
                                                 String booking_id, String rating) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("language", language);
            jsonObject.put("booking_driver_id", booking_driver_id);
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("rating", rating);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> nearest_driver(String user_id, String lat1, String long1, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("latitude", lat1);
            jsonObject.put("longitude", long1);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> cancel_booking(String user_id, String reason, String driver_id,
                                                         String booking_id, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("cancel_reason_text", reason);
            jsonObject.put("language", language);
            jsonObject.put("booking_driver_id", driver_id);
            jsonObject.put("booking_id", booking_id);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> booking_detail(String user_id, String driver_id, String booking_id, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("type", "1");
            jsonObject.put("booking_driver_id", driver_id);
            jsonObject.put("booking_id", booking_id);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> past_booking(String user_id, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> my_booking(String user_id, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("language", language);
            jsonObject.put("type", "1");
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> booking(String user_id, String vehicle_type_id, String date,
                                                  String long1, String payment_type, String address, String lat1,
                                                  String booking_type, String refer_code, String vehicle_id,
                                                  String time, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("booking_time", time);
            jsonObject.put("vehicle_type_id", vehicle_type_id);
            jsonObject.put("booking_date", date);
            jsonObject.put("longitude", long1);
            jsonObject.put("payment_type", payment_type);
            jsonObject.put("booking_address", address);
            jsonObject.put("user_id", user_id);
            jsonObject.put("latitude", lat1);
            jsonObject.put("booking_type", booking_type);
            jsonObject.put("refer_code", refer_code);
            jsonObject.put("user_vehicle_id", vehicle_id);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> apply_code(String user_id, String refer_code, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("refer_code", refer_code);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static RequestBody add_vehicle(String user_id, String model, String make, String plate,
                                              String color, String vehicle_id) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("vehicle_model_id", model);
            jsonObject.put("make_id", make);
            jsonObject.put("plate_number", plate);
            jsonObject.put("color", color);
            jsonObject.put("vehicle_type_id", vehicle_id);
            jsonObject.put("language", AutoSpaConstant.LANGUAGE);
            return RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.toString());
        }

        public static Map<String, Object> past_booking(String user_id, String user_type, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("type", user_type);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        public static Map<String, Object> get_package_details(String user_id, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("language", language);
            //  jsonObject.put("user_id", "20");
            jsonObject.put("user_id", user_id);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }


        /* wallet-3,card-2*/
        public static Map<String, Object> purchase_package(String user_id, String vehicle_type_id, String package_id,
                                                           String charge, String payment_type, String language) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("vehicle_type_id", vehicle_type_id);
            jsonObject.put("package_id", package_id);
            jsonObject.put("charges", charge);
            jsonObject.put("payment_type", payment_type);
            jsonObject.put("language", language);
            return new ConvertJsonToMap().jsonToMap(jsonObject);
        }

        /*public static RequestBody userEditProfile(String user_id, String first_name, String last_name,
                                                  String dob, String gender,
                                                  String bio, String language, String contact, String code) throws JSONException {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user_id", user_id);
            jsonObject.put("first_name", first_name);
            jsonObject.put("last_name", last_name);
            jsonObject.put("dob", dob);
            jsonObject.put("gender", gender);
            jsonObject.put("bio", bio);
            jsonObject.put("language", language);
            jsonObject.put("contact", contact);
            jsonObject.put("country_code", code);
            return RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject.toString());
        }*/

    }

    class ConvertJsonToMap {

        public Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
            Map<String, Object> retMap = new HashMap<>();

            if (json != JSONObject.NULL) {
                retMap = toMap(json);
            }
            return retMap;
        }

        public Map<String, Object> toMap(JSONObject object) throws JSONException {
            Map<String, Object> map = new HashMap<>();

            Iterator<String> keysItr = object.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                Object value = object.get(key);

                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                map.put(key, value);
            }
            return map;
        }

        public List<Object> toList(JSONArray array) throws JSONException {
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                Object value = array.get(i);
                if (value instanceof JSONArray) {
                    value = toList((JSONArray) value);
                } else if (value instanceof JSONObject) {
                    value = toMap((JSONObject) value);
                }
                list.add(value);
            }
            return list;
        }
    }
}