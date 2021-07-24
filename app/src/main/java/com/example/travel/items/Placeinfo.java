package com.example.travel.items;

import java.io.Serializable;

public class Placeinfo implements Serializable {
    private String address, latitude, longtitude;

    public Placeinfo(String address, String latitude, String longtitude) {
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
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
}
