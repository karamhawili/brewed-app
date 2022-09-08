package com.example.brewedapp;

import java.io.Serializable;

/*
        A simpler version of the Product class that is used for the SQLite database
*/

public class ProductSqlite implements Serializable {

    private String p_imageURL;
    private String p_title;
    private String p_origin;
    private Float p_price;
    private int p_quantity;

    public ProductSqlite() {}

    public ProductSqlite(String p_imageURL, String p_title, String p_origin, Float p_price, int p_quantity) {
        this.p_imageURL = p_imageURL;
        this.p_title = p_title;
        this.p_origin = p_origin;
        this.p_price = p_price;
        this.p_quantity = p_quantity;
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

    public Float getP_price() {
        return p_price;
    }

    public void setP_price(Float p_price) {
        this.p_price = p_price;
    }

    public int getP_quantity() {
        return p_quantity;
    }

    public void setP_quantity(int p_quantity) {
        this.p_quantity = p_quantity;
    }
}
