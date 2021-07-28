package com.example.travel.items;

import com.example.travel.UserLocation;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class SavePathInput {

    @Expose
    private ArrayList<String> participants;
    private String title;
    private String region;
    private String totalSize;
    private ArrayList<UserLocation> locations;
    public SavePathInput(ArrayList<String> participants , String title , String  region ,String totalSize ,ArrayList<UserLocation> locations){
        this.participants = participants;
        this.title = title;
        this.region = region;
        this.totalSize = totalSize;
        this.locations = locations;
    }
}
