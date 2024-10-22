package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ShippingAddressesDAO;
import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
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

@WebServlet(name = "RegisterUser", urlPatterns = "/user-register")
public class RegisterUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(checkUser(request, response)) return;

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
            request.getRequestDispatcher("/WEB-INF/results/user-register.jsp").forward(request, response);
            return;
        }

        // Validazione campi DA VEDERE DATA

        DataValidator validator = new DataValidator();
        try {
            validator.validatePattern(name.get().trim(), DataValidator.PatternType.Username);
            validator.validatePattern(surname.get().trim(), DataValidator.PatternType.Surname);
            validator.validatePattern(phone.get().trim(), DataValidator.PatternType.Phone);
            validator.validatePattern(email.get().trim(), DataValidator.PatternType.Email);
            validator.validatePattern(birthdate.get(), DataValidator.PatternType.Birthdate);
            validator.validatePattern(roadType.get().trim(), DataValidator.PatternType.Generic);
            validator.validatePattern(roadName.get().trim(), DataValidator.PatternType.Generic);
            validator.validatePattern(roadNumber.get().trim(), DataValidator.PatternType.GenericAlphaNumeric);
            validator.validatePattern(city.get().trim(), DataValidator.PatternType.Generic);
            validator.validatePattern(province.get().trim(), DataValidator.PatternType.Generic, 2, 2);
            validator.validatePattern(cap.get(), DataValidator.PatternType.CAP);
            validator.validatePattern(password.get(), DataValidator.PatternType.Password);
        } catch (Exception e) {
            //Gestione errore di validazione campi
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("/WEB-INF/results/register.jsp").forward(request, response);
            return;
        }


        UserBean userBean = new UserBean();
        UserDAO userDAO = new UserDAO();

        int id_cred = 0;
        try {
            id_cred = userDAO.doSaveCredentials(password.get());
            userBean.setId_cred(id_cred);
            userBean.setFirstname(name.get().trim());
            userBean.setLastname(surname.get().trim());
            userBean.setTelephone(phone.get().trim());
            userBean.setEmail(email.get().trim());
            userBean.setBirth(LocalDate.parse(birthdate.get()));
            userBean.setAddress(roadType.get().trim() + ", " + roadName.get().trim() + ", " + roadNumber.get().trim());
            userBean.setCity(city.get().trim());
            userBean.setProv(province.get());
            userBean.setCAP(cap.get());
            userDAO.doSave(userBean);

            ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();
            shippingAddressesDAO.doSaveFirstShippingAddresses(userBean);

            HttpSession session = request.getSession();

            session.setAttribute("userlogged", userBean);
            request.getRequestDispatcher("/index").forward(request, response);
        } catch (InvalidParameterException e){

            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.setAttribute("invalidData", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/results/user-register.jsp").forward(request, response);

        } catch (Exception e) {
            try {
                userDAO.doDeleteCredentials(id_cred);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            request.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(request, response);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(checkUser(req, resp)) return;
        req.getRequestDispatcher("WEB-INF/results/register.jsp").forward(req, resp);
    }

    private boolean checkUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        if (request.getSession().getAttribute("userlogged") != null){
            response.sendRedirect(request.getContextPath()+"/index");//Index
            return true;
        }
        return false;
    }
}