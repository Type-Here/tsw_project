package com.unisa.store.tsw_project.model.beans;

import java.math.BigDecimal;
import java.util.Map;

public class CartBean {
    private Integer id_cart;
    private BigDecimal total;
    private Integer id_client;
    private String discount_code;
    private Map<String,CartItemsBean> cartItems;
    private Boolean isActive;

    public CartBean() {
    }

    public CartBean(int id_cart, BigDecimal total, int id_client, String discount_code, Map<String, CartItemsBean> cartItems, Boolean isActive) {
        this.id_cart = id_cart;
        this.total = total;
        this.id_client = id_client;
        this.discount_code = discount_code;
        this.cartItems = cartItems;
        this.isActive = isActive;
    }

    /* - GETTERS - */

    public Integer getId_cart() {
        return id_cart;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Integer getId_client() {
        return id_client;
    }

    public String getDiscount_code() {
        return discount_code;
    }

    public Map<String,CartItemsBean> getCartItems() {return cartItems;}

    public Boolean getActive() {return isActive;}

    /* - SETTERS - */

    public void setId_cart(Integer id_cart) {
        this.id_cart = id_cart;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setId_client(Integer id_client) {
        this.id_client = id_client;
    }

    public void setDiscount_code(String discount_code) {
        this.discount_code = discount_code;
    }

    public void setCartItems(Map<String,CartItemsBean> cartItems) {this.cartItems = cartItems;}

    public void setActive(Boolean active) {isActive = active;}

    /* - OTHER - */
    public void addCartItem(CartItemsBean cartItem) {
        this.cartItems.put(cartItem.getId_prod() + cartItem.getCondition().toString(), cartItem);
    }


    /**
     * Add a CartItemsBean to Map. If Item with same key exists replace it.
     * @param cartItem to insert
     */
    public void setAttribute(CartItemsBean cartItem){
        if(this.cartItems.get(cartItem.getId_prod() + cartItem.getCondition().toString())!=null){
            this.cartItems.replace(cartItem.getId_prod() + cartItem.getCondition().toString(), cartItem);
            return;
        }
        this.cartItems.put(cartItem.getId_prod() + cartItem.getCondition().toString(), cartItem);
    }

    /**
     * Add a CartItemsBean to Map. <br />
     * If Item with same key exists replace it if replace is True, Otherwise return.
     * @param cartItem to insert
     */
    public void setAttribute(CartItemsBean cartItem, boolean replace){
        if(this.cartItems.get(cartItem.getId_prod() + cartItem.getCondition().toString())!=null){
            if(replace) this.cartItems.replace(cartItem.getId_prod() + cartItem.getCondition().toString(), cartItem);
            return;
        }
        this.cartItems.put(cartItem.getId_prod() + cartItem.getCondition().toString(), cartItem);
    }

    @Override
    public String toString() {
        return "CartBean{" +
                "id_cart=" + id_cart +
                ", total=" + total +
                ", id_client=" + id_client +
                ", discount_code='" + discount_code + '\'' +
                ", cartItems=" + cartItems +
                '}';
    }
}
