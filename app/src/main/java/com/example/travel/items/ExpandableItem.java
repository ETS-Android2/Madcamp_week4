package com.example.travel.items;

import java.util.ArrayList;

public class ExpandableItem {
    // common
    public int type;

    // only headers
    public String region;
    public String title;
    public ArrayList<ExpandableItem> invisibleChildren;

    // only child
    public String place;


    // headers
    public ExpandableItem(int type, String region, String title){
        this.type = type;
        this.region = region;
        this.title = title;
    }

    // child
    public ExpandableItem(int type, String place){
        this.type = type;
        this.place = place;
    }

//    public String getRegion(){ return region; }
//    public String getTitle(){ return title; }
//    public String getPlace(){ return place; }
}
