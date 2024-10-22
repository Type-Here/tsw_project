package com.unisa.store.tsw_project.controller.user;

import com.google.gson.Gson;
import com.unisa.store.tsw_project.model.DAO.OrdersDAO;
import com.unisa.store.tsw_project.model.DAO.ShippingAddressesDAO;
import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.OrdersBean;
import com.unisa.store.tsw_project.model.beans.ShippingAddressesBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "ModifyUser", urlPatterns = "/modify-user")
public class ModifyUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("userlogged") == null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }

        try {
            switch (request.getParameter("section")) {
                case "one":
                    updateUser(request, response);
                    break;
                case "buttonDelete":
                    checkDeleteButton(request, response);
                    break;
                case "updatePassword":
                    updatePassword(request, response);
                    break;
                case "four":
                    addShippingAddresses(request, response);
                    break;
                case "delete":
                    deleteShippingAddresses(request, response);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void updateUser (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Recupero parametri
        Optional<String> name = Optional.ofNullable(request.getParameter("name"));
        Optional<String> surname = Optional.ofNullable(request.getParameter("surname"));
        Optional<String> phone = Optional.ofNullable(request.getParameter("phone"));
        Optional<String> email = Optional.ofNullable(request.getParameter("email"));
        Optional<String> birthdate = Optional.ofNullable(request.getParameter("birthdate"));
        Optional<String> roadType = Optional.ofNullable(request.getParameter("road-type"));
        Optional<String> roadName = Optional.ofNullable(request.getParameter("road-name"));
        Optional<String> roadNumber = Optional.ofNullable(request.getParameter("road-number"));
        Optional<String> city = Optional.ofNullable(request.getParameter("city"));
        Optional<String> province = Optional.ofNullable(request.getParameter("province"));
        Optional<String> cap = Optional.ofNullable(request.getParameter("cap"));

        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || email.isEmpty() || birthdate.isEmpty() || roadType.isEmpty() || roadName.isEmpty() || roadNumber.isEmpty() || city.isEmpty() || province.isEmpty() || cap.isEmpty()) {
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("/jsp/user-register.jsp").forward(request, response);
            return;
        }

        // Validazione campi DA VEDERE DATA
        try {
            DataValidator validator = new DataValidator();
            validator.validatePattern(name.get(), DataValidator.PatternType.Username);
            validator.validatePattern(surname.get(), DataValidator.PatternType.Surname);
            validator.validatePattern(phone.get(), DataValidator.PatternType.Phone);
            validator.validatePattern(email.get(), DataValidator.PatternType.Email);
            validator.validatePattern(birthdate.get(), DataValidator.PatternType.Birthdate);
            validator.validatePattern(roadType.get(), DataValidator.PatternType.Generic);
            validator.validatePattern(roadName.get(), DataValidator.PatternType.Generic);
            validator.validatePattern(roadNumber.get(), DataValidator.PatternType.GenericAlphaNumeric);
            validator.validatePattern(city.get(), DataValidator.PatternType.Generic);
            validator.validatePattern(province.get(), DataValidator.PatternType.Generic);
            validator.validatePattern(cap.get(), DataValidator.PatternType.CAP);
        } catch (Exception e) {
            request.setAttribute("invalidUserUpdate", true);
            request.getRequestDispatcher("/WEB-INF/results/user-profile.jsp").forward(request, response);
            return;
        }


        UserBean userBean = new UserBean();
        UserDAO userDAO = new UserDAO();

        try {
            UserBean user = (UserBean) request.getSession().getAttribute("userlogged");
            userBean.setId_cred(user.getId_cred());
            userBean.setFirstname(name.get());
            userBean.setLastname(surname.get());
            userBean.setTelephone(phone.get());
            userBean.setEmail(email.get());
            userBean.setBirth(LocalDate.parse(birthdate.get()));
            userBean.setAddress(roadType.get() + ", " + roadName.get() + ", " + roadNumber.get());
            userBean.setCity(city.get());
            userBean.setProv(province.get());
            userBean.setCAP(cap.get());
            userBean.setId(user.getId());
            userDAO.doUpdate(userBean);
            request.getSession().setAttribute("userlogged", userBean);
            request.getRequestDispatcher("/index").forward(request, response);
        } catch (SQLException e) {
            throw new RuntimeException(e); /*TODO*/
        }
    }

    private void checkDeleteButton(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int id_add = Integer.parseInt(request.getParameter("id_add"));
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");
        OrdersDAO ordersDAO = new OrdersDAO();
        List<OrdersBean> ordersBeanList = new ArrayList<>();

        try {
            ordersBeanList = ordersDAO.doRetrieveAllOrdersByUserId(userBean.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Boolean notDelete = false;

        //Check if the address is used in an order
        for (OrdersBean ordersBean : ordersBeanList) {
            if (ordersBean.getId_add() == id_add) {
                notDelete = true;
                break;
            }
        }

        String json = new Gson().toJson(notDelete);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);

    }

    private void updatePassword (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {

        Optional<String> oldpass = Optional.ofNullable(request.getParameter("passwordOld"));
        Optional<String> newpass = Optional.ofNullable(request.getParameter("passwordNew"));

        if (oldpass.isEmpty() || newpass.isEmpty()) {
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("/index").forward(request, response);
            return;
        }

        // Validazione campi
        DataValidator validator = new DataValidator();
        try {
            validator.validatePattern(oldpass.get(), DataValidator.PatternType.Password);
            validator.validatePattern(newpass.get(), DataValidator.PatternType.Password);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Set status code to 500 (Internal Server Error)
            response.getWriter().write("Nuova Password non valida");
            return;
        }

        // Controllo password corrente ed esecuzione cambio password
        UserDAO userDAO = new UserDAO();
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");

        if(userDAO.checkPassword(oldpass.get(), userBean.getId_cred())){
            if (oldpass.get().equals(newpass.get())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Set status code to 500 (Internal Server Error)
                response.getWriter().write("La nuova password non può essere uguale a quella vecchia");
                return;
            }

            userDAO.doUpdatePassword(newpass.get(), userBean.getId_cred());
            
            LoginUserServlet.setUserCookies(request, response, userBean);

            response.setStatus(HttpServletResponse.SC_OK);// Write success message set status code to 200 (OK)
            response.getWriter().write("Password cambiata con successo");
            return;
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // Set status code to 500 (Internal Server Error)
            response.getWriter().write("Password corrente errata riprovare");
            return;
        }
    }


    /**
     * Add A New Shipping Address for the Current User. <br />
     * Validate Data and save new address if valid. <br />
     * Manage Both Http Form Posts and AJAX Requests
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws ServletException if forward fails
     * @throws IOException if write/forward fails
     */
    synchronized private void addShippingAddresses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Optional<String> roadType = Optional.ofNullable(request.getParameter("road-type2"));
        Optional<String> roadName = Optional.ofNullable(request.getParameter("road-name2"));
        Optional<String> roadNumber = Optional.ofNullable(request.getParameter("road-number2"));
        Optional<String> city = Optional.ofNullable(request.getParameter("city2"));
        Optional<String> province = Optional.ofNullable(request.getParameter("prov2"));
        Optional<String> cap = Optional.ofNullable(request.getParameter("cap2"));

        try{
            if (roadType.isEmpty() || roadName.isEmpty() || roadNumber.isEmpty() || city.isEmpty() || province.isEmpty() || cap.isEmpty()) {
                throw new InvalidParameterException("All fields are required");
            }

            //Fields Validation
            DataValidator validator = new DataValidator();

            //Throws InvalidParameterException if fails validation
            validator.validatePattern(roadType.get(), DataValidator.PatternType.Generic);
            validator.validatePattern(roadName.get(), DataValidator.PatternType.Generic);
            validator.validatePattern(roadNumber.get(), DataValidator.PatternType.Int, 1,999999);
            validator.validatePattern(city.get(), DataValidator.PatternType.Generic);
            validator.validatePattern(province.get(), DataValidator.PatternType.Generic, 2, 2);
            validator.validatePattern(cap.get(), DataValidator.PatternType.CAP, 5, 6);


            // Saving new shipping address
            ShippingAddressesBean shippingAddressesBean = new ShippingAddressesBean();
            UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");

            ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();

            // Limit Number of Shipping Addresses per User to 6
            List<ShippingAddressesBean> shippingAddressesBeanList = shippingAddressesDAO.doRetrieveAllByUserId(userBean.getId());
            if(shippingAddressesBeanList.size() >= 6){
                throw new InvalidParameterException("Maximum number of shipping addresses is 6");
            }

            shippingAddressesBean.setFirstname(userBean.getFirstname());
            shippingAddressesBean.setLastname(userBean.getLastname());
            shippingAddressesBean.setAddress(roadType.get().trim() + ", " + roadName.get().trim() + ", " + roadNumber.get().trim());
            shippingAddressesBean.setCity(city.get().trim());
            shippingAddressesBean.setProv(province.get().trim());
            shippingAddressesBean.setCAP(cap.get().trim());
            shippingAddressesBean.setId_client(userBean.getId());

            //Save New Address
            shippingAddressesDAO.doSave(shippingAddressesBean);

            Optional<String> header = Optional.ofNullable(request.getHeader("X-Requested-With"));

            //Normal request
            if (header.isEmpty() || !header.get().equalsIgnoreCase("XMLHttpRequest")) {
                request.getRequestDispatcher("/WEB-INF/results/user-profile.jsp").forward(request,response);

            //AJAX
            } else {
                Gson gson = new Gson();
                String json = gson.toJson(shippingAddressesBean);

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
                response.getWriter().flush();
            }

        } catch(SQLException | InvalidParameterException e) {

            Optional<String> header = Optional.ofNullable(request.getHeader("X-Requested-With"));
            if (header.isEmpty() || !header.get().equalsIgnoreCase("XMLHttpRequest")) {
                request.setAttribute("invalidAddresses", true);
                request.getRequestDispatcher("/WEB-INF/results/user-profile.jsp").forward(request, response);

            //AJAX
            } else {
                response.setContentType("text/plain");
                response.getWriter().write("OPS... Something went wrong");
                response.setStatus(Data.SC_INVALID_DATA);
                response.getWriter().flush();
            }
        }
    }

    synchronized private void deleteShippingAddresses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        Optional<String> id = Optional.ofNullable(request.getParameter("id_add"));

        if (id.isEmpty()) {
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("/jsp/user-register.jsp").forward(request, response);
            return;
        }

        ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();
        shippingAddressesDAO.doDelete(Integer.parseInt(id.get()));
    }

}
