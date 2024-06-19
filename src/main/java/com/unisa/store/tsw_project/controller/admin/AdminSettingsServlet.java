package com.unisa.store.tsw_project.controller.admin;

import com.unisa.store.tsw_project.model.DAO.AdminDAO;
import com.unisa.store.tsw_project.model.beans.AdminBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Random;

@WebServlet(name = "AdminSettings", urlPatterns = "/WEB-INF/admin/settings")
public class AdminSettingsServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> header = Optional.ofNullable(req.getHeader("X-Requested-With"));
        if (header.isEmpty() || !header.get().equalsIgnoreCase("XMLHttpRequest")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid X-Requested-With");
            return;
        }
        try {
            Optional<String> ask = Optional.ofNullable(req.getParameter("ask"));
            if (ask.isEmpty()) {
                throw new InvalidParameterException("Ask Option required");
            }

            switch (ask.get()) {
                case "modifyAdmin" -> doModifyAdmin(req, resp); //Modify ADMIN
                default -> throw new InvalidParameterException("Ask Option not valid");
            }

        } catch (InvalidParameterException e){
            resp.setContentType("text/html");
            resp.getWriter().print("Invalid data - " + e.getMessage());
            resp.setStatus(Data.SC_INVALID_DATA);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            resp.setContentType("text/plain");
            resp.getWriter().print(HttpServletResponse.SC_INTERNAL_SERVER_ERROR + ": something went wrong");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        } finally {
            resp.getWriter().flush();
        }
    }


    /* ============================================ FOR ADMIN =================================================== */

    /**
     * FOR ADMIN: Change Password
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if response writing fails
     * @throws SQLException if query retrieve or update fails
     */
    private void doModifyAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Optional<String> oldPass = Optional.ofNullable(req.getParameter("old"));
        Optional<String> newPass = Optional.ofNullable(req.getParameter("new"));

        if(oldPass.isEmpty() || newPass.isEmpty()){
            throw new InvalidParameterException("Old and New Passwords Required");
        }

        AdminDAO adminDAO = new AdminDAO();
        DataValidator validator = new DataValidator();

        validator.validatePattern(oldPass.get(), DataValidator.PatternType.Generic);
        validator.validatePattern(newPass.get(), DataValidator.PatternType.Password); //If not valid throws InvalidParameterException

        //Check Old Password

        String sessionAdmin = (String) req.getSession().getAttribute("user");

        Optional<String[]> data = Optional.ofNullable(adminDAO.doRetrieveSaltAndHash(sessionAdmin));
        if(data.isEmpty()){ throw new RuntimeException("User not found"); } //Should Never Happen since Admin should be already logged

        AdminBean checkAdmin = new AdminBean(sessionAdmin, oldPass.get() + data.get()[0]);
        if(!checkAdmin.getPass().equals(data.get()[1])) {
            throw new InvalidParameterException("Invalid password");
        }

        //Set new Password and Salt

        String newSalt = getSalt();
        checkAdmin.setPass(newPass.get() + newSalt); //new Admin

        adminDAO.doUpdatePassword(checkAdmin, newSalt); //Throws SQLException if fails

        //Update AdminBean in Session
        //req.getSession().setAttribute("user", checkAdmin.getUser()); //NOT REQUIRED

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("Password Modificata con Successo");
    }


    /**
     * Generate New Salt String
     * @return Random AlphaNumeric String of 6 chars
     */
    private static String getSalt(){
        //ASCII A=65, Z=90
        Random rand = new Random();
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < 6; i++){
            if(rand.nextBoolean()) builder.append(rand.nextInt(10));
            else{
                int upperCase = rand.nextBoolean() ? 65 : 97;
                char c = (char) (rand.nextInt(26) + upperCase);
                builder.append(c);
            }
        }
        return builder.toString();
    }




}
