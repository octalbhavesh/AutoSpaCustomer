package com.poshwash.android.Beans;

/**
 * Created by anandj on 3/24/2017.
 */

public class VechicleListModal {
    int delete_status;
    String id = "", Vehicle_type = "", Vehicle_type_id = "", model = "",
            plate_number = "", color = "", photo = "",name="",make="",make_id="",model_id="";

    public String getMake_id() {
        return make_id;
    }

    public void setMake_id(String make_id) {
        this.make_id = make_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicle_type_id() {
        return Vehicle_type_id;
    }

    public void setVehicle_type_id(String vehicle_type_id) {
        Vehicle_type_id = vehicle_type_id;
    }

    public String getVehicle_type() {
        return Vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        Vehicle_type = vehicle_type;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getDelete_status() {
        return delete_status;
    }

    public void setDelete_status(int delete_status) {
        this.delete_status = delete_status;
    }
}
