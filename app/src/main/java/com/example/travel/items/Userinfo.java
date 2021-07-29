package com.example.travel.items;

import java.util.ArrayList;

public class Userinfo {
    private String name;
    private String email;
    private String image;
    private ArrayList<String> calendar, friends;
    private Boolean login;

    public Userinfo(String name, String email, String image, ArrayList<String> calendar, ArrayList<String> friends, Boolean login) {
        this.name = name;
        this.email = email;
        this.image = image;
        this.calendar = calendar;
        this.login = login;
        this.friends = friends;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getCalendar() {
        return calendar;
    }
    public void setCalendar(ArrayList<String> calendar) {
        this.calendar = calendar;
    }

    public ArrayList<String> getFriends() {
        return friends;
    }
    public void setFriends(ArrayList<String> friends) {
        this.friends = friends;
    }

    public Boolean getLogin() {
        return login;
    }
    public void setLogin(Boolean login) {
        this.login = login;
    }
}
