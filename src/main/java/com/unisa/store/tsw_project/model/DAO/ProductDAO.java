package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductDAO {
    /**
     * Find all Products
     * @return a list of ProductBean
     */
    public List<ProductBean> doRetrieveAll() throws SQLException {
        return doRetrieveAll(null, null);
    }

    /**
     * Retrieve all product
     * @param limit number of product retrieved, if null = 200
     * @param orderBy order by string value, if null by id_prod DESC
     * @return List of ProductBean
     * @throws SQLException of fails
     */
    public List<ProductBean> doRetrieveAll(Integer limit, String orderBy) throws SQLException {
        return doRetrieveAll(limit, orderBy, null);
    }

    /**
     * Retrieve all product
     * @param limit number of product retrieved, if null = 200
     * @param orderBy order by string value, if null by id_prod DESC
     * @param offset offset to start giving result from
     * @return List of ProductBean
     * @throws SQLException of fails
     */
    public List<ProductBean> doRetrieveAll(Integer limit, String orderBy, Integer offset) throws SQLException {
        try (Connection con = ConPool.getConnection()) { //Auto-Closeable
            PreparedStatement ps =
                    con.prepareStatement("SELECT * FROM products ORDER BY ? DESC LIMIT ? OFFSET ?");
            //Default Choice by ID
            ps.setString(1, Objects.requireNonNullElse(orderBy, "id_prod"));
            //Limit 200 is default for MariaDB
            ps.setInt(2, Objects.requireNonNullElse(limit, 200));
            //Default offset = 0
            ps.setInt(3, Objects.requireNonNullElse(offset, 0));

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
                ProductBean p = populateProduct(rs);
                //Set Categories for All Products in list
                setCategoryList(p);
                return p;
            }
            return null;
        }
    }

    /**
     * Method to Optimize data retrieve for Main Page <b>Carousel Only</b>
     * @param id of a Product from the carousel
     * @return ProductBean with only ip, name, platform and metadataPath selected
     * @throws SQLException if query fails
     */
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

    public void doSave(ProductBean product) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement("INSERT INTO products " +
                    "(name, price, type, platform, developer, description, metadata, `key`, discount) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    PreparedStatement.RETURN_GENERATED_KEYS);
            setPSAllCampProductBean(product, ps);
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
                    "SET name=?, price=?, type=?, platform=?, developer=?, description=?, metadata=?, `key`=?, discount=? WHERE id_prod=?");
            setPSAllCampProductBean(product, ps);
            ps.setInt(10, product.getId_prod());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("UPDATE error.");
            }
        }
    }

    /**
     * Count Number of Products (Not Single products but Product Type) Available
     * @return Number of Products
     * @throws SQLException if SQL fails
     */
    public int doCountAll() throws SQLException{
        int value = -1;
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT COUNT(*) FROM products");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                value = rs.getInt(1);
            }
        }
        return value;
    }

    /* ----------------------- PRIVATE METHODS ------------------- */


    private List<ProductBean> getProductBeansListSelectAll(PreparedStatement ps) throws SQLException {
        ResultSet rs = ps.executeQuery();
        List<ProductBean> products = new ArrayList<>();
        while (rs.next()) {
            ProductBean p = populateProduct(rs);
            products.add(p);
        }
        //Set Categories for All Products in list
        setCategoryList(products);

        return products;
    }

    /**
     * Create a new ProductBean and populate it with ResultSet Data (1 row)
     * @param rs ResultSet to retrieve data from. Must <b>be checked for 'rs.next' before</b> this method call
     * @return a ProductBean with all setters done except for Category -- TODO
     * @throws SQLException if ConditionBean List retrieve fails
     */
    private ProductBean populateProduct(ResultSet rs) throws SQLException{
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
        p.setDiscount(rs.getDouble(10));

        //Retrieve all Conditions and Quantity of a product
        ConditionDAO conditionDAO = new ConditionDAO();
        p.setConditions(conditionDAO.doRetrieveAllByIdProduct(p));
        return p;
    }

    /**
     * Prepare a Statement with all Product Data, for Update / Save
     * @param product Bean to retrieve data from
     * @param ps to fill with data from bean (update/save)
     * @throws SQLException if query fails
     */
    private void setPSAllCampProductBean(ProductBean product, PreparedStatement ps) throws SQLException {
        ps.setString(1, product.getName());
        ps.setDouble(2, product.getPrice());
        ps.setBoolean(3, product.getType());
        ps.setString(4, product.getPlatform());
        ps.setString(5, product.getDeveloper());
        ps.setString(6, product.getDescription());
        ps.setString(7, product.getMetadataPath());
        ps.setString(8, product.getKey());
        ps.setObject(9, product.getDiscount());
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
