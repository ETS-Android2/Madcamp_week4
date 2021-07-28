package com.example.travel.items;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class Dateinfo {

    @Expose
    private String email;
    private ArrayList<String> days;

    public Dateinfo(String email,ArrayList<String> days){
        this.email = email;
        this.days = days;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getDays() {
        return days;
    }
}
