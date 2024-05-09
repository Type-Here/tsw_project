package com.unisa.store.tsw_project.model.beans;

import java.util.List;

public class MetaData {
    private String path;
    private String front;
    private List<String> gallery;


    public MetaData() {
    }

    public MetaData(String path, String front, List<String> gallery) {
        this.path = path;
        this.front = front;
        this.gallery = gallery;
    }


    /* - GETTERS - */

    public String getPath() {
        return path;
    }

    public String getFront() {
        return front;
    }

    public List<String> getGallery() {
        return gallery;
    }

    /* - SETTERS - */

    public void setPath(String path) {
        this.path = path;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public void setGallery(List<String> gallery) {
        this.gallery = gallery;
    }
}
