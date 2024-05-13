package com.unisa.store.tsw_project.model.beans;

import java.time.LocalDate;

public class ReviewsBean {
    private int id_review;
    private int voto;
    private String commento;
    private LocalDate review_date;
    private int id_prod;
    private int id_client;

    public ReviewsBean() {
    }

    public ReviewsBean(int id_review, int voto, String commento, LocalDate review_date, int id_prod, int id_client) {
        this.id_review = id_review;
        this.voto = voto;
        this.commento = commento;
        this.review_date = review_date;
        this.id_prod = id_prod;
        this.id_client = id_client;
    }

    /* - GETTERS - */

    public int getId_review() {
        return id_review;
    }

    public int getVoto() {
        return voto;
    }

    public String getCommento() {
        return commento;
    }

    public LocalDate getReview_date() {
        return review_date;
    }

    public int getId_prod() {
        return id_prod;
    }

    public int getId_client() {
        return id_client;
    }

    /* - SETTERS - */

    public void setId_review(int id_review) {
        this.id_review = id_review;
    }

    public void setVoto(int voto) {
        this.voto = voto;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }

    public void setReview_date(LocalDate review_date) {
        this.review_date = review_date;
    }

    public void setId_prod(int id_prod) {
        this.id_prod = id_prod;
    }

    public void setId_client(int id_client) {
        this.id_client = id_client;
    }
}
