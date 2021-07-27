package com.example.travel.items;

import java.io.Serializable;
import java.util.ArrayList;

public class Placeinfo implements Serializable {
    private String _id, email, region, title, address, latitude, longtitude, memo;
    private ArrayList<String> image;
    private ArrayList<Integer> orientation;

    public Placeinfo(String _id, String email, String region, String title, String address, String latitude, String longtitude, String memo, ArrayList<String> image) {
        this._id = _id;
        this.email = email;
        this.region = region;
        this.title = title;
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.memo = memo;
        this.image = image;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<Integer> getOrientation() {
        return orientation;
    }

    public void setOrientation(ArrayList<Integer> image) {
        this.orientation = orientation;
    }
}
