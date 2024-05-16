package com.unisa.store.tsw_project.controller.admin;

import com.unisa.store.tsw_project.model.DAO.AdminDAO;
import com.unisa.store.tsw_project.model.beans.AdminBean;
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
        Optional<String> action = Optional.ofNullable(req.getParameter("action"));
        Optional<Object> adminAtt = Optional.ofNullable(req.getSession().getAttribute("admin"));
        Optional<Object> validAtt = Optional.ofNullable(req.getSession().getAttribute("valid"));

        //Admin already logged - Check for Exit Action or else Redirect to Admin Page
        if(adminAtt.isPresent() && validAtt.isPresent()){
            if(action.isPresent() && action.get().equals("exit")){
                req.getSession().invalidate();
                resp.sendRedirect("/");
                return;
            }

            resp.sendRedirect(req.getContextPath() + "/console");
            return;
        }

        Optional<String> user = Optional.ofNullable(req.getParameter("user"));
        Optional<String> pass = Optional.ofNullable(req.getParameter("pass"));
        AdminBean admin = null;
        AdminDAO adminDAO = new AdminDAO();

        if(pass.isEmpty() || user.isEmpty()) {
            req.getSession().invalidate(); //To be sure, to be sure
            req.setAttribute("invalidUser", true);
            req.getRequestDispatcher("admin-login.jsp").forward(req, resp);
            return;
        }

        try{
            admin = new AdminBean(user.get(), pass.get());
            if(adminDAO.doRetrieveByCredentials(admin)){
                HttpSession session = req.getSession();
                session.setAttribute("user", admin.getUser());
                session.setAttribute("admin", true);
                session.setAttribute("valid", true);
                req.getRequestDispatcher("/WEB-INF/admin/console.jsp").forward(req, resp);
            }

        } catch (SQLException e) {
            req.getRequestDispatcher("/WEB-INF/errors/error.jsp").forward(req, resp);
        }

    }

    @Override
    public void destroy() {

    }
}
