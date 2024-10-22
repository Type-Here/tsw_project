package com.unisa.store.tsw_project.model.beans;
import com.unisa.store.tsw_project.other.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductBean {
    private int id_prod;
    private String name;
    private BigDecimal price;
    private Boolean type; // False Physical , True Digital
    private String platform;
    private String developer;
    private String description;
    private String metadataPath;
    private String key;

    private List<ConditionBean> conditions;
    private Double discount;
    private List<CategoryBean> categoryBeanList;

    private MetaData metaData;

    public ProductBean() {
    }

    public ProductBean(int id_prod, String name, BigDecimal price, boolean type,
                       String platform, String developer, String description, String metadata,
                       String key, List<ConditionBean> conditions, double discount, List<CategoryBean> categoryBeanList) {
        this.id_prod = id_prod;
        this.name = name;
        this.price = price;
        this.type = type;
        this.platform = platform;
        this.developer = developer;
        this.description = description;
        this.metadataPath = metadata;
        this.key = key;
        this.conditions = conditions;
        this.discount = discount;
        this.categoryBeanList = categoryBeanList;
    }



    /* - GETTERS - */

    public int getId_prod() {
        return id_prod;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
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

    public List<ConditionBean> getConditions() {
        return conditions;
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

    public List<CategoryBean> getCategoryBeanList() {
        return categoryBeanList;
    }

    /* - SETTERS - */

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(BigDecimal price) {
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

    public void setConditions(List<ConditionBean> conditions) {
        if(this.type){ //Digital Type Has only 1 Condition (0 = Digital)
            this.conditions = new ArrayList<>();
            ConditionBean conditionBean = new ConditionBean();
            conditionBean.setCondition(Data.Condition.X);
            conditionBean.setId_cond(0);
            conditionBean.setQuantity(-1);
            conditionBean.setId_prod(this.getId_prod());
            this.conditions.add(conditionBean);
            return;
        }
        this.conditions = conditions;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public void setCategoryBeanList(List<CategoryBean> categoryBeanList) {
        this.categoryBeanList = categoryBeanList;
    }

    /* OTHER SPECIFIC */

    /**
     * Add a Single Condition to the list in ProductBean
     */
    public void addCondition(ConditionBean conditionBean){
        if(this.getConditions() == null){
            this.setConditions(new ArrayList<>());
            if(this.type) return; //If Digital ---> Condition already set in setConditions call above!
        }
        this.getConditions().add(conditionBean);
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
                ", conditions=" + conditions +
                ", discount=" + discount +
                ", metaData=" + metaData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductBean that = (ProductBean) o;
        return getId_prod() == that.getId_prod();
    }

    @Override
    public int hashCode() {
        return this.getId_prod();
    }
}
