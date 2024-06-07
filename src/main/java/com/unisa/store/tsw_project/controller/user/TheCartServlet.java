package com.unisa.store.tsw_project.controller.user;

import com.google.gson.Gson;
import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.CartBean;
import com.unisa.store.tsw_project.model.beans.CartItemsBean;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

@WebServlet(name = "CartServlet", urlPatterns = "/cart")
public class TheCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPostCart(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> requestType = Optional.ofNullable(req.getHeader("X-Requested-With"));
        //No AJAX request: respond with normal post
        if(requestType.isEmpty()){
            doPostCart(req, resp);

        //If X-Requested-With has no valid option
        } else if(!requestType.get().equalsIgnoreCase("XMLHttpRequest")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        //If valid AJAX is sent
        } else {
            try {
                answerToAjax(req, resp);
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }


    /* --- PRIVATE METHODS --- */

    private void doPostCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/results/cart.jsp").forward(req, resp);
    }

    /**
     * Answer to AJAX Request to Cart Operations <br />
     * Operations: <br /><ul>
     * <li>addToCart: add item to cart: required id_prod, id_condition</li>
     * <li>removeFromCart: remove item from cart if exist: required id_prod, id_condition</li>
     * <li>Others TODO </li>
     * </ul>
     * @param req to get parameters required
     * @param resp to set answer / error code or messages
     * @throws SQLException if data query fails
     * @throws IOException if IO fails (i.e. sendError)
     * @throws com.unisa.store.tsw_project.other.exceptions.InvalidParameterException if some required parameter is not valid
     */
    private void answerToAjax(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        Optional<String> option = Optional.ofNullable(req.getParameter("option"));
        if(option.isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No option set");
        } else {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            switch (option.get()){
                case "addToCart" -> addToCart(req, resp);
                case "removeFromCart" -> removeFromCart(req,resp);
                case "requestNewPrice" -> sendNewPrice(req,resp);
                case "retrieveProduct" -> retrieveProduct(req,resp);
                default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid option");
            }
        }
    }

    /**
     * Add Item to Cart or Update its Quantity. Private method only AJAX
     * @param req to get parameters from
     * @param resp to set OK Answer or Error code + message
     * @throws IOException if IO fails (i.e. sendError)
     * @throws SQLException if query to retrieve product data fails
     */
    synchronized private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        HttpSession session = req.getSession();
        DataValidator validator = new DataValidator();
        ProductDAO productDAO = new ProductDAO();

        try {
            //Retrieve Product ID from Request and validate it
            Optional<String> id = Optional.ofNullable(req.getParameter("id_prod"));
            Optional<String> id_condition = Optional.ofNullable(req.getParameter("condition"));
            if (id.isEmpty() || id_condition.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No id or condition set");
                return;
            }
            validator.validatePattern(id.get(), DataValidator.PatternType.Int, 1, null);
            validator.validatePattern(id_condition.get(), DataValidator.PatternType.Int, 0, 5);

            Data.Condition condition_requested = Data.Condition.getEnum(Integer.parseInt(id_condition.get()));
            if(condition_requested == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Condition");
                return;
            }

            // VALIDATION: Check if product exists and retrieve it
            ProductBean productBean = productDAO.doRetrieveById(Integer.parseInt(id.get()));
            if (productBean == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product not found");
                return;
            }


            //Check if user is logged: Set id_client to user id if logged, null if not
            Optional<UserBean> user = Optional.ofNullable((UserBean) session.getAttribute("userlogged"));
            Integer id_client = user.isPresent() ? user.get().getId_cred() : null;

            //Check for an existing Cart in Session
            Optional<Object> cartObj = Optional.ofNullable(session.getAttribute("cart"));

            //If Cart doesn't exist Create it
            if (cartObj.isEmpty()) {
                CartBean cartBean = new CartBean();

                cartBean.setId_cart(null); //Null because we don't have a valid ID from the Database Yet.
                cartBean.setId_client(id_client); //int ID if logged - null if not
                cartBean.setCartItems(new LinkedHashMap<>()); //Initiate HashMap ( Key: id_prod (Integer), value: CartItem )
                cartBean.setActive(true); //This Flag for Current Active Cart
                session.setAttribute("cart", cartBean);
            }

            //Cart here is always not null
            CartBean cart = (CartBean) session.getAttribute("cart");

            // Get CartItem from Cart or Create a New One
            CartItemsBean item = Optional.ofNullable(cart.getCartItems().get(productBean.getId_prod() + condition_requested.toString() )).orElse(new CartItemsBean());


            //VALIDATION: Check for Quantity only on Physical Products! getType: false=physical, true=digital
            if(!productBean.getType()) {
                //VALIDATION: Check if product Condition exists and retrieve it:
                // if <= 0 NO products are left: return error
                // if there are some products but user request more than available return error
                int quantityConditionLeft = productBean.getConditions().stream()
                        .filter(condBean -> condBean.getCondition().equals(condition_requested)).findFirst()
                        .orElseThrow(() -> new InvalidParameterException("Invalid Condition")).getQuantity();
                if (quantityConditionLeft <= 0) {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "This specific product is unavailable");
                    return;
                } else if(quantityConditionLeft < item.getQuantity() + 1){
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unable to add more quantity to cart");
                    return;
                }
            }

            //Set ItemAttributes
            item.setId_prod(productBean.getId_prod());
            item.addQuantity();
            item.setCondition(condition_requested);


            BigDecimal originalPrice = productBean.getPrice().multiply(BigDecimal.valueOf(1 - (double) condition_requested.discount / 100));
            item.setReal_price(originalPrice);

            //Set CartItem to Cart
            cart.setAttribute(item);

            //Send Answer the quantity of Item inside the Cart
            Gson gson = new Gson();
            String json = gson.toJson(item.getQuantity());
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(json);
            resp.getWriter().flush();

            //resp.sendError(HttpServletResponse.SC_OK, "Item correctly added to Cart");

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Generic invalid Parameter: 1");
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Generic invalid Parameter: 2");
        } catch (InvalidParameterException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }


    /**
     * Remove an Item in Session Cart with his (String) key = id_prod + condition
     * @param req to get key from
     * @param resp to send response status / message
     * @throws IOException if response writing fails
     */
    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<String> key = Optional.ofNullable(req.getParameter("key"));
        DataValidator validator = new DataValidator();

        if(key.isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No key prod set");
            return;
        }
        validator.validatePattern(key.get(), DataValidator.PatternType.GenericAlphaNumeric);

        CartBean cart = (CartBean) req.getSession().getAttribute("cart");
        if(cart == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Cart is Empty");
            return;
        }

        CartItemsBean item = cart.getCartItems().remove(key.get());
        if(item == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Item not found in Cart");
            return;
        }

        resp.setContentType("text/plain");
        resp.getWriter().write("Prodotto rimosso correttamente dal carrello...");
        resp.getWriter().flush();
        //resp.sendError(HttpServletResponse.SC_OK, "OK");
    }


    private void sendNewPrice(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        CartBean cart = (CartBean) req.getSession().getAttribute("cart");
        BigDecimal[] prices = new BigDecimal[]{BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)};
        BigDecimal value = new BigDecimal("0.00");
        if(cart != null && !cart.getCartItems().isEmpty()) {
            for(CartItemsBean cartItem : cart.getCartItems().values()) {
                value = value.add(cartItem.getReal_price().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }

            prices[0] = value;
            if(prices[0].compareTo(new BigDecimal(100)) < 0) {
                prices[1] = value.add(new BigDecimal("15.00"));
            } else prices[1] = value;
        }

        Gson gson = new Gson();
        String json = gson.toJson(prices);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
        resp.getWriter().flush();
    }


    private void retrieveProduct(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        Optional<String> id = Optional.ofNullable(req.getParameter("id"));
        DataValidator validator = new DataValidator();

        if(id.isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No id prod set");
            return;
        }
        validator.validatePattern(id.get(), DataValidator.PatternType.Int, 1, null);

        ProductDAO productDAO = new ProductDAO();
        ProductBean prod = productDAO.doRetrieveById(Integer.parseInt(id.get()));
        if(prod == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id prod");
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(prod);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
        resp.getWriter().flush();
    }
}