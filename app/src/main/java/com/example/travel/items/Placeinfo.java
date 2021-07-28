package com.example.travel.items;

import java.io.Serializable;
import java.util.ArrayList;

public class Placeinfo implements Serializable {
    private String _id, region, title, address, latitude, longtitude, memo;
    private ArrayList<String> image, participants;
    private ArrayList<Integer> orientation;

    public Placeinfo(String _id, ArrayList<String> participants, String region, String title, String address, String latitude, String longtitude, String memo, ArrayList<String> image) {
        this._id = _id;
        this.participants = participants;
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

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
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
