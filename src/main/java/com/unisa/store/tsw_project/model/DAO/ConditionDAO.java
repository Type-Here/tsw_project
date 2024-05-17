package com.unisa.store.tsw_project.model.DAO;

import com.unisa.store.tsw_project.model.beans.ConditionBean;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConditionDAO {

    /**
     * Retrieve a List of ConditionBean for a specific Product ID
     * @param productBean to retrieve data from prod_quantity
     * @return List of ConditionBean of a Specific Product
     * @throws SQLException if select fails
     */
    public List<ConditionBean> doRetrieveAllByIdProduct(ProductBean productBean) throws SQLException {
        List<ConditionBean> conditionBeans = new ArrayList<>();
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("SELECT id_cond, quantity FROM prod_quantity WHERE id_prod=?");
            ps.setInt(1, productBean.getId_prod());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ConditionBean c = new ConditionBean();
                c.setId_cond(rs.getInt(1));
                c.setQuantity(rs.getInt(1));
                conditionBeans.add(c);
            }
            return conditionBeans;
        }
    }

    /**
     * For a new Product Insert: Save all ConditionBeans in list of a specific product
     * @param productBean whose id is used
     * @throws SQLException if fails
     */
    public void doSaveByProduct(ProductBean productBean) throws SQLException {
        try (Connection con = ConPool.getConnection()) {
            for (ConditionBean cond: productBean.getConditions()){
                PreparedStatement ps =
                        con.prepareStatement("INSERT INTO prod_quantity (id_prod, id_cond, quantity) VALUES (?, ?, ?)");
                ps.setInt(1, productBean.getId_prod());
                ps.setInt(2, cond.getId_cond());
                ps.setInt(3, cond.getQuantity());
                if (ps.executeUpdate() != 1) {
                    throw new RuntimeException("INSERT error.");
                }
            }
        }
    }

    /**
     * Use this method to update the quantity of a sold item by product id
     * @param conditionBean needs to have SET id_prod before calling this method
     * @throws SQLException if update fails
     * @apiNote <b>Bean needs to have SET id_prod before calling this method</p>
     */
    synchronized public void doSell(ConditionBean conditionBean) throws SQLException {

        //Return Error if id_prod == 0 -> ID NOT SET
        if(conditionBean.getId_prod() == 0){ throw new InvalidParameterException(); }

        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("UPDATE prod_quantity SET quantity = quantity - 1 WHERE id_prod = ?");
            ps.setInt(1, conditionBean.getId_prod());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        }
    }

    /**
     * Use this method to update quantity by an absolute value (I.E. for Admin Update)
     * @param conditionBean needs to have SET id_prod before calling this method
     * @throws SQLException if update fails
     * @apiNote <b>Bean needs to have SET id_prod before calling this method</p>
     */
    synchronized public void doUpdateByIDProduct(ConditionBean conditionBean) throws SQLException {
        //Return Error if id_prod == 0 -> ID NOT SET
        if(conditionBean.getId_prod() <= 0){ throw new InvalidParameterException(); }

        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps =
                    con.prepareStatement("UPDATE prod_quantity SET quantity = ? WHERE id_prod = ?");
            ps.setInt(1, conditionBean.getQuantity());
            ps.setInt(2, conditionBean.getId_prod());
            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        }
    }




}