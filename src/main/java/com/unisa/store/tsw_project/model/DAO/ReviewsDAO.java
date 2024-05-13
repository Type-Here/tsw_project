package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.ReviewsBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewsDAO {

    private List<ReviewsBean> getReviewsBeanListSelectAll(int id_prod, PreparedStatement ps) throws SQLException {
        ps.setInt(1, id_prod);
        ResultSet rs = ps.executeQuery();
        List<ReviewsBean> reviews = new ArrayList<>();
        while (rs.next()) {
            ReviewsBean r = new ReviewsBean();
            r.setId_review(rs.getInt(1));
            r.setVoto(rs.getInt(2));
            r.setCommento(rs.getString(3));
            r.setReview_date(rs.getDate(4).toLocalDate());
            r.setId_prod(rs.getInt(5));
            r.setId_client(rs.getInt(6));
            reviews.add(r);
        }
        return reviews; //Anche se non ci sono recensioni, restituisco una lista vuota
    }

    public List<ReviewsBean> doRetrieveByProductId(int id_prod) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reviews WHERE id_prod = ?");
            return getReviewsBeanListSelectAll(id_prod, ps);
        }
    }

    public List<ReviewsBean> doRetrieveByClientId(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM reviews WHERE id_client = ?");
            return getReviewsBeanListSelectAll(id_client, ps);
        }
    }

    public void doSave(ReviewsBean review) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO reviews (voto, commento, review_date, id_prod, id_client) VALUES(?, ?, ?, ?, ?)");
            ps.setInt(1, review.getVoto());
            ps.setString(2, review.getCommento());
            ps.setDate(3, java.sql.Date.valueOf(review.getReview_date()));
            ps.setInt(4, review.getId_prod());
            ps.setInt(5, review.getId_client());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            /* Non necessario in quanto una volta salvato non modifichero più il bean
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            review.setId_prod(rs.getInt(1)); //Set the new ID in the ProductBean
            */
        }
    }
}
