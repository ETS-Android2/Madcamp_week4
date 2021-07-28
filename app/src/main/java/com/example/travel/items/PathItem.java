package com.example.travel.items;

public class PathItem {
    private String pathtitle, pathregion, pathcount, image;

    public PathItem() {
        this.pathtitle = "";
        this.pathregion = "";
        this.pathcount="";
        this.image="";
    }

    public PathItem(String pathtitle, String pathregion, String pathcount, String image) {
        this.pathtitle = pathtitle;
        this.pathregion = pathregion;
        this.pathcount = pathcount;
        this.image = image;
    }

    public String getPathtitle() {
        return pathtitle;
    }

    public void setPathtitle(String pathtitle) {
        this.pathtitle = pathtitle;
    }

    public String getPathregion() {
        return pathregion;
    }

    public void setPathregion(String pathregion) {
        this.pathregion = pathregion;
    }

    public String getPathcount() {
        return pathcount;
    }

    public void setPathcount(String pathcount) {
        this.pathcount = pathcount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String pathcount) {
        this.image = image;
    }
}
