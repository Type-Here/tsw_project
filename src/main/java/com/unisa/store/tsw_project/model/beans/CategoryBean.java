package com.unisa.store.tsw_project.model.beans;

public class CategoryBean {
    private int id_cat;
    private String typename;

    public CategoryBean(){
    }

    public CategoryBean(int id_cat, String typename){
        this.id_cat = id_cat;
        this.typename = typename;
    }

    /* - GETTERS - */

    public int getId_cat() {
        return id_cat;
    }

    public String getTypename() {
        return typename;
    }

    /* - SETTERS - */

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
