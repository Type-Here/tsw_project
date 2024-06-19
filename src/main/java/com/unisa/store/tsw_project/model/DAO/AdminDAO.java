package com.unisa.store.tsw_project.model.DAO;
import com.unisa.store.tsw_project.model.beans.AdminBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    /**
     * Return true if credential are valid, false otherwise. <br />
     * Bean password has to be already hashed with salt.
     * @param bean admin bean to validate
     * @return true is Admin exists
     * @throws SQLException if query fails
     */
    public boolean doRetrieveByCredentials(AdminBean bean) throws SQLException {
        try (Connection con = ConPool.getConnection()) { //Auto-Closeable

            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM admin WHERE user=? AND hash=?");
            ps.setString(1, bean.getUser());
            ps.setString(2, bean.getPass());

            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }


    /**
     * Retrieve Salt and Password Hash of a given admin username
     * @param user username of the hypothetical admin
     * @return a String Array with two elements: <br />
     * [0] Salt <br />
     * [1] Hash <br />
     * @throws SQLException if query fails
     */
    public String[] doRetrieveSaltAndHash(String user) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement retrieveSalt = con.prepareStatement("SELECT salt, hash FROM admin WHERE user = ?");
            String[] data = null;
            retrieveSalt.setString(1, user);
            ResultSet saltSet = retrieveSalt.executeQuery();
            if (saltSet.next()) {
                data = new String[2];
                data[0] = saltSet.getString("salt");
                data[1] = saltSet.getString("hash");
            }
            return data;
        }
    }


    /**
     * Update Admin Password with new Salt. Password already has to be already Hashed.
     * @param bean AdminBean with Username and new Password
     * @param salt String of new Salt
     * @throws SQLException if update fails
     */
    public void doUpdatePassword(AdminBean bean, String salt) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE admin SET hash = ?, salt = ? WHERE user = ?"); //Already Hashed Password
            ps.setString(1, bean.getPass());
            ps.setString(2, salt);
            ps.setString(3, bean.getUser());
            if (ps.executeUpdate() < 1) {
                throw new SQLException("Failed to update password");
            }
        }
    }

}
