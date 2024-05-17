package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ShippingAddressesDAO;
import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Random;

@WebServlet(name = "RegisterUser", urlPatterns = "/user-register")
public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("user-logged") != null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }

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

        Optional<String> password = Optional.ofNullable(request.getParameter("password"));

        if (name.isEmpty() || surname.isEmpty() || phone.isEmpty() || email.isEmpty() || birthdate.isEmpty() || roadType.isEmpty() || roadName.isEmpty() || roadNumber.isEmpty() || city.isEmpty() || province.isEmpty() || cap.isEmpty() || password.isEmpty()) {
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
        validator.validatePattern(password.get(), DataValidator.PatternType.Password);




        UserBean userBean = new UserBean();
        UserDAO userDAO = new UserDAO();

        int id_cred = 0;
        try {
            id_cred = userDAO.doSaveCredentials(password.get(), generateSalt());
            userBean.setId_cred(id_cred);
            userBean.setFirstname(name.get());
            userBean.setLastname(surname.get());
            userBean.setTelephone("+39 " + phone.get());
            userBean.setEmail(email.get());
            userBean.setBirth(LocalDate.parse(birthdate.get()));
            userBean.setAddress(roadType.get() + " " + roadName.get() + " " + roadNumber.get());
            userBean.setCity(city.get());
            userBean.setProv(province.get());
            userBean.setCAP(cap.get());
            userDAO.doSave(userBean);

            ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();
            shippingAddressesDAO.doSaveFirstShippingAddresses(userBean);

            HttpSession session = request.getSession();

            session.setAttribute("user-logged", userBean);
            request.getRequestDispatcher("/index").forward(request, response);
        } catch (Exception e) {
            try {
                userDAO.doDeleteCredentials(id_cred);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            request.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(request, response);
        }

    }

    private String generateSalt(){
        int length = 6; // Lunghezza della stringa casuale
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Caratteri da cui scegliere

        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = rnd.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }


}
