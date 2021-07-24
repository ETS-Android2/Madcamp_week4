package com.example.travel.items;

public class Placeinfo {
    private String _id, email, region, name;

    public Placeinfo(String _id, String email, String region, String name) {
        this._id = _id;
        this.email = email;
        this.region = region;
        this.name = name;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getplaceName() {
        return name;
    }

    public void setplaecName(String name) {
        this.name = name;
    }
}
