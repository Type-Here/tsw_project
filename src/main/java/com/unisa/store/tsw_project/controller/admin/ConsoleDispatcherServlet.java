package com.unisa.store.tsw_project.controller.admin;

import com.unisa.store.tsw_project.other.exceptions.InvalidUserException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;


@WebServlet(name = "ConsoleDispatcher", urlPatterns = "/console")
public class ConsoleDispatcherServlet extends HttpServlet {
    private enum Actions{addProd, prodManager}

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Verify User
        this.verifyAdminUser(req);

        Optional<String> action = Optional.ofNullable(req.getParameter("action"));
        action.map(Actions::valueOf).ifPresent(a -> {
            try{
                switch (a) {
                    case addProd -> req.getRequestDispatcher("/WEB-INF/admin/add-prod").forward(req, resp);
                    case prodManager -> req.getRequestDispatcher("/WEB-INF/admin/prod-manager").forward(req, resp);
                    default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                }
                return;
            } catch (Exception e){
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        if(action.isEmpty()){ //Without this IF: sendError gives error (500 not 400!)
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Verify User /* TODO Remind Admin incorrect method */
        this.verifyAdminUser(req);
        req.getRequestDispatcher("/WEB-INF/admin/console.jsp").forward(req, resp);
    }

    /* --- Private Methods --- */

    /**
     * Verifies if User is Correctly logged as Admin
     * @param req request to retrieve session data
     * @throws InvalidUserException if user is not logged / incorrect
     */
    private void verifyAdminUser(HttpServletRequest req) {
        Optional<Object> adminAtt = Optional.ofNullable(req.getSession().getAttribute("admin"));
        Optional<Object> user = Optional.ofNullable(req.getSession().getAttribute("user"));

        if(adminAtt.isEmpty() || !(adminAtt.get() instanceof Boolean b) || !b){
            throw new InvalidUserException(user.map(o -> (String) o).orElse("undefined"));
        }
    }


}