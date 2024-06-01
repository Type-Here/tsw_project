package com.unisa.store.tsw_project.controller.user;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unisa.store.tsw_project.model.DAO.OrdersDAO;
import com.unisa.store.tsw_project.model.DAO.ShippingAddressesDAO;
import com.unisa.store.tsw_project.model.beans.OrdersBean;
import com.unisa.store.tsw_project.model.beans.ShippingAddressesBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.LocalDateAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LoadOrders", urlPatterns = "/loadOrders")
public class LoadOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Verify User
        if (request.getSession().getAttribute("userlogged") == null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }

        String orderId = request.getParameter("orderId");
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");
        List<OrdersBean> orders = new ArrayList<>();
        List<ShippingAddressesBean> orderAddresses = new ArrayList<>();
        OrdersDAO ordersDAO = new OrdersDAO();
        ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();

        // Fetch all orders
        try {
            orders = ordersDAO.doRetrieveAllOrdersByUserId(userBean.getId());
            for (OrdersBean order : orders) {
                orderAddresses.addAll(shippingAddressesDAO.doRetrieveAddressByOrderID(order.getId_add()));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Handle LocalDate Gson serialization
        // Create a GsonBuilder instance
        GsonBuilder builder = new GsonBuilder();

        // Register the LocalDateAdapter with the builder
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());

        // Create a Gson instance from the builder
        Gson gson = builder.create();

        // Convert the orders to JSON
        String ordersJson = gson.toJson(orders);
        String addressesJson = new Gson().toJson(orderAddresses);

        // Combine the JSONs in one
        String combinedJson = String.format("{\"orders\": %s, \"addresses\": %s}", ordersJson, addressesJson);

        // Set the response type to JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the JSON to the response
        response.getWriter().write(combinedJson);
    }
}
