package com.unisa.store.tsw_project.controller.admin;

import com.unisa.store.tsw_project.model.DAO.AdminDAO;
import com.unisa.store.tsw_project.model.beans.AdminBean;
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
import java.util.Optional;

@WebServlet(name = "LoginAdmin", urlPatterns = "/admin-login")
public class LoginAdminServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Object> adminAtt = Optional.ofNullable(req.getSession().getAttribute("admin"));
        Optional<Object> validAtt = Optional.ofNullable(req.getSession().getAttribute("valid"));

        //Admin already logged - Redirect to Admin Page
        if(adminAtt.isPresent() && validAtt.isPresent()){
            resp.sendRedirect(req.getContextPath() + "/console");
            return;
        }

        Optional<String> user = Optional.ofNullable(req.getParameter("user"));
        Optional<String> pass = Optional.ofNullable(req.getParameter("pass"));
        AdminBean admin = null;
        AdminDAO adminDAO = new AdminDAO();

        if(pass.isEmpty() || user.isEmpty()) {
            respondInvalidUser(req, resp);
            return;
        }

        try{
            DataValidator validator = new DataValidator();
            validator.validatePattern(user.get(), DataValidator.PatternType.GenericAlphaNumeric);
            validator.validatePattern(pass.get(), DataValidator.PatternType.Generic);

            Optional<String[]> data = Optional.ofNullable(adminDAO.doRetrieveSaltAndHash(user.get()));

            if(data.isEmpty()){
                respondInvalidUser(req, resp);
                return;
            }

            admin = new AdminBean(user.get(), pass.get() + data.get()[0]);

            if(!admin.getPass().equals(data.get()[1])) {
                respondInvalidUser(req, resp);
                return;
            }

            HttpSession session = req.getSession();
            session.setAttribute("user", admin.getUser());
            session.setAttribute("admin", true);
            session.setAttribute("valid", true);
            req.getRequestDispatcher("/WEB-INF/admin/console.jsp").forward(req, resp);

        } catch (SQLException e) {
            req.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(req, resp);
        } catch (InvalidParameterException e){
            respondInvalidUser(req, resp);
        }

    }

    private void respondInvalidUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate(); //To be sure, to be sure
        req.setAttribute("invalidUser", true);
        req.getRequestDispatcher("admin-login.jsp").forward(req, resp);
    }


}
