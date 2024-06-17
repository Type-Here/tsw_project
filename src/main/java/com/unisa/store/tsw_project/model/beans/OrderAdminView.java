package com.unisa.store.tsw_project.model.beans;

import com.unisa.store.tsw_project.other.Data;

import java.time.LocalDate;

public class OrderAdminView {
    private int id_cart;
    private int id_client;
    private String firstname;
    private String lastname;
    private String shippingAddress;
    private Data.OrderStatus status;
    private LocalDate orderDate;

    /* GETTERS */

    public int getId_cart() {
        return id_cart;
    }

    public int getId_client() {
        return id_client;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public Data.OrderStatus getStatus() {
        return status;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    /* SETTERS */

    public void setId_cart(int id_cart) {
        this.id_cart = id_cart;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void setStatus(Data.OrderStatus status) {
        this.status = status;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
}
