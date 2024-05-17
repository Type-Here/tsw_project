package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.OrdersDAO;
import com.unisa.store.tsw_project.model.DAO.ShippingAddressesDAO;
import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.OrdersBean;
import com.unisa.store.tsw_project.model.beans.ShippingAddressesBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "LoginUser", urlPatterns = "/user-login")
public class LoginUserServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("user-logged") != null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }

        Optional<String> email = Optional.ofNullable(request.getParameter("email"));
        Optional<String> password = Optional.ofNullable(request.getParameter("password"));

        if(password.isEmpty() || email.isEmpty()){
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        // Validazione campi
        DataValidator validator = new DataValidator();
        validator.validatePattern(email.get(), DataValidator.PatternType.Email);
        validator.validatePattern(password.get(), DataValidator.PatternType.Password);

        UserBean userBean = new UserBean();
        UserDAO userDAO = new UserDAO();

        try {
            userBean = userDAO.getUserByEmail(email.get());
            if (userBean == null){
                request.getSession().invalidate();
                request.setAttribute("invalidUser", true);
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                return;
            }
            String[] hashSalt = userDAO.doRetrieveHashAndSaltByUserId(userBean.getId_cred());
            if(checkPassword(password.get(), hashSalt[0], hashSalt[1])){
                HttpSession session = request.getSession();
                session.setAttribute("user-logged", userBean);
                request.getRequestDispatcher("/index").forward(request, response);
                return;
            } else {
                request.getSession().invalidate();
                request.setAttribute("invalidUser", true);
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                return;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkPassword(String password, String hash, String salt){
        password = password + salt;

        try {
            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            password = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return password.equals(hash);
    }
}
