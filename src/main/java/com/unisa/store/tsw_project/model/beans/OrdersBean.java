package com.unisa.store.tsw_project.model.beans;

import java.time.LocalDate;

public class OrdersBean {
    private int id_cart;
    private int id_client;
    private int id_add;
    private enum status {inProcess, shipped, delivering, delivered, refunded, canceled};
    private LocalDate order_date;

    public OrdersBean() {
    }

    // Da vedere enum status nel getter e setter e costruttore
    public OrdersBean(int id_cart, int id_client, int id_add, LocalDate order_date) {
        this.id_cart = id_cart;
        this.id_client = id_client;
        this.id_add = id_add;
        this.order_date = order_date;
    }



    /* - GETTERS - */

    public int getId_cart() {
        return id_cart;
    }

    public int getId_client() {
        return id_client;
    }

    public int getId_add() {
        return id_add;
    }

    public LocalDate getOrder_date() {
        return order_date;
    }

    /* - SETTERS - */

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public void setId_add(int id_add) {
        this.id_add = id_add;
    }

    public void setOrder_date(LocalDate order_date) {
        this.order_date = order_date;
    }
}
