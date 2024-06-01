package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.CartItemsDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "RefundServlet", urlPatterns = "/refund")
public class RefundServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int cartItemID = Integer.parseInt(request.getParameter("cartItemID"));
        int cartID = Integer.parseInt(request.getParameter("cartID"));

        CartItemsDAO cartItemsDAO = new CartItemsDAO();



        try {
            cartItemsDAO.doRefundByCartItemID(cartItemID, cartID);
            response.setStatus(HttpServletResponse.SC_OK);// Write success message set status code to 200 (OK)
            response.getWriter().write("Rimborso inserita con successo");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Set status code to 500 (Internal Server Error)
            response.getWriter().write("Errore durante il processo del rimborso");
            throw new RuntimeException(e);
        }
    }
}
