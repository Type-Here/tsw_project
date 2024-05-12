package com.unisa.store.tsw_project.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "ExitAdmin", urlPatterns = "/exit_admin")
public class ExitAdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Object> adminAtt = Optional.ofNullable(req.getSession().getAttribute("admin"));
        Optional<Object> validAtt = Optional.ofNullable(req.getSession().getAttribute("valid"));

        //Admin already logged or else Redirect to Home Page
        if(adminAtt.isPresent() && validAtt.isPresent()) {
            req.getSession().invalidate();
        }

        resp.sendRedirect("index");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
