package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.UserBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // LocalDate to Date da verificare funzionamento

    public UserBean getUserById(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()){
            PreparedStatement ps = con.prepareStatement("SELECT * FROM user WHERE id = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                UserBean u = new UserBean();
                u.setId(ps.executeQuery().getInt(1));
                u.setFirstname(ps.executeQuery().getString(2));
                u.setLastname(ps.executeQuery().getString(3));
                u.setTelephone(ps.executeQuery().getString(4));
                u.setEmail(ps.executeQuery().getString(5));
                u.setBirth(ps.executeQuery().getDate(6).toLocalDate());
                u.setAddress(ps.executeQuery().getString(7));
                u.setCity(ps.executeQuery().getString(8));
                u.setProv(ps.executeQuery().getString(9));
                u.setCAP(ps.executeQuery().getString(10));
                u.setId_cred(ps.executeQuery().getInt(11));
                return u;
            }
            return null;
        }
    }

    private void setpsAllCampUserBean(UserBean user, PreparedStatement ps) throws SQLException {
        ps.setString(1, user.getFirstname());
        ps.setString(2, user.getLastname());
        ps.setString(3, user.getTelephone());
        ps.setString(4, user.getEmail());
        ps.setDate(5, java.sql.Date.valueOf(user.getBirth()));
        ps.setString(6, user.getAddress());
        ps.setString(7, user.getCity());
        ps.setString(8, user.getProv());
        ps.setString(9, user.getCAP());
        ps.setInt(10, user.getId_cred());
    }

    public void doSave(UserBean user) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO user " +
                    "(firstname, lastname, telephone, email, birth, address, city, prov, cap, id_cred)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            setpsAllCampUserBean(user, ps);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            user.setId(rs.getInt(1));
        }
    }

    public void doUpdate(UserBean user) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE user " +
                    "SET firstname=?, lastname=?, telephone=?, email=?, birth=?," +
                    " address=?, city=?, prov=?, cap=?, id_cred=? WHERE id=?");
            setpsAllCampUserBean(user, ps);
            ps.setInt(11, user.getId());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }
}
