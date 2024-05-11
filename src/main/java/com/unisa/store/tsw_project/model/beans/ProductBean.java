package com.unisa.store.tsw_project.model.beans;

import com.unisa.store.tsw_project.other.Data;

public class ProductBean {
    private int id_prod;
    private String name;
    private Double price;
    private Boolean type;
    private String platform;
    private String developer;
    private String description;
    private String metadataPath;
    private String key;
    private Data.Condition condition;
    private Double discount;

    private MetaData metaData;

    public ProductBean() {
    }

    public ProductBean(int id_prod, String name, double price, boolean type,
                       String platform, String developer, String description, String metadata,
                       String key, Data.Condition condition, double discount) {
        this.id_prod = id_prod;
        this.name = name;
        this.price = price;
        this.type = type;
        this.platform = platform;
        this.developer = developer;
        this.description = description;
        this.metadataPath = metadata;
        this.key = key;
        this.condition = condition;
        this.discount = discount;
    }



    /* - GETTERS - */

    public int getId_prod() {
        return id_prod;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public boolean isType() {
        return type;
    }

    public String getPlatform() {
        return platform;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getDescription() {
        return description;
    }

    public String getMetadataPath() {
        return metadataPath;
    }

    public String getKey() {
        return key;
    }

    public Data.Condition getCondition() {
        return condition;
    }

    public Double getDiscount() {
        return discount;
    }

    public Boolean getType() {
        return type;
    }

    public MetaData getMetaData() {
        return metaData;
    }



    /* - SETTERS - */

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMetadataPath(String metadata) {
        this.metadataPath = metadata;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setCondition(Data.Condition condition) {
        this.condition = condition;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }


    @Override
    public String toString() {
        return "ProductBean{" +
                "id_prod=" + id_prod +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", type=" + type +
                ", platform='" + platform + '\'' +
                ", developer='" + developer + '\'' +
                ", description='" + description + '\'' +
                ", metadataPath='" + metadataPath + '\'' +
                ", key='" + key + '\'' +
                ", condition=" + condition +
                ", discount=" + discount +
                ", metaData=" + metaData +
                '}';
    }
}
