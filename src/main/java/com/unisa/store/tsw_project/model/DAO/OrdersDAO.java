package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.OrderAdminView;
import com.unisa.store.tsw_project.model.beans.OrdersBean;
import com.unisa.store.tsw_project.other.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrdersDAO {

    public List<OrdersBean> doRetrieveAllOrdersByUserId(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE id_client = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            List<OrdersBean> orders = new ArrayList<>();
            while (rs.next()) {
                OrdersBean o = addOrderBean(rs);
                orders.add(o);
            }
            return orders;
        }
    }

    public OrdersBean doRetrieveOrderByCartId(int id_cart) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE id_cart = ?");
            ps.setInt(1, id_cart);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return addOrderBean(rs);
            }
            return null;
        }
    }

    public void doSave(OrdersBean order) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO orders " +
                    "(id_cart, id_client, id_add, status, order_date) VALUES(?, ?, ?, ?, ?)");
            ps.setInt(1, order.getId_cart());
            ps.setInt(2, order.getId_client());
            ps.setInt(3, order.getId_add());
            ps.setString(4, order.getStatus().value); // Enum to String da vedere -- Checked: use value to get db enum parameter
            ps.setDate(5, java.sql.Date.valueOf(order.getOrder_date())); // LocalDate to Date da verificare funzionamento
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        }
    }

    public void doUpdate(OrdersBean order) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE orders " +
                    "SET id_cart=?, id_client=?, id_add=?, status=?, order_date=? WHERE id_cart=?");
            ps.setInt(1, order.getId_cart());
            ps.setInt(2, order.getId_client());
            ps.setInt(3, order.getId_add());
            ps.setString(4, order.getStatus().toString()); // Enum to String da vedere
            ps.setDate(5, java.sql.Date.valueOf(order.getOrder_date())); // LocalDate to Date da verificare funzionamento
            ps.setInt(6, order.getId_cart());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }

    /**
     * Retrieve Orders By Status. Retrieve Most recent first
     * @param status Data.OrderStatus enum to retrieve
     * @param limit limit results to this value, default 200
     * @param offset set offset to this value, default 0
     * @return a List with all valid orders
     * @throws SQLException is query fails
     */
    public List<OrdersBean> doRetrieveAllOrdersByStatus(Data.OrderStatus status, Integer limit, Integer offset) throws SQLException {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE status = ? ORDER BY order_date DESC LIMIT ? OFFSET ?");
            ps.setString(1, status.value);
            ps.setInt(2, Objects.requireNonNullElse(limit, 200));
            ps.setInt(3, Objects.requireNonNullElse(offset, 0));

            ResultSet rs = ps.executeQuery();
            List<OrdersBean> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(addOrderBean(rs));
            }
            return orders;
        }
    }



    /* ============================= FOR ORDERADMINVIEW BEAN FOR ADMIN ORDER TABLE =================================== */

    /**
     * NB: OrderAdminView: <br />
     * Bean only for Order Table View in Admin: Needed for Data Join
     * Retrieve Orders JOIN Shipping Address By Status. Retrieve Most recent first
     * @param status Data.OrderStatus enum to retrieve
     * @param limit limit results to this value, default 200
     * @param offset set offset to this value, default 0
     * @return a List with all valid orders
     * @throws SQLException is query fails
     */
    public List<OrderAdminView> doRetrieveAllOrdersAndAddressesByStatus(Data.OrderStatus status, Integer limit, Integer offset) throws SQLException {
        try (Connection con = ConPool.getConnection()){

            PreparedStatement ps = con.prepareStatement("SELECT id_cart, id_client, firstname, lastname, address, status, order_date FROM orders NATURAL JOIN shipping_addresses WHERE status = ? ORDER BY order_date DESC LIMIT ? OFFSET ?");
            ps.setString(1, status.value);
            ps.setInt(2, Objects.requireNonNullElse(limit, 200));
            ps.setInt(3, Objects.requireNonNullElse(offset, 0));

            ResultSet rs = ps.executeQuery();
            List<OrderAdminView> orders = new ArrayList<>();
            while (rs.next()) {
                orders.add(addOrderAdminView(rs));
            }
            return orders;
        }
    }

    /**
     * NB: OrderAdminView: <br />
     * Bean only for Order Table View in Admin: Needed for Data Join
     * Retrieve Orders JOIN Shipping Address By ID.
     * @param id Order ID to retrieve
     * @return List of OrderAdminView : should be only 1 item, but it permits JS Code reuse
     * @throws SQLException if query fails
     */
    public List<OrderAdminView> doRetrieveByID(int id) throws SQLException {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT id_cart, id_client, firstname, lastname, address, status, order_date FROM orders NATURAL JOIN shipping_addresses WHERE id_cart = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<OrderAdminView> order = new ArrayList<>();
            if (rs.next()) {
                order.add(addOrderAdminView(rs));
            }
            return order;
        }
    }




    /* ========================================= PRIVATE METHODS ================================================== */

    /**
     * Populate and OrderBean with a ResultSet row data
     * @param rs ResultSet
     * @return Populated OrderBean
     * @throws SQLException if retrieving data fails
     */
    private OrdersBean addOrderBean(ResultSet rs) throws SQLException {
        OrdersBean o = new OrdersBean();
        o.setId_cart(rs.getInt(1));
        o.setId_client(rs.getInt(2));
        o.setId_add(rs.getInt(3));
        String status = rs.getString(4);

        // Manage Optional Value to Avoid Illegal Argument Exception when null
        if (status != null) {
            o.setStatus(Data.OrderStatus.getEnum(status));
        } else {
            o.setStatus(null);
        }
        o.setOrder_date(rs.getDate(5).toLocalDate());

        return o;
    }

    /**
     * FOR ADMIN VIEW <br />
     * Populate and OrderAdminView with a ResultSet row data
     * @param rs ResultSet
     * @return Populated OrderBean
     * @throws SQLException if retrieving data fails
     */
    private OrderAdminView addOrderAdminView(ResultSet rs) throws SQLException {
        OrderAdminView o = new OrderAdminView();
        o.setId_cart(rs.getInt(1));
        o.setId_client(rs.getInt(2));
        o.setFirstname(rs.getString(3));
        o.setLastname(rs.getString(4));
        o.setShippingAddress(rs.getString(5));
        String status = rs.getString(6);
        if(status != null) o.setStatus(Data.OrderStatus.getEnum(status));
        else o.setStatus(null);
        o.setOrderDate(rs.getDate(7).toLocalDate());

        return o;
    }


}
