package com.example.travel.items;

import java.util.ArrayList;
import java.util.List;

public class Pathinfo {
    private String _id, title, region, image;
    private Integer totalSize;
    private ArrayList<Placeinfo> locations;
    private ArrayList<String> date, participants;

    public Pathinfo(String _id, ArrayList<String>  participants, String title, String region, Integer totalSize, ArrayList<Placeinfo> locations, String image, ArrayList<String> date) {
        this._id = _id;
        this.participants = participants;
        this.title = title;
        this.region = region;
        this.totalSize = totalSize;
        this.locations = locations;
        this.image = image;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ArrayList<String> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<String> participants) {
        this.participants = participants;
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

    public ArrayList<Placeinfo> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Placeinfo> locations) {
        this.locations = locations;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getDate() {
        return date;
    }

    public void setDate(ArrayList<String> date) {
        this.date = date;
    }
}
