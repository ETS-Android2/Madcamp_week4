package com.example.travel.items;

import java.util.ArrayList;

public class Userinfo {
    private String name;
    private String email;
    private String photo;
    private ArrayList<String> calendar;
    private Boolean login;

    public Userinfo(String name, String email, String photo, ArrayList<String> calendar, Boolean login) {
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.calendar = calendar;
        this.login = login;
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

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ArrayList<String> getCalendar() {
        return calendar;
    }
    public void setECalendar(ArrayList<String> calendar) {
        this.calendar = calendar;
    }

    public Boolean getLogin() {
        return login;
    }
    public void setLogin(Boolean login) {
        this.login = login;
    }
}
