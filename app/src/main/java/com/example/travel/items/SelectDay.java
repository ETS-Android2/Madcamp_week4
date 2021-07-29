package com.example.travel.items;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class SelectDay {

    @Expose
    private ArrayList<String> days;
    private String place;

    public SelectDay(ArrayList<String> days , String place){
        this.days = days;
        this.place =place;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public String getTitle() {
        return place;
    }
}
