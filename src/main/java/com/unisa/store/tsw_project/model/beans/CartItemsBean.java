package com.unisa.store.tsw_project.model.beans;

public class CartItemsBean {
    private int id_prod;
    private int id_cart;
    private int quantity;
    private int real_price;

    public CartItemsBean() {
    }

    public CartItemsBean(int id_prod, int id_cart, int quantity, int real_price) {
        this.id_prod = id_prod;
        this.id_cart = id_cart;
        this.quantity = quantity;
        this.real_price = real_price;
    }



    /* - GETTERS - */

    public int getId_prod() {
        return id_prod;
    }

    public int getId_cart() {
        return id_cart;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getReal_price() {
        return real_price;
    }

    /* - SETTERS - */

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setReal_price(int real_price) {
        this.real_price = real_price;
    }
}
