package com.example.brewedapp;

import android.net.Uri;

import java.io.Serializable;

public class Product implements Serializable {
    private String p_imageURL;
    private String p_title;
    private String p_origin;
    private String p_roastLevel;
    private String p_notes;
    private String p_acidityLevel;
    private String p_grind;
    private String p_weight;
    private Float p_price;

    public Product() {}

    public Product(String p_imageURL, String p_title, String p_origin, String p_roastLevel, String p_notes, String p_acidityLevel,
                   String p_grind, String p_weight, Float p_price) {
        this.p_imageURL = p_imageURL;
        this.p_title = p_title;
        this.p_origin = p_origin;
        this.p_roastLevel = p_roastLevel;
        this.p_notes = p_notes;
        this.p_acidityLevel = p_acidityLevel;
        this.p_grind = p_grind;
        this.p_weight = p_weight;
        this.p_price = p_price;
    }

    public String getP_imageURL() {
        return p_imageURL;
    }

    public void setP_imageURL(String p_imageURL) {
        this.p_imageURL = p_imageURL;
    }

    public String getP_title() {
        return p_title;
    }

    public void setP_title(String p_title) {
        this.p_title = p_title;
    }

    public String getP_origin() {
        return p_origin;
    }

    public void setP_origin(String p_origin) {
        this.p_origin = p_origin;
    }

    public String getP_roastLevel() {
        return p_roastLevel;
    }

    public void setP_roastLevel(String p_roastLevel) {
        this.p_roastLevel = p_roastLevel;
    }

    public String getP_notes() {
        return p_notes;
    }

    public void setP_notes(String p_notes) {
        this.p_notes = p_notes;
    }

    public String getP_acidityLevel() {
        return p_acidityLevel;
    }

    public void setP_acidityLevel(String p_acidityLevel) {
        this.p_acidityLevel = p_acidityLevel;
    }

    public String getP_grind() {
        return p_grind;
    }

    public void setP_grind(String p_grind) {
        this.p_grind = p_grind;
    }

    public String getP_weight() {
        return p_weight;
    }

    public void setP_weight(String p_weight) {
        this.p_weight = p_weight;
    }

    public Float getP_price() {
        return p_price;
    }

    public void setP_price(Float p_price) {
        this.p_price = p_price;
    }
}
