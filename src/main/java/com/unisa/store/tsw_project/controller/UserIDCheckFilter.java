package com.unisa.store.tsw_project.controller;

import com.unisa.store.tsw_project.controller.user.LoginUserServlet;
import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.UserBean;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")  // For all requests
public class UserIDCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpRequest.getCookies();
        String userIDFromCookie = null, userPasswordFromCookie = null;
        Cookie userIDCookie = null, userPasswordCookie = null;
        Boolean userLogged = false;

        if (cookies == null) {
            chain.doFilter(request, response); // continue with the rest of the filter chain
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JSESSIONID")) {
                userLogged = true;
            }
        }

        if (!userLogged) {
            chain.doFilter(request, response); // continue with the rest of the filter chain
            return;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userID")) {
                userIDFromCookie = cookie.getValue();
                userIDCookie = cookie;
            } else if (cookie.getName().equals("userPassword")) {
                userPasswordFromCookie = cookie.getValue();
                userPasswordCookie = cookie;
            }
        }

        if (httpRequest.getSession().getAttribute("userlogged") != null){
            chain.doFilter(request, response); // continue with the rest of the filter chain
            return;
        }
        if (userIDCookie != null && userPasswordCookie != null) {
            try {
                //System.out.println("UserIDCheckFilter is working");
                String userIDecrypted = LoginUserServlet.decrypt(userIDFromCookie);
                String userPasswordDecrypted = LoginUserServlet.decrypt(userPasswordFromCookie);
                UserDAO userDAO = new UserDAO();
                UserBean user = userDAO.getUserByEmail(userIDecrypted);
                System.out.println(userPasswordDecrypted);
                System.out.println(userDAO.doRetrieveHashAndSaltByUserId(user.getId_cred())[0]);
                if (userDAO.doRetrieveHashAndSaltByUserId(user.getId_cred())[0].equals(userPasswordDecrypted)){
                    httpRequest.getSession().setAttribute("userlogged", user);
                } else { // if the user is not found in the database, delete the cookie because it is altered or not correct
                    deleteCookies(cookies, (HttpServletResponse) response);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            deleteCookies(cookies, (HttpServletResponse) response);
        }

        chain.doFilter(request, response); // continue with the rest of the filter chain
    }

    private void deleteCookies(Cookie[] cookies, HttpServletResponse response) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userID") || cookie.getName().equals("userPassword")){
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

}