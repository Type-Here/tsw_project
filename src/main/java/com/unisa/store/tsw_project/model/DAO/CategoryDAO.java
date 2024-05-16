package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.CategoryBean;
import com.unisa.store.tsw_project.model.beans.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<CategoryBean> doRetriveCategoryListByProductId(int id) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_cat, typename FROM prod_categories NATURAL JOIN " +
                            "categories NATURAL JOIN products WHERE id_prod=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<CategoryBean> categoryBeanList = new ArrayList<>();
            while (rs.next()) {
                CategoryBean c = new CategoryBean();
                c.setId_cat(rs.getInt(1));
                c.setTypename(rs.getString(2));
                categoryBeanList.add(c);
            }
            return categoryBeanList;
        }
    }

    public void doSave(CategoryBean categoryBean) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("INSERT INTO categories (typename)  VALUES (?)");
            ps.setString(1, categoryBean.getTypename());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        }
    }

    public void doSaveProductCategories(ProductBean productBean) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            for (CategoryBean categoryBean: productBean.getCategoryBeanList()){
                PreparedStatement ps =
                        con.prepareStatement("INSERT INTO prod_categories (id_prod, id_cat) VALUES (?, ?)");
                ps.setInt(1, productBean.getId_prod());
                ps.setInt(1, categoryBean.getId_cat());
                if (ps.executeUpdate() != 1) {
                    throw new RuntimeException("INSERT error.");
                }
            }
        }
    }
}
