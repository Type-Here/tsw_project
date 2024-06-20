package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.*;
import com.unisa.store.tsw_project.model.beans.*;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebServlet(name = "OrderServlet", urlPatterns = "/order")
public class TheOrderServlet extends HttpServlet {

    /**
     * Load Order Page
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       if(isInvalidUser(req)) {
           resp.sendRedirect(req.getContextPath() + "/user-login?redirect=order");
           return;
       }

        //If verification is passed user exists
        UserBean user = (UserBean) req.getSession().getAttribute("userlogged");
        try {
            ShippingAddressesDAO addressDAO = new ShippingAddressesDAO();

            /* Set a Temporary Total to Display */
            setTotal((CartBean) req.getSession().getAttribute("cart"));

            List<ShippingAddressesBean> addresses = addressDAO.doRetrieveAllByUserId(user.getId());
            req.setAttribute("addresses", addresses);
            req.getRequestDispatcher("/WEB-INF/results/order.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Only AJAX: Make the Order
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /* AJAX Header */
        Optional<String> requestType = Optional.ofNullable(req.getHeader("X-Requested-With"));
        if(requestType.isEmpty() || !requestType.get().equals("XMLHttpRequest")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        DataValidator validator = new DataValidator();
        /* User, Cart, Parameter Validation */

        if(isInvalidUser(req)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid User or Cart");
            return;
        }

        //If verification is passed user and cart/cartItems exist
        Optional<String> orderCmd = Optional.ofNullable(req.getParameter("order"));
        if(orderCmd.isEmpty() || !orderCmd.get().equals("true")){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order parameter");
            return;
        }

        Optional<String> shippingAddress = Optional.ofNullable(req.getParameter("address"));
        if(shippingAddress.isEmpty() ||
                !validator.validatePattern(shippingAddress.get(), DataValidator.PatternType.Int, 1, null)){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order parameter");
            return;
        }

        try {

            Optional<String> name = Optional.ofNullable(req.getParameter("name"));
            Optional<String> pan = Optional.ofNullable(req.getParameter("pan"));
            Optional<String> cvv = Optional.ofNullable(req.getParameter("cvv"));
            Optional<String> date = Optional.ofNullable(req.getParameter("expire"));

            if(name.isEmpty() || pan.isEmpty() || cvv.isEmpty() || date.isEmpty()){
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Check Payment Parameters");
                return;
            }

            validator.validatePattern(name.get().trim(), DataValidator.PatternType.Generic);
            validator.validatePattern(pan.get().trim(), DataValidator.PatternType.GenericAlphaNumeric, 16, 16);
            validator.validatePattern(cvv.get().trim(), DataValidator.PatternType.StringOnlyNumbers, 3, 3);
            validator.validatePattern(date.get().trim(), DataValidator.PatternType.DateFuture);

            /* VALIDATE PAYMENT DETAILS THAN DO NOTHING SINCE IT'S ONLY A PROJECT */

            CartBean cart = (CartBean) req.getSession().getAttribute("cart");
            UserBean user = (UserBean) req.getSession().getAttribute("userlogged");

            CartDAO cartDao = new CartDAO();
            CartItemsDAO itemsDAO = new CartItemsDAO();
            OrdersDAO ordersDAO = new OrdersDAO();
            OrdersBean order = new OrdersBean();
            ShippingAddressesDAO shippingAddressDAO = new ShippingAddressesDAO();
            ConditionDAO conditionDAO = new ConditionDAO();

            // To be sure, to be sure

            /* Check For ShippingAddress */
            List<ShippingAddressesBean> addresses = shippingAddressDAO.doRetrieveAllByUserId(user.getId());
            ShippingAddressesBean address = addresses.stream()
                    .filter( ad -> ad.getId_add() == Integer.parseInt(shippingAddress.get())).findFirst()
                    .orElseThrow(() -> new InvalidParameterException("Shipping Address Not Found"));

            /* Set Id-Client */
            cart.setId_client(user.getId());

            /* CALCULATE TOTAL - VALUTATE QUANTITY And CONDITION For Each Product; Valutate DISCOUNT CODE */
            setTotal(cart);

            /* Prepare Cart to DB Save: set Active false as it will not be the current cart anymore */
            cart.setActive(false);

            // Cart is already Saved in DB if it has an ID but needs and update if user has added/removed some items
            // If Cart is Saved: Remove old data and save new items.
            if (cart.getId_cart() != null && cart.getId_cart() != 0) {
                cartDao.doUpdate(cart);
                itemsDAO.doRemoveAllByCartID(cart);
            } else {
                cartDao.doSave(cart);
            }

            for(Map.Entry<String, CartItemsBean> item : cart.getCartItems().entrySet()){
                item.getValue().setId_cart(cart.getId_cart());
                itemsDAO.doSave(item.getValue());
            }

            /* (Re)Set total order price */
            order.setStatus(Data.OrderStatus.INPROCESS);
            order.setOrder_date(LocalDate.now());
            order.setId_cart(cart.getId_cart());
            order.setId_client(user.getId());
            order.setId_add(address.getId_add());

            /* Save Order */
            ordersDAO.doSave(order);

            /* Update Quantity if Physical */
            for(CartItemsBean item : cart.getCartItems().values()){
                if(!item.getCondition().equals(Data.Condition.X)){
                    conditionDAO.doSell(item.getId_prod(),item.getCondition().dbValue, item.getQuantity());
                }
            }


            /* Debug */
            if(getServletContext().getAttribute("debug") != null){
                System.err.println("----- ORDER DEBUG [INFO]: ---- ");
                System.out.println("- Cart: " + cart);
                System.out.println("- Order: " + order);
                for(CartItemsBean item : cart.getCartItems().values()){
                    System.out.println(" - Item: " + item);
                }
            }


            /* Clear Cart from Session */
            req.getSession().removeAttribute("cart");

            /* Write Message Answer */
            resp.setContentType("text/html");
            resp.getWriter().write("Acquisto Effettuato!");
            resp.getWriter().flush();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InvalidParameterException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    /* ====================== CALCULATE DATA FOR ORDER METHODS ======================== */


    /**
     * Calculate Total for the Current Cart in Session <br />
     * <ul>
     *     <li>Set Discount Code Value</li>
     *     <li>Set Discount for B-E Condition Products </li>
     *     <li>Calculate price for each physical product by its quantity</li>
     *     <li> Eventually Add Shipping Costs </li>
     * </ul>
     * @implNote Total set inside Cart#total variable (BigDecimal)
     * @param cart CartBean to calculate total and set in Bean
     * @throws SQLException if query for product data retrieval fails
     */
    private void setTotal(CartBean cart) throws SQLException {
        /* Check For Discount */
        Map<String, Double> discounts = (Map<String, Double>) getServletContext().getAttribute("discountCode");
        Double discountValue = null;

        // Retrieve Discount
        if(cart.getDiscount_code() != null && !discounts.isEmpty()){
            discountValue = discounts.get(cart.getDiscount_code());
        }

        if(discountValue == null){ //Reset Discount Name if it's not valid anymore
            cart.setDiscount_code(null);
        }

        /* Check Item and Set Total */
        BigDecimal total = checkEachProduct(cart, discountValue); //Throws InvalidParameterException if some values are not valid!

        /* If Total is Less Than 100€ add Shipping Costs */
        BigDecimal shippingCost = (BigDecimal) getServletContext().getAttribute("shippingCost");
        if(total.compareTo(BigDecimal.valueOf(100.00)) < 0) total = total.add(shippingCost);
        cart.setTotal(total);
    }






    /**
     * Check Each Product in Cart: <br />
     * <ul>
     *     <li>Check for Product if Exists</li>
     *     <li>Check for Any Discount applied to Product in general</li>
     *     <li>If Physical: Check for Condition and apply its discount</li>
     *     <li>If Physical: Check for Quantity Left vs Requested</li>
     *     <li>Apply Any Discount Code Value to each Product</li>
     * </ul>
     * @param cart Cart in which Items are checked and price counted
     * @param discountCodeValue Nullable value. Possible Discount Code Value to apply
     * @return Total Price of Items in Cart with all Discount Counted
     * @throws SQLException if Queries to Retrieve Product Data fail
     */
    public static BigDecimal checkEachProduct(CartBean cart, Double discountCodeValue) throws SQLException {
        BigDecimal total = new BigDecimal(0);
        ProductDAO productDAO = new ProductDAO();

        for(Map.Entry<String, CartItemsBean> item : cart.getCartItems().entrySet()){
            CartItemsBean cartItem = item.getValue();
            cartItem.setRefund(1); // 1 is for Bought Element
            ProductBean p = productDAO.doRetrieveById(cartItem.getId_prod());

            /* Check for the Product and its applied Discount */
            if(p == null){
                throw new InvalidParameterException("Product Not Found");
            }

            //Check for Any Discount in Product Table
            BigDecimal discountedPrice = p.getPrice().multiply(BigDecimal.valueOf((1 - p.getDiscount()/100)));

            /* Check for Condition and its Discount if Product is Physical */
            if(!p.getType()){ //1-Digital 0-Physical

                //Find condition in Product Conditions List
                ConditionBean cond = p.getConditions().stream()
                        .filter((c) -> c.getId_cond() == cartItem.getCondition().dbValue).findFirst()
                        .orElseThrow(() -> new InvalidParameterException("condition"));

                //We're here, check if product left is less than required by user
                if(cond.getQuantity() < cartItem.getQuantity()){
                    throw new InvalidParameterException("Insufficient Quantity");
                }

                //Apply any Condition Discount
                discountedPrice = discountedPrice.multiply(BigDecimal.valueOf((1 - cond.getCondition().getDiscount()/100)));
            }

            /* Apply Any DiscountCodeValue */
            if(discountCodeValue != null){
                discountedPrice = discountedPrice.multiply(BigDecimal.valueOf(1 - discountCodeValue/100));
            }

            /* Set Value and update Total */
            cartItem.setReal_price(discountedPrice);
            total = total.add(discountedPrice);
        }
        return total;
    }





    /* ================ SECURITY ==================== */


    /**
     * Validate UserLogged and Check for Cart Existence. <br/>
     * Check if Cart is not Empty.
     * @param req Servlet Request
     * @return true if user and cart are valid, false otherwise
     */
    private boolean isInvalidUser(HttpServletRequest req) {
        Optional<Object> user = Optional.ofNullable(req.getSession().getAttribute("userlogged"));
        Optional<CartBean> cart = Optional.ofNullable((CartBean) req.getSession().getAttribute("cart"));
        return user.isEmpty() || cart.isEmpty() || cart.get().getCartItems().isEmpty();
    }


}
