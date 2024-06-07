package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.CartBean;
import com.unisa.store.tsw_project.model.beans.CartItemsBean;
import com.unisa.store.tsw_project.other.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartItemsDAO {

    public List<CartItemsBean> doRetrieveAllByCartId(int id_cart) throws SQLException {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM cart_items WHERE id_cart = ?");
            ps.setInt(1, id_cart);
            ResultSet rs = ps.executeQuery();
            List<CartItemsBean> cartItems = new ArrayList<>();
            while (rs.next()) {
                CartItemsBean c = new CartItemsBean();
                c.setId_prod(rs.getInt(1));
                c.setId_cart(rs.getInt(2));
                c.setQuantity(rs.getInt(3));
                c.setReal_price(rs.getBigDecimal(4));
                c.setRefund(rs.getInt(5));
                c.setCondition(Data.Condition.getEnum(rs.getInt(6)));
                cartItems.add(c);
            }
            if (cartItems.isEmpty()){
                return null;
            } else {
                return cartItems;
            }
        }
    }

    public void doSave(CartItemsBean cartItem) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO cart_items " +
                    "(id_prod, id_cart, quantity, real_price, refund, `condition`) VALUES(?, ?, ?, ?, ?, ?)");
            ps.setInt(1, cartItem.getId_prod());
            ps.setInt(2, cartItem.getId_cart());
            ps.setInt(3, cartItem.getQuantity());
            ps.setBigDecimal(4, cartItem.getReal_price());
            ps.setObject(5, cartItem.getRefund()); //Can Be Null!
            ps.setObject(6, cartItem.getCondition().dbValue);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        }
    }

    public void doUpdate(CartItemsBean cartItem) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE cart_items " +
                    "SET id_prod=?, id_cart=?, quantity=?, real_price=?, refund=?, `condition`=? WHERE id_prod=? AND id_cart=?");
            ps.setInt(1, cartItem.getId_prod());
            ps.setInt(2, cartItem.getId_cart());
            ps.setInt(3, cartItem.getQuantity());
            ps.setBigDecimal(4, cartItem.getReal_price());
            ps.setInt(5, cartItem.getRefund());
            ps.setInt(6, cartItem.getCondition().dbValue);
            ps.setInt(7, cartItem.getId_prod());
            ps.setInt(8, cartItem.getId_cart());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }

    public void doRefundByCartItemID(int cartItemID, int cartID) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE cart_items " +
                    "SET refund = 2 WHERE id_prod = ? AND id_cart = ?");
            ps.setInt(1, cartItemID);
            ps.setInt(2, cartID);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("DELETE error.");
            }
        }
    }

    public void doRemoveAllByCartID(CartBean cart) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            if(cart.getId_cart() == null) throw new RuntimeException("No ID Cart Set in remove Items");
            PreparedStatement ps = con.prepareStatement("DELETE FROM cart_items WHERE id_cart = ?");
            ps.setInt(1, cart.getId_cart());
            ps.executeUpdate();
        }
    }

}
