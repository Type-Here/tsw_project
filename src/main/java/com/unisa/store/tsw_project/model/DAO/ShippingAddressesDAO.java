package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.ShippingAddressesBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShippingAddressesDAO {

    public List<ShippingAddressesBean> doRetriveAllByUserId(int id_client) throws SQLException {
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
            shippingAddress.setId_add(rs.getInt(2)); // Da controllare
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
}
