package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.Data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    /**
     * Find all Products
     * @return a list of ProductBean
     */
    public List<ProductBean> doRetrieveAll() throws SQLException {
        return doRetrieveAll(null, null);
    }

    public List<ProductBean> doRetrieveAll(Integer limit, String orderBy) throws SQLException {
        try (Connection con = ConPool.getConnection()) { //Auto-Closeable
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM products ORDER BY ? LIMIT ? ");
            if(orderBy != null){
                ps.setString(1, orderBy);
            } else ps.setString(1, "id_prod"); //Default Choice

            if(limit != null){
                ps.setInt(2, limit);
            } else ps.setInt(2, 200); //Limit 200 is default for MariaDB

            ResultSet rs = ps.executeQuery();
            List<ProductBean> products = new ArrayList<>();
            while (rs.next()) {
                ProductBean p = new ProductBean();
                p.setId_prod(rs.getInt(1));
                p.setName(rs.getString(2));
                p.setPrice(rs.getDouble(3));
                p.setType(rs.getBoolean(4));
                p.setPlatform(rs.getString(5));
                p.setMetadataPath(rs.getString(6));
                p.setKey(rs.getString(7));
                String condition = rs.getString(8);

                // Manage Optional Value to Avoid Illegal Argument Exception when null
                if (condition != null) {
                    p.setCondition(Data.Condition.valueOf(condition));
                } else {
                    p.setCondition(null);
                }

                p.setDiscount(rs.getDouble(9));
                products.add(p);
            }
            return products;
        }
    }
}
