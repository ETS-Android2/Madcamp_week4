package com.example.travel.items;

public class PlaceItem {
    private String address, latitude, longtitude;

    public PlaceItem(String address, String latitude, String longtitude) {
        this.address = address;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getAddress(){ return address; }
}
