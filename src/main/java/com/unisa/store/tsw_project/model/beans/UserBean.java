package com.unisa.store.tsw_project.model.beans;

import java.time.LocalDate;

public class UserBean {
    private int id;
    private String firstname;
    private String lastname;
    private String telephone;
    private String email;
    private LocalDate birth;
    private String address;
    private String city;
    private String prov;
    private String CAP;
    private int id_cred;


    public UserBean() {
    }

    public UserBean(int id, String firstname, String lastname, String telephone, String email, LocalDate birth, String address, String city, String prov, String CAP, int id_cred) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.email = email;
        this.birth = birth;
        this.address = address;
        this.city = city;
        this.prov = prov;
        this.CAP = CAP;
        this.id_cred = id_cred;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirth() {
        return birth;
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

    public int getId_cred() {
        return id_cred;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
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

    public void setId_cred(int id_cred) {
        this.id_cred = id_cred;
    }
}
