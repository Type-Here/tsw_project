package com.unisa.store.tsw_project.controller.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "UserProfile", urlPatterns = "/user-profile")
public class UserProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (request.getSession().getAttribute("userlogged") != null){
            request.getRequestDispatcher("/WEB-INF/results/user-profile.jsp").forward(request,response); //user profile page
        } else {
            response.sendRedirect(request.getContextPath()+"/index"); //user page
        }

    }
}
