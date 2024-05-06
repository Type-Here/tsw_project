package com.unisa.store.tsw_project.model;

import com.unisa.store.tsw_project.other.Data;

public class ProductBean {
    private int id_prod;
    private String name;
    private double price;
    private boolean type;
    private String platform;
    private String metadata;
    private String key;
    private Data.Condition condition;
    private double discount;

    public ProductBean() {
    }

    public ProductBean(int id_prod, String name, double price, boolean type,
                       String platform, String metadata, String key,
                       Data.Condition condition, double discount) {
        this.id_prod = id_prod;
        this.name = name;
        this.price = price;
        this.type = type;
        this.platform = platform;
        this.metadata = metadata;
        this.key = key;
        this.condition = condition;
        this.discount = discount;
    }

    public int getId_prod() {
        return id_prod;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public boolean isType() {
        return type;
    }

    public String getPlatform() {
        return platform;
    }

    public String getMetadata() {
        return metadata;
    }

    public String getKey() {
        return key;
    }

    public Data.Condition getCondition() {
        return condition;
    }

    public double getDiscount() {
        return discount;
    }

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCondition(Data.Condition condition) {
        this.condition = condition;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
