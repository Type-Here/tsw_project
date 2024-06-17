package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.ShippingAddressesBean;
import com.unisa.store.tsw_project.model.beans.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingAddressesDAO {

    public ShippingAddressesBean doRetrieveAddressByAddressesID(int id_add) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM shipping_addresses WHERE id_add=?");
            ps.setInt(1, id_add);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ShippingAddressesBean s = new ShippingAddressesBean();
                s.setId_client(rs.getInt(1));
                s.setId_add(rs.getInt(2));
                s.setFirstname(rs.getString(3));
                s.setLastname(rs.getString(4));
                s.setAddress(rs.getString(5));
                s.setCity(rs.getString(6));
                s.setProv(rs.getString(7));
                s.setCAP(rs.getString(8));
                return s;
            }
            return null;
        }
    }

    // Function to retrieve all shipping addresses for a user
    public List<ShippingAddressesBean> doRetrieveAllByUserId(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM shipping_addresses WHERE id_client = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            List<ShippingAddressesBean> shippingAddresses = new ArrayList<>();
            while (rs.next()) {
                ShippingAddressesBean s = new ShippingAddressesBean();
                s.setId_client(rs.getInt(1));
                s.setId_add(rs.getInt(2));
                s.setFirstname(rs.getString(3));
                s.setLastname(rs.getString(4));
                s.setAddress(rs.getString(5));
                s.setCity(rs.getString(6));
                s.setProv(rs.getString(7));
                s.setCAP(rs.getString(8));
                shippingAddresses.add(s);
            }
            if (shippingAddresses.isEmpty()){
                return null;
            } else {
                return shippingAddresses;
            }
        }
    }

    public void doSave(ShippingAddressesBean shippingAddress) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO shipping_addresses " +
                    "(id_client, firstname, lastname, address, city, prov, cap) VALUES(?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, shippingAddress.getId_client());
            ps.setString(2, shippingAddress.getFirstname());
            ps.setString(3, shippingAddress.getLastname());
            ps.setString(4, shippingAddress.getAddress());
            ps.setString(5, shippingAddress.getCity());
            ps.setString(6, shippingAddress.getProv());
            ps.setString(7, shippingAddress.getCAP());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            shippingAddress.setId_add(rs.getInt(1));
        }
    }


    public void doSaveFirstShippingAddresses(UserBean userBean){
        ShippingAddressesBean shippingAddressesBean = new ShippingAddressesBean();
        shippingAddressesBean.setId_client(userBean.getId());
        shippingAddressesBean.setFirstname(userBean.getFirstname());
        shippingAddressesBean.setLastname(userBean.getLastname());
        shippingAddressesBean.setAddress(userBean.getAddress());
        shippingAddressesBean.setCity(userBean.getCity());
        shippingAddressesBean.setProv(userBean.getProv());
        shippingAddressesBean.setCAP(userBean.getCAP());

        ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();
        try {
            shippingAddressesDAO.doSave(shippingAddressesBean);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void doUpdate(ShippingAddressesBean shippingAddress) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE shipping_addresses " +
                    "SET firstname=?, lastname=?, address=?, city=?, prov=?, cap=? WHERE id_add=?");
            ps.setString(1, shippingAddress.getFirstname());
            ps.setString(2, shippingAddress.getLastname());
            ps.setString(3, shippingAddress.getAddress());
            ps.setString(4, shippingAddress.getCity());
            ps.setString(5, shippingAddress.getProv());
            ps.setString(6, shippingAddress.getCAP());
            ps.setInt(7, shippingAddress.getId_add());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }

    public void doDelete(int id_add) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM shipping_addresses WHERE id_add=?");
            ps.setInt(1, id_add);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("DELETE error.");
            }
        }
    }
}
