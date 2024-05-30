package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.CartBean;
import com.unisa.store.tsw_project.model.beans.CartItemsBean;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
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
     * @throws ServletException if response fails
     * @throws IOException if IO fails (i.e. sendError)
     * @throws com.unisa.store.tsw_project.other.exceptions.InvalidParameterException if some required parameter is not valid
     */
    private void answerToAjax(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        Optional<String> option = Optional.ofNullable(req.getParameter("option"));
        if(option.isEmpty()){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No option set");
        } else {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");

            switch (option.get()){
                case "addToCart":
                    addToCart(req, resp);
                    System.out.println("Qui addCart");
                    break;
                case "removeFromCart":
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid option");
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
    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
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

            //Check if product exists and retrieve it
            ProductBean p = productDAO.doRetrieveById(Integer.parseInt(id.get()));
            if (p == null) {
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

                session.setAttribute("cart", cartBean);
            }

            //Cart here is always not null
            CartBean cart = (CartBean) session.getAttribute("cart");

            // Get CartItem from Cart or Create a New One
            CartItemsBean item = Optional.ofNullable(cart.getCartItems().get(p.getId_prod())).orElse(new CartItemsBean());

            //Set ItemAttributes
            item.setId_prod(p.getId_prod());
            item.addQuantity();
            Data.Condition c = Data.Condition.getEnum(Integer.parseInt(id_condition.get()));

            if(c == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Condition");
                return;
            }

            BigDecimal originalPrice = p.getPrice().multiply(BigDecimal.valueOf(1 - (double) c.discount / 100));
            item.setReal_price(originalPrice);

            //Set CartItem to Cart
            cart.setAttribute(item);

            //Send Answer OK
            resp.sendError(HttpServletResponse.SC_OK, "Item correctly added to Cart");

        } catch (NumberFormatException | NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Generic invalid Parameter");
        }

    }


}