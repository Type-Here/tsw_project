package com.unisa.store.tsw_project.model.beans;
import java.util.ArrayList;
import java.util.List;

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

    private List<ConditionBean> conditions;
    private Double discount;
    private List<CategoryBean> categoryBeanList;

    private MetaData metaData;

    public ProductBean() {
    }

    public ProductBean(int id_prod, String name, double price, boolean type,
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

    public void setConditions(List<ConditionBean> conditions) {
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
}
