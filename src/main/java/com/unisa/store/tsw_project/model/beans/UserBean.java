package com.unisa.store.tsw_project.model.beans;

import com.unisa.store.tsw_project.controller.user.TheOrderServlet;
import com.unisa.store.tsw_project.model.DAO.CartDAO;
import com.unisa.store.tsw_project.model.DAO.CartItemsDAO;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class UserBean implements HttpSessionBindingListener {
    private int id;
    private String firstname;
    private String lastname;
    private String telephone;
    private String email;
    private LocalDate birth;
    private String address;
    private String city;
    private String prov;
    private String CAP;
    private Integer id_cred;


    public UserBean() {
    }

    public UserBean(int id, String firstname, String lastname, String telephone, String email, LocalDate birth, String address, String city, String prov, String CAP, int id_cred) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.telephone = telephone;
        this.email = email;
        this.birth = birth;
        this.address = address;
        this.city = city;
        this.prov = prov;
        this.CAP = CAP;
        this.id_cred = id_cred;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProv() {
        return prov;
    }

    public String getCAP() {
        return CAP;
    }

    public int getId_cred() {
        return id_cred;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public void setCAP(String CAP) {
        this.CAP = CAP;
    }

    public void setId_cred(Integer id_cred) {
        this.id_cred = id_cred;
    }


    /* ------- ACTIONS FOR HttpSessionBindingListener ------- */

    /**
     * @implNote When an instance of this ActiveUser get set as a session attribute by HttpSession#setAttribute(),
     * then the valueBound() will be invoked. <br />
     * It's notified when an UserBean Object is Set. <br />
     * @apiNote Load an Active Cart from DataBase in session. <br />
     * If the same product is loaded keep the quantity already in session (newer data preferred)
     * @param event the event that identifies the session.
     *
     */
    @Override
    public void valueBound(HttpSessionBindingEvent event) {

        HttpSession session = event.getSession();
        UserBean user = (UserBean) event.getValue();

        CartDAO cartDAO = new CartDAO();
        CartItemsDAO itemsDAO = new CartItemsDAO();
        try {
            // Retrieve current Cart from Session
            CartBean current = (CartBean) session.getAttribute("cart");

            // Retrieve old Cart from DataBase
            CartBean previous = cartDAO.doRetrieveActive(user.getId());

            // Set a New Cart if No Cart in session is Found
            if(current == null){
                current = new CartBean();
                current.setCartItems(new LinkedHashMap<>()); //Initiate HashMap ( Key: id_prod (Integer), value: CartItem )
            }
            current.setActive(true);
            current.setId_client(user.getId());

            //Retrieve old CartItems and load in Session if possible.
            if(previous != null) {
                List<CartItemsBean> items = itemsDAO.doRetrieveAllByCartId(previous.getId_cart());
                current.setId_cart(previous.getId_cart());
                for(CartItemsBean item : items) {
                    current.setAttribute(item, false);
                }
            }
            event.getSession().setAttribute("cart", current);

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * @implNote When it gets removed by either HttpSession#removeAttribute(),
     * or an invalidate of the session, or get replaced by another HttpSession#setAttribute(),
     * then the valueUnbound() will be invoked.
     * @param event the event that identifies the session
     * @apiNote As for now, UserBean is replaced only on Logout or Session Invalidate. <br />
     * Save Cart and CartItems if exist on DataBase.
     */
    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        Optional<CartBean> cart = Optional.ofNullable((CartBean) event.getSession().getAttribute("cart"));
        UserBean user = (UserBean) event.getValue();

        // If Cart is Null or is Not the Current Cart (i.e. user already made the order) nothing has to be done
        if (cart.isEmpty() || cart.get().getActive() == null || !cart.get().getActive()) return;

        try {
            CartDAO cartDAO = new CartDAO();
            CartItemsDAO itemsDAO = new CartItemsDAO();

            cart.get().setId_client(user.getId());
            cart.get().setActive(true);

            //Set Prices for Each Item and Total in Cart
            BigDecimal total = TheOrderServlet.checkEachProduct(cart.get(), null);
            cart.get().setTotal(total);

            CartBean previous = cartDAO.doRetrieveActive(cart.get().getId_client());

            //Update Cart if Already Exists in DataBase
            if (previous != null) {
                cart.get().setId_cart(previous.getId_cart()); //Set same ID
                cartDAO.doUpdate(cart.get());

                //Remove all previous CartItems as we're not sure user has modified the Data
                itemsDAO.doRemoveAllByCartID(cart.get());

            } else {
                cartDAO.doSave(cart.get());
            }

            //Save New Items
            for (CartItemsBean item : cart.get().getCartItems().values()){
                item.setId_cart(cart.get().getId_cart());
                itemsDAO.doSave(item);
            }

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
