package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ShippingAddressesDAO;
import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.ShippingAddressesBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
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
                case "two":
                    //todo
                    break;
                case "three":
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
            request.getRequestDispatcher("user-register.jsp").forward(request, response);
            return;
        }

        // Validazione campi DA VEDERE DATA

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
            userBean.setAddress(roadType.get() + " " + roadName.get() + " " + roadNumber.get());
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

    private void updatePassword (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Optional<String> oldpass = Optional.ofNullable(request.getParameter("oldpass"));
        Optional<String> newpass = Optional.ofNullable(request.getParameter("newpass"));

        if (oldpass.isEmpty() || newpass.isEmpty()) {
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("user-register.jsp").forward(request, response);
            return;
        }

        DataValidator validator = new DataValidator();
        validator.validatePattern(oldpass.get(), DataValidator.PatternType.Password);
        validator.validatePattern(newpass.get(), DataValidator.PatternType.Password);

        UserDAO userDAO = new UserDAO();
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");

        if(userDAO.checkPassword(oldpass.get(), userBean.getId_cred())){
            userDAO.doUpdatePassword(newpass.get(), userBean.getId_cred());
            request.getRequestDispatcher("/index").forward(request, response);
            return;
        } else {
            //TODO password errata
            request.getRequestDispatcher("/index").forward(request, response);
            return;
        }
    }

    private void addShippingAddresses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        Optional<String> roadType = Optional.ofNullable(request.getParameter("road-type2"));
        Optional<String> roadName = Optional.ofNullable(request.getParameter("road-name2"));
        Optional<String> roadNumber = Optional.ofNullable(request.getParameter("road-number2"));
        Optional<String> city = Optional.ofNullable(request.getParameter("city2"));
        Optional<String> province = Optional.ofNullable(request.getParameter("prov2"));
        Optional<String> cap = Optional.ofNullable(request.getParameter("cap2"));

        if (roadType.isEmpty() || roadName.isEmpty() || roadNumber.isEmpty() || city.isEmpty() || province.isEmpty() || cap.isEmpty()) {
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("user-register.jsp").forward(request, response);
            return;
        }

        DataValidator validator = new DataValidator();

        validator.validatePattern(roadType.get(), DataValidator.PatternType.Generic);
        validator.validatePattern(roadName.get(), DataValidator.PatternType.Generic);
        validator.validatePattern(roadNumber.get(), DataValidator.PatternType.GenericAlphaNumeric);
        validator.validatePattern(city.get(), DataValidator.PatternType.Generic);
        validator.validatePattern(province.get(), DataValidator.PatternType.Generic);
        validator.validatePattern(cap.get(), DataValidator.PatternType.CAP);

        ShippingAddressesBean shippingAddressesBean = new ShippingAddressesBean();
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");
        ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();

        shippingAddressesBean.setFirstname(userBean.getFirstname());
        shippingAddressesBean.setLastname(userBean.getLastname());
        shippingAddressesBean.setAddress(roadType.get() + " " + roadName.get() + " " + roadNumber.get());
        shippingAddressesBean.setCity(city.get());
        shippingAddressesBean.setProv(province.get());
        shippingAddressesBean.setCAP(cap.get());
        shippingAddressesBean.setId_client(userBean.getId());
        shippingAddressesDAO.doSave(shippingAddressesBean);
        request.getRequestDispatcher("/jsp/user-profile.jsp").forward(request, response);
    }

    private void deleteShippingAddresses(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {

        Optional<String> id = Optional.ofNullable(request.getParameter("id_add"));

        if (id.isEmpty()) {
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("user-register.jsp").forward(request, response);
            return;
        }

        ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();
        shippingAddressesDAO.doDelete(Integer.parseInt(id.get()));
    }

}
