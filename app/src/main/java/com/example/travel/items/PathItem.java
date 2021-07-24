package com.example.travel.items;

public class PathItem {
    private String pathtitle, pathregion, pathcount;

    public PathItem() {
        this.pathtitle = "";
        this.pathregion = "";
        this.pathcount="";
    }

    public PathItem(String pathtitle, String pathregion, String pathcount) {
        this.pathtitle = pathtitle;
        this.pathregion = pathregion;
        this.pathcount = pathcount;
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
}
