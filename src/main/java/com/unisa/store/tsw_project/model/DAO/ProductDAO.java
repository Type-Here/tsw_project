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

    private List<ProductBean> getProductBeansListSelectAll(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<ProductBean> products = new ArrayList<>();
        while (rs.next()) {
            ProductBean p = new ProductBean();
            p.setId_prod(rs.getInt(1));
            p.setName(rs.getString(2));
            p.setPrice(rs.getDouble(3));
            p.setType(rs.getBoolean(4));
            p.setPlatform(rs.getString(5));
            p.setDeveloper(rs.getString(6));
            p.setDescription(rs.getString(7));
            p.setMetadataPath(rs.getString(8));
            p.setKey(rs.getString(9));
            String condition = rs.getString(10);

            // Manage Optional Value to Avoid Illegal Argument Exception when null
            if (condition != null) {
                p.setCondition(Data.Condition.valueOf(condition));
            } else {
                p.setCondition(null);
            }

            p.setDiscount(rs.getDouble(11));
            products.add(p);
        }
        setCategoryList(products);
        return products;
    }

    public List<ProductBean> doRetrieveAll(Integer limit, String orderBy) throws SQLException {
        try (Connection con = ConPool.getConnection()) { //Auto-Closeable
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM products ORDER BY ? DESC LIMIT ? ");
            if(orderBy != null){
                ps.setString(1, orderBy);
            } else ps.setString(1, "id_prod"); //Default Choice

            if(limit != null){
                ps.setInt(2, limit);
            } else ps.setInt(2, 200); //Limit 200 is default for MariaDB

            return getProductBeansListSelectAll(ps);
        }
    }

    public List<ProductBean> doRetrieveByName(String name) throws SQLException{
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM products WHERE name LIKE ?");
            ps.setString(1, "%" + name + "%");
            return getProductBeansListSelectAll(ps);
        }
    }

    public ProductBean doRetrieveById(int id) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM products WHERE id_prod=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ProductBean p = new ProductBean();
                p.setId_prod(rs.getInt(1));
                p.setName(rs.getString(2));
                p.setPrice(rs.getDouble(3));
                p.setType(rs.getBoolean(4));
                p.setPlatform(rs.getString(5));
                p.setDeveloper(rs.getString(6));
                p.setDescription(rs.getString(7));
                p.setMetadataPath(rs.getString(8));
                p.setKey(rs.getString(9));
                String condition = rs.getString(10);

                // Manage Optional Value to Avoid Illegal Argument Exception when null
                if (condition != null) {
                    p.setCondition(Data.Condition.valueOf(condition));
                } else {
                    p.setCondition(null);
                }

                p.setDiscount(rs.getDouble(11));
                setCategoryList(p);
                return p;
            }
            return null;
        }
    }

    public ProductBean doRetrieveCarousel(int id) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT  name, platform, metadata FROM products WHERE id_prod=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ProductBean p = new ProductBean();
                p.setId_prod(id);
                p.setName(rs.getString(1));
                p.setPlatform(rs.getString(2));
                p.setMetadataPath(rs.getString(3));
                return p;
            }
            return null;
        }
    }

    private void setpsAllCampProductBean(ProductBean product, PreparedStatement ps) throws SQLException {
        ps.setString(1, product.getName());
        ps.setDouble(2, product.getPrice());
        ps.setBoolean(3, product.getType());
        ps.setString(4, product.getPlatform());
        ps.setString(5, product.getDeveloper());
        ps.setString(6, product.getDescription());
        ps.setString(7, product.getMetadataPath());
        ps.setString(8, product.getKey());
        ps.setString(9, product.getCondition()== null ? null : product.getCondition().toString()); //Enum to String Da vedere
        ps.setObject(10, product.getDiscount());
    }

    public void doSave(ProductBean product) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO products " +
                    "(name, price, type, platform, developer, description, metadata, `key`, `condition`, discount) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            setpsAllCampProductBean(product, ps);
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            product.setId_prod(rs.getInt(1)); //Set the new ID in the ProductBean

            CategoryDAO categoryDAO = new CategoryDAO();
            categoryDAO.doSaveProductCategories(product);

        }
    }

    public void doUpdate(ProductBean product) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("UPDATE products " +
                    "SET name=?, price=?, type=?, platform=?, developer=?, description=?, metadata=?, `key`=?, `condition`=?, discount=? WHERE id_prod=?");
            setpsAllCampProductBean(product, ps);
            ps.setInt(11, product.getId_prod());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }

    private void setCategoryList(ProductBean productBean) throws SQLException {
        CategoryDAO categoryDAO = new CategoryDAO();
        productBean.setCategoryBeanList(categoryDAO.doRetriveCategoryListByProductId(productBean.getId_prod()));
    }

    private void setCategoryList(List<ProductBean> productBeanList) throws SQLException {
        for (ProductBean p: productBeanList){
            setCategoryList(p);
        }
    }
}
