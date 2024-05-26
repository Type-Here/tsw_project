package com.unisa.store.tsw_project.model.beans;

import java.time.LocalDate;

public class ReviewsBean {
    private int id_review;
    private int vote;
    private String comment;
    private LocalDate review_date;
    private int id_prod;
    private int id_client;
    private String firstname;

    public ReviewsBean() {
    }

    public ReviewsBean(int id_review, int vote, String comment, LocalDate review_date, int id_prod, int id_client) {
        this.id_review = id_review;
        this.vote = vote;
        this.comment = comment;
        this.review_date = review_date;
        this.id_prod = id_prod;
        this.id_client = id_client;
    }

    /* - GETTERS - */

    public int getId_review() {
        return id_review;
    }

    public int getVote() {
        return vote;
    }

    public String getComment() {
        return comment;
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

    public String getFirstname() {
        return firstname;
    }

    /* - SETTERS - */

    public void setId_review(int id_review) {
        this.id_review = id_review;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
