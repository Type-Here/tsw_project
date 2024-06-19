package com.unisa.store.tsw_project.controller.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unisa.store.tsw_project.model.DAO.AdminDAO;
import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.AdminBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.LocalDateAdapter;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@WebServlet(name = "UsersManagerAdmin", urlPatterns = "/WEB-INF/admin/users-manager")
public class UsersManagerServlet extends HttpServlet {

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
                case "retrieve" -> doSendUsers(req, resp); //Retrieve By Status
                case "retrieveId" -> doSendUserByID(req, resp); //Retrieve By Id

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



    /* =================== PRIVATE METHODS ======================== */

    /**
     * AJAX: Send JSON <br />
     * Send Users. <br />
     * Each page has 10 elements
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if response write fails
     * @throws SQLException if query fails
     */
    private void doSendUsers(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Optional<String> page = Optional.ofNullable(req.getParameter("page"));
        int pageOffset = 0;

        if(page.isPresent()){
            DataValidator validator = new DataValidator();
            validator.validatePattern(page.get(), DataValidator.PatternType.Int, 0, null);
            pageOffset = Integer.parseInt(page.get()) * 10;
        }

        UserDAO userDAO = new UserDAO();
        List<UserBean> users = userDAO.doRetrieveUsers(10, pageOffset);

        String info = "{\"status\":\"continue\",";
        if(users.isEmpty() || users.size() < 10) {
            info = "{\"status\":\"last\",";
        }


        // Create a GsonBuilder instance
        GsonBuilder builder = new GsonBuilder();
        // Register the LocalDateAdapter with the builder
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        // Create a Gson instance from the builder
        Gson gson = builder.create(); //No serializeNulls() so null values won't be serialized

        String json = gson.toJson(users);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        System.out.println(json);
        resp.getWriter().print(info + "\"data\":" + json + "}");
    }


    /**
     * AJAX: Send JSON <br />
     * Send User found by his ID. <br />
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException if response write fails
     * @throws SQLException if query fails
     */
    private void doSendUserByID(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Optional<String> id = Optional.ofNullable(req.getParameter("id"));
        if(id.isEmpty()) { throw new InvalidParameterException("ID is required");}

        DataValidator validator = new DataValidator();
        validator.validatePattern(id.get(), DataValidator.PatternType.Int, 1, null);

        UserDAO userDAO = new UserDAO();
        UserBean user = userDAO.getUserById(Integer.parseInt(id.get()), true);
        if(user == null) {
            throw new InvalidParameterException("User not found");
        }

        // Create a GsonBuilder instance
        GsonBuilder builder = new GsonBuilder();
        // Register the LocalDateAdapter with the builder
        builder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        // Create a Gson instance from the builder
        Gson gson = builder.create(); //No serializeNulls() so null values won't be serialized
        String json = gson.toJson(user);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        //AJAX Expects an Array of Users so add [] to print table, this avoids the use of an arraylist
        resp.getWriter().print("{\"data\":[" + json + "]}");

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
