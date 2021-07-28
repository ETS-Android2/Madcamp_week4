package com.example.travel.items;

import java.util.ArrayList;
import java.util.List;

public class Pathinfo {
    private String _id, email, title, region;
    private Integer totalSize;
    private ArrayList<PlaceItem> locations;

    public Pathinfo(String _id, String email, String title, String region, Integer totalSize, ArrayList<PlaceItem> locations) {
        this._id = _id;
        this.email = email;
        this.title = title;
        this.region = region;
        this.totalSize = totalSize;
        this.locations = locations;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
    }

    public ArrayList<PlaceItem> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<PlaceItem> locations) {
        this.locations = locations;
    }
}
