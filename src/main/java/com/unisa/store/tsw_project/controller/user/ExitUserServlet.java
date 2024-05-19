package com.unisa.store.tsw_project.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "ExitUser", urlPatterns = "/exit-user")
public class ExitUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Optional<Object> userAtt = Optional.ofNullable(request.getSession().getAttribute("userlogged"));

        //User already logged or else Redirect to Home Page
        if(userAtt.isPresent()) {
            request.getSession().invalidate();
        }

        response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
    }
}
