package com.poshwash.android.Beans;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abhinava on 4/11/2017.
 */

public class MyRequestDataBean {
    public ArrayList<BeanCars> selected_cars = new ArrayList<>();
    public ArrayList<String> select_car_ids = new ArrayList<>();
    public String request_type = "1";
    public String request_time = "";
    public String request_date = "";
    public int isScheduleReminder = 0;
    public String request_amount = "0.00";
    public ArrayList<BeanPromoCodes> promoCodeList = new ArrayList<>();
    public String promoCodeValue = "";
    public String promoCodeDesc = "";
    public String request_address = "";
    public String request_lat = "";
    public String request_long = "";
    public String driverId = "";
    public String paymentMode = "";

    public JSONObject getFareReviewObj() {
        return fareReviewObj;
    }

    public void setFareReviewObj(JSONObject fareReviewObj) {
        this.fareReviewObj = fareReviewObj;
    }

    public JSONObject fareReviewObj = new JSONObject();

    public boolean isVehicle() {
        return isVehicle;
    }

    public void setIsVehicle(boolean vehicle) {
        isVehicle = vehicle;
    }

    public boolean isVehicle = true;

    public ArrayList<BeanCars> getSelected_cars() {
        return selected_cars;
    }

    public void setSelected_cars(ArrayList<BeanCars> selected_cars) {
        this.selected_cars = selected_cars;
    }

    public ArrayList<String> getSelect_car_ids() {
        return select_car_ids;
    }

    public void setSelect_car_ids(ArrayList<String> select_car_ids) {
        this.select_car_ids = select_car_ids;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getRequest_date() {
        return request_date;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public int getIsScheduleReminder() {
        return isScheduleReminder;
    }

    public void setIsScheduleReminder(int isScheduleReminder) {
        this.isScheduleReminder = isScheduleReminder;
    }

    public String getRequest_amount() {
        return request_amount;
    }

    public void setRequest_amount(String request_amount) {
        this.request_amount = request_amount;
    }


    public ArrayList<BeanPromoCodes> getPromoCodeList() {
        return promoCodeList;
    }

    public void setPromoCodeList(ArrayList<BeanPromoCodes> promoCodeList) {
        this.promoCodeList = promoCodeList;
    }


    public String getPromoCodeValue() {
        return promoCodeValue;
    }

    public void setPromoCodeValue(String promoCodeValue) {
        this.promoCodeValue = promoCodeValue;
    }

    public String getPromoCodeDesc() {
        return promoCodeDesc;
    }

    public void setPromoCodeDesc(String promoCodeDesc) {
        this.promoCodeDesc = promoCodeDesc;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getRequest_address() {
        return request_address;
    }

    public void setRequest_address(String request_address) {
        this.request_address = request_address;
    }

    public String getRequest_lat() {
        return request_lat;
    }

    public void setRequest_lat(String request_lat) {
        this.request_lat = request_lat;
    }

    public String getRequest_long() {
        return request_long;
    }

    public void setRequest_long(String request_long) {
        this.request_long = request_long;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }


}
