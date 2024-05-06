package com.unisa.store.tsw_project.model.beans;

import java.util.List;

public class MetaData {
    private String name;
    private String description;
    private List<String> images;
    private boolean available;


    public MetaData() {
    }

    public MetaData(String name, String description, List<String> images, boolean available) {
        this.name = name;
        this.description = description;
        this.images = images;
        this.available = available;
    }


    /* - GETTERS - */

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImages() {
        return images;
    }

    public boolean isAvailable() {
        return available;
    }


    /* - SETTERS - */

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
