package com.unisa.store.tsw_project.model.beans;

public class CartBean {
    private int id_cart;
    private double total;
    private int id_client;
    private String discount_code;

    public CartBean() {
    }

    public CartBean(int id_cart, double total, int id_client, String discount_code) {
        this.id_cart = id_cart;
        this.total = total;
        this.id_client = id_client;
        this.discount_code = discount_code;
    }

    /* - GETTERS - */

    public int getId_cart() {
        return id_cart;
    }

    public double getTotal() {
        return total;
    }

    public int getId_client() {
        return id_client;
    }

    public String getDiscount_code() {
        return discount_code;
    }

    /* - SETTERS - */

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public void setDiscount_code(String discount_code) {
        this.discount_code = discount_code;
    }
}
