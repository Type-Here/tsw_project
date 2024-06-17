package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.*;
import com.unisa.store.tsw_project.model.beans.*;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoadOrderInformation", urlPatterns = "/loadOrderInformation")
public class LoadOrderInformationServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //Verify User
        if (request.getSession().getAttribute("userlogged") == null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }

        String orderId = request.getParameter("orderId");
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");

        CartDAO cartDAO = new CartDAO();
        CartBean cart = new CartBean();

        CartItemsDAO cartItemsDAO = new CartItemsDAO();
        List<CartItemsBean> cartItems = new ArrayList<>();

        ProductDAO productDAO = new ProductDAO();
        List<ProductBean> products = new ArrayList<>();

        ReviewsDAO reviewsDAO = new ReviewsDAO();
        List<ReviewsBean> reviewClient = new ArrayList<>();

        // Retrieve all the reviews of the client, the cart, the cart items and the products
        try {
            reviewClient = reviewsDAO.doRetrieveByClientId(userBean.getId());
            cart = cartDAO.doRetrieveById(Integer.parseInt(orderId));
            cartItems = cartItemsDAO.doRetrieveAllByCartId(Integer.parseInt(orderId));
            for (CartItemsBean cartItem : cartItems) {
                products.add(productDAO.doRetrieveById(cartItem.getId_prod()));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<ReviewsBean> reviews = new ArrayList<>();

        // Retrieve all the reviews of the products in the cart
        for (ProductBean product : products) {
            for (ReviewsBean reviewsBean : reviewClient) {
                if (reviewsBean.getId_prod() == product.getId_prod()) {
                    reviews.add(reviewsBean);
                }
            }
        }

        //Adding path to product images
        JSONMetaParser parser = new JSONMetaParser();
        parser.doParseMetaData(products, getServletContext());

        // Set attributes and forward to the order information page
        request.setAttribute("reviews", reviews);
        request.setAttribute("cart", cart);
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/results/orderInformation.jsp").forward(request, response);

    }
}
