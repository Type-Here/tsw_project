package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ReviewsDAO;
import com.unisa.store.tsw_project.model.beans.ReviewsBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet(name = "RegisterReviewServlet", value = "/registerReviewServlet")
public class RegisterReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id_prod = request.getParameter("id_prod");
        String evaluation = request.getParameter("evaluation");
        String review = request.getParameter("comment");

        // Review
        ReviewsDAO reviewsDAO = new ReviewsDAO();
        ReviewsBean reviewsBean = new ReviewsBean();
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");

        reviewsBean.setVote(Integer.parseInt(evaluation));
        reviewsBean.setComment(review);
        reviewsBean.setReview_date(LocalDate.now());
        reviewsBean.setId_prod(Integer.parseInt(id_prod));
        reviewsBean.setId_client(userBean.getId());
        reviewsBean.setFirstname(userBean.getFirstname());

        try {
            reviewsDAO.doSave(reviewsBean);
            response.setStatus(HttpServletResponse.SC_OK);// Write success message set status code to 200 (OK)
            response.getWriter().write("Recensione inserita con successo");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Set status code to 500 (Internal Server Error)
            response.getWriter().write("Errore durante l'inserimento della recensione");
            throw new RuntimeException(e);
        }


    }
}
