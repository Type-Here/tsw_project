package com.unisa.store.tsw_project.model.beans;

public class ConditionBean {
    private int id_prod;
    private int id_cond;
    private int quantity;


    /* GETTERS */

    public int getId_prod() {
        return id_prod;
    }

    public int getId_cond() {
        return id_cond;
    }

    public int getQuantity() {
        return quantity;
    }

    /* SETTERS */

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public void setId_cond(int id_cond) {
        this.id_cond = id_cond;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
