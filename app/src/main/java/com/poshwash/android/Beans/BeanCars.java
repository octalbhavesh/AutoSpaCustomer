package com.poshwash.android.Beans;

/**
 * Created by abhinava on 1/30/2017.
 */

public class BeanCars {

    public String car_name = "";
    public String car_address = "";
    public String car_amount = "";
    public String car_lat = "";
    public String car_long = "";
    public String car_id = "";
    public String carImage = "";
    public boolean is_available = false;

    public boolean isVehicle() {
        return isVehicle;
    }

    public void setVehicle(boolean vehicle) {
        isVehicle = vehicle;
    }

    public boolean isVehicle = false;


    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public boolean isHeader = false;
    int carCount = 1;
    public String getCar_image() {
        return car_image;
    }

    public void setCar_image(String car_image) {
        this.car_image = car_image;
    }

    public String getCar_color() {
        return car_color;
    }

    public void setCar_color(String car_color) {
        this.car_color = car_color;
    }

    public String car_image = "";
    public String car_color = "";


    public String getCarImage() {
        return carImage;
    }

    public void setCarImage(String carImage) {
        this.carImage = carImage;
    }

    public String getCar_amount() {
        return car_amount;
    }

    public void setCar_amount(String car_amount) {
        this.car_amount = car_amount;
    }

    public String getCar_address() {
        return car_address;
    }

    public void setCar_address(String car_address) {
        this.car_address = car_address;
    }

    public String getCar_lat() {
        return car_lat;
    }

    public void setCar_lat(String car_lat) {
        this.car_lat = car_lat;
    }

    public String getCar_long() {
        return car_long;
    }

    public void setCar_long(String car_long) {
        this.car_long = car_long;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public boolean is_available() {
        return is_available;
    }

    public void setIs_available(boolean is_available) {
        this.is_available = is_available;
    }

    public int getCarCount() {
        return carCount;
    }

    public void setCarCount(int carCount) {
        this.carCount = carCount;
    }


    /**
     * Created by anandj on 4/5/2017.
     */
}
