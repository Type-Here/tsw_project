package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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

    /**
     * Retrieve a List of different Developers inside product Table
     * @return List of String with developers name
     * @throws SQLException if query fails
     */
    public List<String> doRetrieveDevelopers() throws SQLException{
        List<String> values = new ArrayList<>();
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT developer FROM products GROUP BY developer");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                values.add(rs.getString(1));
            }
        }
        return values;
    }


    /**
     * Select Query Filtered By Parameters. (using def: limit 200, offset 0)
     * @implNote FOR SAFETY REASON: do not use params KEY from user input but manually input it
     * @param params Map: Key: Parameter name, Value:Parameter Value.
     * @return List of Product Bean with all parameters true for Filters
     * @throws SQLException if query fails
     * @see ProductDAO#doRetrieveByParameters(Map, Integer, Integer, List)
     */
    public List<ProductBean> doRetrieveByParameters(Map<String, String> params) throws SQLException {
        return doRetrieveByParameters(params, null, null);
    }

    /**
     * Select Query Filtered By Parameters.
     * @implNote FOR SAFETY REASON: do not use params KEY from user input but manually input it
     * @param params Map: Key: Parameter name, Value:Parameter Value.
     * @param limit number of elements to display in result (if null def: 200)
     * @param offset offset to start selecting elements (if null def: 0)
     * @return List of Product Bean with all parameters true for Filters
     * @throws SQLException if query fails
     * @see ProductDAO#doRetrieveByParameters(Map, Integer, Integer, List)
     */
    public List<ProductBean> doRetrieveByParameters(Map<String, String> params,
                                                    Integer limit, Integer offset) throws SQLException{
        return doRetrieveByParameters(params, limit, offset, null);
    }

    /**
     * Select Query Filtered By Parameters. (Return a SET)
     * @implNote FOR SAFETY REASON: do not use params KEY from user input but manually input it
     * @param params Map: Key: Parameter name, Value:Parameter Value.
     * @param limit number of elements to display in result (if null def: 200)
     * @param offset offset to start selecting elements (if null def: 0)
     * @param categories Array of Categories ID to filter from (Join needed)
     * @return List of Product Bean with all parameters true for Filters
     * @throws SQLException if query fails
     */
    public List<ProductBean> doRetrieveByParameters(Map<String, String> params, Integer limit,
                                                    Integer offset, List<Integer> categories) throws SQLException{
        int catNumber = 0;
        StringBuilder stm = new StringBuilder("SELECT DISTINCT * FROM products");
        if(categories != null && !categories.isEmpty()){
            stm.append(" NATURAL JOIN prod_categories");
            catNumber = categories.size();
        }

        List<ProductBean> products = new ArrayList<>();

        // Set all PARAMETERS KEY IN STRING BUILDER

        stm.append(" WHERE 1"); // THIS return always true but SIMPLIFY CONTROLS ON STRING Append (if param ... append 'AND' ecc)

        //Set Params in MAP (for min and max price append >= or <=, other =
        for (String key : params.keySet()) {
            stm.append(" AND ");
            if(key.equals("minprice")){
                stm.append("price").append(">=?");
            } else if(key.equals("maxprice")){
                stm.append("price").append("<=?");
            } else {
                stm.append(key).append("=?");
            }
        }

        //For Categories: add 'and id_cat IN (?,?,?...,?) more efficient and clean
        //NB: GROUP BY id_prod: Needed to remove DUPLICATES!
        if(catNumber > 0) {
            stm.append(" AND ").append("id_cat").append(" IN( ?");
            stm.append(",?".repeat(catNumber - 1));
            stm.append(" )");
            stm.append(" GROUP BY id_prod");
        }

        //Set LIMIT and OFFSET
        stm.append(" LIMIT ?").append(" OFFSET ?");

        //Prepare the Statement
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement(stm.toString());
            int i = 1;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                ps.setObject(i++, entry.getValue());
            }

            for(int j = 0; j < catNumber; j++){
                ps.setInt(i++, categories.get(j));
            }

            ps.setInt(i++, Objects.requireNonNullElse(limit, 200));
            ps.setInt(i, Objects.requireNonNullElse(offset, 0));

            ResultSet rs = ps.executeQuery();

            //Save Result to List
            while (rs.next()) {
                ProductBean p = populateProduct(rs);
                //Set Categories for All Products in list
                setCategoryList(p);
                products.add(p);
            }
            return products;
        }
    }


    /**
     * Remove product di ID.
     * From the DAO it is not possible to retrieve json and images files, so they need to be removed before this call.
     * @param product to delete
     * @throws SQLException if query fails
     */
    public void doRemoveById(ProductBean product) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement psCat = con.prepareStatement("DELETE FROM prod_categories WHERE id_prod=?");
            PreparedStatement ps = con.prepareStatement("DELETE FROM products WHERE id_prod=?");
            psCat.setInt(1, product.getId_prod());
            ps.setInt(1, product.getId_prod());
            if(psCat.executeUpdate() == 0){
                throw new RuntimeException("DELETE error Categories of the Product");
            }
            if(ps.executeUpdate() == 0){
                throw new RuntimeException("DELETE error Product");
            }
        }
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
        p.setPrice(rs.getBigDecimal(3));
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
        ps.setBigDecimal(2, product.getPrice());
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
