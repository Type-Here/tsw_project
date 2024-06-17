package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.UserBean;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;

public class UserDAO {

    // LocalDate to Date da verificare funzionamento

    public UserBean getUserByEmail(String email) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE email =?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserBean u = new UserBean();
                u.setId(rs.getInt(1));
                u.setFirstname(rs.getString(2));
                u.setLastname(rs.getString(3));
                u.setTelephone(rs.getString(4));
                u.setEmail(rs.getString(5));
                u.setBirth(rs.getDate(6).toLocalDate());
                u.setAddress(rs.getString(7));
                u.setCity(rs.getString(8));
                u.setProv(rs.getString(9));
                u.setCAP(rs.getString(10));
                u.setId_cred(rs.getInt(11));
                return u;
            }
            return null;
        }
    }

    public UserBean getUserById(int id_client) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id_client = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                UserBean u = new UserBean();
                u.setId(rs.getInt(1));
                u.setFirstname(rs.getString(2));
                u.setLastname(rs.getString(3));
                u.setTelephone(rs.getString(4));
                u.setEmail(rs.getString(5));
                u.setBirth(rs.getDate(6).toLocalDate());
                u.setAddress(rs.getString(7));
                u.setCity(rs.getString(8));
                u.setProv(rs.getString(9));
                u.setCAP(rs.getString(10));
                u.setId_cred(rs.getInt(11));
                return u;
            }
            return null;
        }
    }

    // Funzione per settare tutti i campi di un UserBean in un PreparedStatement
    private void setPsAllCampUserBean(UserBean user, PreparedStatement ps) throws SQLException {
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
            PreparedStatement ps = con.prepareStatement("INSERT INTO users " +
                    "(firstname, lastname, telephone, email, birth, address, city, prov, cap, id_cred)" +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            setPsAllCampUserBean(user, ps);
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
            PreparedStatement ps = con.prepareStatement("UPDATE users " +
                    "SET firstname=?, lastname=?, telephone=?, email=?, birth=?," +
                    " address=?, city=?, prov=?, cap=?, id_cred=? WHERE id_client=?");
            setPsAllCampUserBean(user, ps);
            ps.setInt(11, user.getId());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }

    public String[] doRetrieveHashAndSaltByUserId(int id_cred) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT pass_hash, pass_salt FROM credentials WHERE id_cred = ?");
            ps.setInt(1, id_cred);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String[] result = new String[2];
                result[0] = rs.getString(1);
                result[1] = rs.getString(2);
                return result;
            }
            return null;
        }
    }

    public void doUpdatePassword(String password, int id_cred) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE credentials SET pass_hash = SHA2(?, 256), pass_salt = ? WHERE id_cred = ?");
            String salt = generateSalt();
            ps.setString(1, password + salt);
            ps.setString(2, salt);
            ps.setInt(3, id_cred);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        }
    }

    public int doSaveCredentials(String hash) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO credentials (pass_hash, pass_salt, creation_date) VALUES (SHA2(?, 256), ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            String salt = generateSalt();
            ps.setString(1, hash + salt);
            ps.setString(2, salt);
            ps.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            return rs.getInt(1);
        }
    }

    public void doDeleteCredentials(int id_cred) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("DELETE FROM credentials WHERE id_cred = ?");
            ps.setInt(1, id_cred);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("DELETE error.");
            }
        }
    }

    //Funzioni per controllo password

    public boolean checkPassword(String password, int id_cred) throws SQLException {
        String[] hashSalt = doRetrieveHashAndSaltByUserId(id_cred);
        password = password + hashSalt[1];

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            password = String.format("%064x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hashSalt[0].equals(password);
    }

    private String generateSalt() {
        int length = 6; // Lunghezza della stringa casuale
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Caratteri da cui scegliere

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = rnd.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

}