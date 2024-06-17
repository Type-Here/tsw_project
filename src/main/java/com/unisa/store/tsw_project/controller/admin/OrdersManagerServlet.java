package com.unisa.store.tsw_project.controller.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unisa.store.tsw_project.model.DAO.OrdersDAO;
import com.unisa.store.tsw_project.model.beans.OrderAdminView;
import com.unisa.store.tsw_project.model.beans.OrdersBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.LocalDateAdapter;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "OrderManagerAdmin", urlPatterns = "/WEB-INF/admin/order-manager")
public class OrdersManagerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> header = Optional.ofNullable(req.getHeader("X-Requested-With"));
        if (header.isEmpty() || !header.get().equalsIgnoreCase("XMLHttpRequest")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid X-Requested-With");
            return;
        }
        try {
            Optional<String> ask = Optional.ofNullable(req.getParameter("ask"));
            if (ask.isEmpty()) {
                throw new InvalidParameterException("Ask Option required");
            }

            switch (ask.get()) {
                case "retrieve" -> doSendOrders(req, resp); //Retrieve By Status
                case "retrieveId" -> doSendOrderByID(req, resp); //Retrieve By Id
                default -> throw new InvalidParameterException("Ask Option not valid");
            }
        } catch (InvalidParameterException e) {
            resp.setStatus(Data.SC_INVALID_DATA);
            resp.getWriter().print(Data.SC_INVALID_DATA + ": invalid data; " + e.getMessage());
        }
    }

    /* ============== PRIVATE METHODS ========================= */

    /**
     * AJAX: Send JSON <br />
     * Send Orders JOIN Shipping Addresses Filtered by Order Status. <br />
     * Each page has 10 elements
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if response write fails
     */
    private void doSendOrders(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<String> type = Optional.ofNullable(req.getParameter("type"));
        Optional<String> page = Optional.ofNullable(req.getParameter("page"));
        int pageOffset = 0;

        if(page.isPresent()){
            DataValidator validator = new DataValidator();
            validator.validatePattern(page.get(), DataValidator.PatternType.Int, 0, null);
            pageOffset = Integer.parseInt(page.get()) * 10;
        }

        if(type.isEmpty()) { throw new InvalidParameterException("Type is required");}

        try {
            Data.OrderStatus status = Data.OrderStatus.getEnum(type.get());
            if(status == null) throw new InvalidParameterException("Status Invalid");

            OrdersDAO ordersDAO = new OrdersDAO();
            List<OrderAdminView> orders = ordersDAO.doRetrieveAllOrdersAndAddressesByStatus(status, 10, pageOffset); //NB DIFFERENT BEAN NEEDED FOR JOIN

            String info = "{\"status\":\"continue\",";
            if(orders.isEmpty() || orders.size() < 10) {
                info = "{\"status\":\"last\",";
            }

            // Create a GsonBuilder instance
            GsonBuilder builder = new GsonBuilder();
            // Register the LocalDateAdapter with the builder
            builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
            // Create a Gson instance from the builder
            Gson gson = builder.serializeNulls().create();

            String json = gson.toJson(orders);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print(info + "\"data\":" + json + "}");


        } catch (InvalidParameterException | IllegalArgumentException e){
            resp.setContentType("text/plain");
            resp.getWriter().print(Data.SC_INVALID_DATA + ": invalid data; " + e.getMessage());
            resp.setStatus(Data.SC_INVALID_DATA);
        } catch (SQLException e){
            resp.setContentType("text/plain");
            resp.getWriter().print(HttpServletResponse.SC_INTERNAL_SERVER_ERROR + ": something went wrong");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            resp.getWriter().flush();
        }
    }

    /**
     * AJAX: Send JSON <br />
     * Send Orders JOIN Shipping Addresses Filtered by ORDER ID. <br />
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if response write fails
     */
    private void doSendOrderByID(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<String> id = Optional.ofNullable(req.getParameter("id"));
        if(id.isEmpty()) { throw new InvalidParameterException("ID is required");}

        try {
            DataValidator validator = new DataValidator();
            validator.validatePattern(id.get(), DataValidator.PatternType.Int, 1, null);

            OrdersDAO ordersDAO = new OrdersDAO();
            List<OrderAdminView> order = ordersDAO.doRetrieveByID(Integer.parseInt(id.get()));

            // Create a GsonBuilder instance
            GsonBuilder builder = new GsonBuilder();
            // Register the LocalDateAdapter with the builder
            builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
            // Create a Gson instance from the builder
            Gson gson = builder.serializeNulls().create();

            String json = gson.toJson(order);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().print("{\"data\":" + json + "}");

        } catch (InvalidParameterException e){
            resp.setContentType("text/html");
            resp.getWriter().print("Invalid data - " + e.getMessage());
            resp.setStatus(Data.SC_INVALID_DATA);
        } catch (SQLException e) {
            resp.setContentType("text/plain");
            resp.getWriter().print(HttpServletResponse.SC_INTERNAL_SERVER_ERROR + ": something went wrong");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            //System.err.println("Error: " + e.getMessage());
        } finally {
            resp.getWriter().flush();
        }
    }



}
