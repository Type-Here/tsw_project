package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.OrdersBean;
import com.unisa.store.tsw_project.other.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdersDAO {

    public List<OrdersBean> doRetrieveAllOrdersByUserId(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM orders WHERE id_client = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            List<OrdersBean> orders = new ArrayList<>();
            while (rs.next()) {
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

}
