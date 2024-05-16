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

    /**
     * Retrieve a list of CategoryBeans with all category in DataBase
     * @return List of All Category in DB
     * @throws SQLException if query fails
     */
    public List<CategoryBean> doRetrieveAll() throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM categories");
            ResultSet rs = ps.executeQuery();
            List<CategoryBean> categoryBeanList = new ArrayList<>();
            while (rs.next()) {
                CategoryBean c = setCategoryBean(rs);
                categoryBeanList.add(c);
            }
            return categoryBeanList;
        }
    }



    public List<CategoryBean> doRetriveCategoryListByProductId(int id) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_cat, typename FROM prod_categories NATURAL JOIN " +
                            "categories NATURAL JOIN products WHERE id_prod=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<CategoryBean> categoryBeanList = new ArrayList<>();
            while (rs.next()) {
                CategoryBean c = setCategoryBean(rs);
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
                ps.setInt(2, categoryBean.getId_cat());
                if (ps.executeUpdate() != 1) {
                    throw new RuntimeException("INSERT error.");
                }
            }
        }
    }


    /* ------------------ PRIVATE METHODS ------------------- */

    /**
     * Set a new CategoryBean from data of a ResultSet
     * @param rs ResultSet (check if not null is needed before this method call)
     * @return a full CategoryBean
     * @throws SQLException if get values from rs fails
     */
    private CategoryBean setCategoryBean(ResultSet rs) throws SQLException{
        CategoryBean c = new CategoryBean();
        c.setId_cat(rs.getInt(1));
        c.setTypename(rs.getString(2));
        return c;
    }

}
