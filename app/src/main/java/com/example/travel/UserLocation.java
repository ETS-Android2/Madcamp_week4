package com.example.travel;

public class UserLocation {
    private String address;
    private String latitude;
    private String longtitude;

    public UserLocation(String address, String lat , String lng){
        this.address = address;
        this.latitude = lat;
        this.longtitude = lng;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getAddress() {
        return address;
    }
}
