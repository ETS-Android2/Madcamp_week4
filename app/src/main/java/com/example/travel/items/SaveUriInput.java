package com.example.travel.items;

import android.net.Uri;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class SaveUriInput {
    @Expose
    private String email;
    private String region;
    private String title;
    private String address;
    private ArrayList<Uri> uris;

    public SaveUriInput(String email, String region, String title, String address, ArrayList<Uri> uris){
        this.email = email;
        this.region = region;
        this.title = title;
        this.address = address;
        this.uris = uris;
    }
}