package com.unisa.store.tsw_project.model.beans;

public class ShippingAddressesBean {
    private int id_client;
    private int id_add;
    private String firstname;
    private String lastname;
    private String address;
    private String city;
    private String prov;
    private String CAP;

    public ShippingAddressesBean() {
    }

    public ShippingAddressesBean(int id_client, int id_add, String firstname, String lastname, String address, String city, String prov, String CAP) {
        this.id_client = id_client;
        this.id_add = id_add;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.city = city;
        this.prov = prov;
        this.CAP = CAP;
    }



    /* - GETTERS - */

    public int getId_client() {
        return id_client;
    }

    public int getId_add() {
        return id_add;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProv() {
        return prov;
    }

    public String getCAP() {
        return CAP;
    }

    /* - SETTERS - */

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }

    public void setId_add(int id_add) {
        this.id_add = id_add;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setCAP(String CAP) {
        this.CAP = CAP;
    }
}
