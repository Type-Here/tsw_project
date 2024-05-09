package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.AdminBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {
    public boolean doRetrieveByCredentials(AdminBean adminBean) throws SQLException {
        try (Connection con = ConPool.getConnection()) { //Auto-Closeable
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM admin WHERE user=? AND hash=? ");

            ps.setString(1, adminBean.getUser());
            ps.setString(2, adminBean.getPass());
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

}
