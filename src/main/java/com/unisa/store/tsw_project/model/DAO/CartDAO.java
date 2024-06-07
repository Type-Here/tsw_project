package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.CartBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDAO {
    /**
     * Retrieve a Cart by its ID
     * @param id_cart CartID to retrieve
     * @return a CartBean or Null if not found
     */
    public CartBean doRetrieveById(int id_cart){
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM cart WHERE id_cart = ?");
            ps.setInt(1, id_cart);
            ResultSet rs = ps.executeQuery();
            return populateBean(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CartBean doRetrieveByUserId(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM cart WHERE id_client = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            return populateBean(rs);
        }
    }

    /**
     * Save a Cart in Database. <br />
     * If save id successful the new id_cart is set inside the Bean.
     * @param cart to be saved
     * @throws SQLException if save or query of new key fails
     */
    public void doSave(CartBean cart) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO cart " +
                    "(total, id_client, discount_code, active) VALUES(?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            ps.setBigDecimal(1, cart.getTotal());
            ps.setInt(2, cart.getId_client());
            ps.setString(3, cart.getDiscount_code());
            ps.setObject(4, cart.getActive());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            cart.setId_cart(rs.getInt(1));
        }
    }

    /**
     * Update Cart Bean by CartID
     * @param cart with the data to be updated. It needs to have a valid cartID
     * @throws SQLException if update fails
     */
    public void doUpdate(CartBean cart) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE cart " +
                    "SET total=?, id_client=?, discount_code=?, active=? WHERE id_cart=?");
            ps.setBigDecimal(1, cart.getTotal());
            ps.setInt(2, cart.getId_client());
            ps.setString(3, cart.getDiscount_code());
            ps.setObject(4, cart.getActive());
            ps.setInt(5, cart.getId_cart());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }

    /**
     * Retrieve Active Cart for a User by his ID. <br />
     * Active Cart is a Cart that is saved but not ordered yet by the user.
     * @param id_client User ID to retrieve data from
     * @return CartBean if retrieved, else Null
     * @throws SQLException if query fails
     */
    public CartBean doRetrieveActive(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(" SELECT * FROM cart WHERE id_client=? AND active = true");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            return populateBean(rs);
        }
    }

    /* ----------------------------- PRIVATE METHODS ------------------------------- */

    /**
     * Set a CartBean with a ResultSet Data
     * @param rs ResultSet
     * @return a CartBean or Null if rs is empty
     * @throws SQLException if getter/setter from RS fails
     */
    private CartBean populateBean(ResultSet rs) throws SQLException {
        if (rs.next()) {
            CartBean c = new CartBean();
            c.setId_cart(rs.getInt(1));
            c.setTotal(rs.getBigDecimal(2));
            c.setId_client(rs.getInt(3));
            c.setDiscount_code(rs.getString(4));
            c.setActive((Boolean) rs.getObject(5));
            return c;
        }
        return null;
    }



}
