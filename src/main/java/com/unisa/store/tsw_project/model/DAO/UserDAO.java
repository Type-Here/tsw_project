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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class UserDAO {

    /**
     * Retrieve a List of UserBeans
     * @param limit limit result to this value. If null default = 200
     * @param offset offset the results to this value, if null default = 0
     * @return List of UserBean
     * @throws SQLException if query fails
     */
    public List<UserBean> doRetrieveUsers(Integer limit, Integer offset) throws SQLException {
        List<UserBean> users = new ArrayList<>();
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users LIMIT ? OFFSET ?");
            ps.setInt(1, Objects.requireNonNullElse(limit, 200)); //limit or default 200
            ps.setInt(2, Objects.requireNonNullElse(offset, 0)); //offset or default 0
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(setUserBeanFromRS(rs, true));
            }
        }
        return users;
    }

    // LocalDate to Date da verificare funzionamento

    public UserBean getUserByEmail(String email) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE email =?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return setUserBeanFromRS(rs, false);
            }
            return null;
        }
    }


    /**
     * OverLoaded <br />
     * Retrieve User by his ID. isAdminView will set id_credential and birthdate to null if is True. <br />
     * The 1 Parameter method use default value of isAdminView = false
     * @param id_client user id to retrieve
     * @param isAdminView true = return UserBean with d_credential and birthdate set to null. false = return normal UserBean
     * @return UserBean with the data of the user with id_client equal to parameter
     * @throws SQLException if query fails
     */
    public UserBean getUserById(int id_client, boolean isAdminView) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM users WHERE id_client = ?");
            ps.setInt(1, id_client);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return setUserBeanFromRS(rs, isAdminView);
            }
            return null;
        }
    }


    /**
     * @see UserDAO#getUserById(int, boolean)
     * @param id_client id of the user
     * @return UserBean with the data of the User of id_client = to parameter
     * @throws SQLException if query fails
     */
    public UserBean getUserById(int id_client) throws SQLException {
        return getUserById(id_client, false);
    }

    /**
     * Populate an UserBean from ResultSet
     * @param rs ResultSet
     * @return UserBean
     * @throws SQLException if data retrieve fails
     */
    private UserBean setUserBeanFromRS(ResultSet rs, boolean isAdminView) throws SQLException {
        UserBean u = new UserBean();
        u.setId(rs.getInt(1));
        u.setFirstname(rs.getString(2));
        u.setLastname(rs.getString(3));
        u.setTelephone(rs.getString(4));
        u.setEmail(rs.getString(5));
        u.setBirth(isAdminView? null : rs.getDate(6).toLocalDate());
        u.setAddress(rs.getString(7));
        u.setCity(rs.getString(8));
        u.setProv(rs.getString(9));
        u.setCAP(rs.getString(10));
        u.setId_cred(isAdminView ? null : rs.getInt(11));
        return u;
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