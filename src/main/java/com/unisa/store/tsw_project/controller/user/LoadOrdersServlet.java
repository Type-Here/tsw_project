package com.unisa.store.tsw_project.controller.user;

import com.google.gson.Gson;
import com.unisa.store.tsw_project.model.DAO.OrdersDAO;
import com.unisa.store.tsw_project.model.beans.OrdersBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "LoadOrders", urlPatterns = "/loadOrders")
public class LoadOrdersServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Verify User
        if (request.getSession().getAttribute("userlogged") == null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }

        OrdersDAO ordersDAO = new OrdersDAO();
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");
        OrdersBean orders = null;

        if (request.getParameter("order-id").equals("-1")){
            try {
                orders = ordersDAO.doRetrieveOrderByCartId(userBean.getId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                orders = ordersDAO.doRetrieveOrderByCartId(Integer.parseInt(request.getParameter("order-id")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        Gson gson = new Gson();
        String json = gson.toJson(orders);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

    }
}
