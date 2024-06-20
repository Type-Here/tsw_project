package com.unisa.store.tsw_project.controller.user;

import com.google.gson.Gson;
import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.CartBean;
import com.unisa.store.tsw_project.model.beans.CartItemsBean;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.BadRequestException;
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
        doPostCart(req, resp); //  -- Load Cart Page
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> requestType = Optional.ofNullable(req.getHeader("X-Requested-With"));
        //No AJAX request: respond with normal post -- Load Cart Page
        if(requestType.isEmpty()) {
            doPostCart(req, resp);
            return;
        }

        try {
            //If X-Requested-With has no valid option
            if(!requestType.get().equalsIgnoreCase("XMLHttpRequest")) {
                throw new BadRequestException("Only XMLHttpRequest is supported");
            }

            //If valid AJAX is sent
            answerToAjax(req, resp);

        } catch (SQLException e){
            resp.getWriter().println("Something went wrong");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        } catch (InvalidParameterException e) {
            resp.getWriter().println(e.getMessage());
            resp.setStatus(Data.SC_INVALID_DATA);

        } catch (BadRequestException e){
            resp.getWriter().println(e.getMessage());
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        } finally {
            resp.getWriter().flush();
        }
    }


    /* --- PRIVATE METHODS --- */

    private void doPostCart(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/results/cart.jsp").forward(req, resp);
    }

    /**
     * Answer to AJAX Request to Cart Operations <br />
     * Operations: <br /><ul>
     * <li>addToCart: add item to cart / update its quantity (no remove): required id_prod, id_condition</li>
     * <li>removeFromCart: remove item from cart if exist: required id_prod, id_condition</li>
     * <li>Others TODO </li>
     * </ul>
     * @param req to get parameters required
     * @param resp to set answer / error code or messages
     * @throws SQLException if data query fails
     * @throws IOException if IO fails (i.e. sendError)
     * @throws com.unisa.store.tsw_project.other.exceptions.InvalidParameterException if some required parameter is not valid
     */
    synchronized private void answerToAjax(HttpServletRequest req, HttpServletResponse resp) throws BadRequestException, SQLException, IOException {
        Optional<String> option = Optional.ofNullable(req.getParameter("option"));
        if(option.isEmpty()){
            throw new BadRequestException("No option set");
        } else {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            switch (option.get()){
                case "addToCart" -> addToCart(req, resp);
                case "removeFromCart" -> removeFromCart(req,resp);
                case "requestNewPrice" -> sendNewPrice(req,resp);
                case "retrieveProduct" -> retrieveProduct(req,resp);
                case "addDiscountCode" -> addDiscountCode(req,resp);
                default -> throw new BadRequestException("Invalid option");
            }
        }
    }

    /**
     * Add Item to Cart or Update its Quantity. Private method only AJAX <br />
     * Send Optional Parameter quantity to update to a specific quantity.
     * If no 'quantity' request param is available update to current Item + 1 (only physical products)
     * @param req to get parameters from
     * @param resp to set OK Answer or Error code + message
     * @throws IOException if IO fails (i.e. sendError)
     * @throws SQLException if query to retrieve product data fails
     */
    synchronized private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws BadRequestException, IOException, SQLException {
        DataValidator validator = new DataValidator();
        ProductDAO productDAO = new ProductDAO();

        try {
            //Retrieve Product ID from Request and validate it
            Optional<String> id = Optional.ofNullable(req.getParameter("id_prod"));
            Optional<String> id_condition = Optional.ofNullable(req.getParameter("condition"));
            //Can be Null - Used only to Add More Products on Select Input!
            Optional<String> quantity = Optional.ofNullable(req.getParameter("quantity"));

            if (id.isEmpty() || id_condition.isEmpty()) {
                throw new BadRequestException("No id or condition set");
            }

            //NB To remove item use Remove Request instead
            quantity.ifPresent(s -> validator.validatePattern(s, DataValidator.PatternType.Int, 1, null)); //Check only if Present - No values less than 1!
            validator.validatePattern(id.get(), DataValidator.PatternType.Int, 1, null); //Required Always Check
            validator.validatePattern(id_condition.get(), DataValidator.PatternType.Int, 0, 5); //Required

            Data.Condition condition_requested = Data.Condition.getEnum(Integer.parseInt(id_condition.get()));
            if(condition_requested == null) {
                throw new InvalidParameterException("Invalid Condition");
            }

            // VALIDATION: Check if product exists and retrieve it
            ProductBean productBean = productDAO.doRetrieveById(Integer.parseInt(id.get()));
            if (productBean == null) {
                throw new InvalidParameterException("Product not found");
            }

            /* Obtain Cart: getCart Method Below */
            //Cart here is always not null
            CartBean cart = getCart(req);

            // Get CartItem from Cart or Create a New One
            CartItemsBean item = Optional.ofNullable(cart.getCartItems().get(productBean.getId_prod() + condition_requested.toString() )).orElse(new CartItemsBean());

            // Set Quantity to Update to Quantity Requested or Current Cart + 1 based on Request
            int quantityRequested = quantity.map(Integer::parseInt).orElseGet(() -> item.getQuantity() + 1);

            //VALIDATION: Check for Quantity only on Physical Products! getType: false=physical, true=digital
            if(!productBean.getType()) {
                //VALIDATION: Check if product Condition exists and retrieve it:
                // if <= 0 NO products are left: return error
                // if there are some products but user request more than available return error
                int quantityConditionLeft = productBean.getConditions().stream()
                        .filter(condBean -> condBean.getCondition().equals(condition_requested)).findFirst()
                        .orElseThrow(() -> new InvalidParameterException("Invalid Condition")).getQuantity();

                if (quantityConditionLeft <= 0) {
                    throw new InvalidParameterException( "This specific product is unavailable");
                } else if(quantityConditionLeft < quantityRequested){
                    throw new InvalidParameterException("Unable to add more quantity to cart");
                }
            }

            //Set ItemAttributes
            item.setId_prod(productBean.getId_prod());
            item.setQuantity(quantityRequested);
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
           throw new InvalidParameterException("Generic invalid Parameter: Not a Number");
        } catch (NullPointerException e) {
            throw new InvalidParameterException("Generic invalid Parameter: Null");
        }
    }


    /**
     * Remove an Item in Session Cart with his (String) key = id_prod + condition
     * @param req to get key from
     * @param resp to send response status / message
     * @throws IOException if response writing fails
     */
    synchronized private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Optional<String> key = Optional.ofNullable(req.getParameter("key"));
        DataValidator validator = new DataValidator();

        if(key.isEmpty()){
            throw new InvalidParameterException("No key prod set");
        }
        validator.validatePattern(key.get(), DataValidator.PatternType.GenericAlphaNumeric);

        CartBean cart = (CartBean) req.getSession().getAttribute("cart");
        if(cart == null) {
            throw new InvalidParameterException("Cart is Empty");
        }

        CartItemsBean item = cart.getCartItems().remove(key.get());
        if(item == null) {
            throw new InvalidParameterException("Item not found in Cart");
        }

        resp.setContentType("text/plain");
        resp.getWriter().write("Prodotto rimosso correttamente dal carrello...");
        resp.getWriter().flush();
    }


    /**
     * Send Updated Price for Products in Cart <br />
     * Send JSON
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException  if response writing fails
     */
    private void sendNewPrice(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        CartBean cart = (CartBean) req.getSession().getAttribute("cart");
        BigDecimal[] prices = new BigDecimal[]{BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)};
        BigDecimal value = new BigDecimal("0.00");
        if(cart != null && !cart.getCartItems().isEmpty()) {
            for(CartItemsBean cartItem : cart.getCartItems().values()) {
                value = value.add(cartItem.getReal_price().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            }

            prices[0] = value;
            if(prices[0].compareTo(new BigDecimal(100)) < 0) {
                prices[1] = value.add(new BigDecimal("15.00")); //Shipping Costs
            } else prices[1] = value;
        }

        Gson gson = new Gson();
        String json = gson.toJson(prices);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
        resp.getWriter().flush();
    }


    /**
     * Send ProductBean Data to Print <br />
     * Send JSON
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws SQLException if query to database fails
     * @throws IOException if json response writing fails
     */
    private void retrieveProduct(HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException, BadRequestException {
        Optional<String> id = Optional.ofNullable(req.getParameter("id"));
        DataValidator validator = new DataValidator();

        if(id.isEmpty()){
            throw new BadRequestException("No id prod set");
        }
        validator.validatePattern(id.get(), DataValidator.PatternType.Int, 1, null);

        ProductDAO productDAO = new ProductDAO();
        ProductBean prod = productDAO.doRetrieveById(Integer.parseInt(id.get()));
        if(prod == null) {
            throw new BadRequestException("Invalid id prod");
        }

        Gson gson = new Gson();
        String json = gson.toJson(prod);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
        resp.getWriter().flush();
    }

    /**
     * AJAX <br />
     * Add A Discount Code to Cart if Possible <br />
     * - Discount Code must be Valid <br />
     * - A Discount COde must not be Already Set <br />
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if response writing fails
     */
    synchronized private void addDiscountCode(HttpServletRequest req, HttpServletResponse resp) throws IOException, BadRequestException {
        Optional<String> key = Optional.ofNullable(req.getParameter("key"));
        DataValidator validator = new DataValidator();

        CartBean cart = getCart(req); //Get Cart
        if(cart.getDiscount_code() != null){
            throw new BadRequestException("Hai già un Codice Sconto Attivo");
        }

        if(key.isEmpty()){
            throw new InvalidParameterException("Discount code is Empty");
        }
        String code = key.get().trim();
        validator.validatePattern(code, DataValidator.PatternType.DiscountName);

        Map<String, Double> discountAvailable = (Map<String, Double>) getServletContext().getAttribute("discountCode");
        if(discountAvailable == null || !discountAvailable.containsKey(code)) {
            throw new InvalidParameterException("Discount code not found");
        }

        cart.setDiscount_code(code);

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("Codice Aggiunto Correttamente!");
    }



    /* ================================= GENERATE OR RETRIEVE CART =================================================== */

    /**
     * Function to Get Cart from Current Session or Create a New One if there isn't
     * @param req HttpServletRequest
     * @return CartBean with current Session Cart
     */
    private CartBean getCart(HttpServletRequest req) {
        HttpSession session = req.getSession();

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
        return (CartBean) session.getAttribute("cart");
    }
}