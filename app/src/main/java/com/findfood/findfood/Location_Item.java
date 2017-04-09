package com.findfood.findfood;

import android.widget.Button;

import java.security.Key;

public class Location_Item {
    private int id;
    private String category;
    private String price;
    private String location;
    private String comment;
    private double coorlat;
    private double coorlong;

    public Location_Item() {
    }

    public Location_Item(String category, String price, String location, String comment
    , double coorlat, double coorlong) {
        this.category = category;
        this.price = price;
        this.location = location;
        this.comment = comment;
        this.coorlat = coorlat;
        this.coorlong = coorlong;
    }

    public Location_Item(int id, String category, String price, String location, String comment
    , double coorlat, double coorlong) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.location = location;
        this.comment = comment;
        this.coorlat = coorlat;
        this.coorlong = coorlong;
    }

    public void setId(int id) {this.id = id;}
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(String price) {this.price = price;}
    public void setLocation(String location) {this.location = location; }
    public void setComment(String comment) {this.comment = comment; }
    public void setCoorlat(double coorlat) {this.coorlat = coorlat; }
    public void setCoorlong(double coorlong) {this.coorlong = coorlong; }

    public int getId() {
        return id;
    }
    public String getCategory() {
        return category;
    }
    public String getPrice() {return price;}
    public String getLocation() {
        return location;
    }
    public String getComment() {return comment; }
    public double getCoorlat() {return coorlat; }
    public double getCoorlong() {return coorlong; }
}