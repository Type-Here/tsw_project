package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.CartBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAO {

    public CartBean doRetriveByUserId(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM cart WHERE id_client = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartBean c = new CartBean();
                c.setId_cart(rs.getInt(1));
                c.setTotal(rs.getDouble(2));
                c.setId_client(rs.getInt(3));
                c.setDiscount_code(rs.getString(4));
                return c;
            }
            return null;
        }
    }

    public void doSave(CartBean cart) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO cart " +
                    "(total, id_client, discount_code) VALUES(?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setDouble(1, cart.getTotal());
            ps.setInt(2, cart.getId_client());
            ps.setString(3, cart.getDiscount_code());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            cart.setId_cart(rs.getInt(1));
        }
    }

    public void doUpdate(CartBean cart) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE cart " +
                    "SET total=?, id_client=?, discount_code=? WHERE id_cart=?");
            ps.setDouble(1, cart.getTotal());
            ps.setInt(2, cart.getId_client());
            ps.setString(3, cart.getDiscount_code());
            ps.setInt(4, cart.getId_cart());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }
}
