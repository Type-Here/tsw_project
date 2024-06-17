package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.UserDAO;
import com.unisa.store.tsw_project.model.beans.UserBean;
import com.unisa.store.tsw_project.other.DataValidator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Optional;

@WebServlet(name = "LoginUser", urlPatterns = "/user-login")
public class LoginUserServlet extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if (request.getSession().getAttribute("userlogged") != null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }



        Optional<String> email = Optional.ofNullable(request.getParameter("email"));
        Optional<String> password = Optional.ofNullable(request.getParameter("password"));

        //Controllo campi vuoti
        if(password.isEmpty() || email.isEmpty()){
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("/WEB-INF/results/login.jsp").forward(request, response);
            return;
        }

        // Validazione campi
        DataValidator validator = new DataValidator();

        try {
            validator.validatePattern(email.get(), DataValidator.PatternType.Email);
            validator.validatePattern(password.get(), DataValidator.PatternType.Password);
        } catch (Exception e) {
            //Gestione errore di validazione campi
            request.getSession().invalidate();
            request.setAttribute("invalidUser", true);
            request.getRequestDispatcher("/WEB-INF/results/login.jsp").forward(request, response);
            return;
        }


        //Controllo se l'utente è registrato nel database

        UserBean userBean = new UserBean();
        UserDAO userDAO = new UserDAO();

        try {
            userBean = userDAO.getUserByEmail(email.get());
            String address = "WEB-INF/results/login.jsp"; //Default send to login page

            //Controllo password e settaggio sessione
            if(userBean != null && userDAO.checkPassword(password.get(), userBean.getId_cred())){
                HttpSession session = request.getSession();
                session.setAttribute("userlogged", userBean);

                //Cookie
                setUserCookies(request, response, userBean);
                address = "/index";
            } else {
                request.getSession().invalidate();
                request.setAttribute("invalidUser", true);
            }
            request.getRequestDispatcher(address).forward(request, response);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if (req.getSession().getAttribute("userlogged") != null){
            resp.sendRedirect(req.getContextPath() + "/index");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/results/login.jsp").forward(req, resp);
    }



    /* ------------------------------------- OTHER METHODS ------------------------------------------- */


    //Encryption and Decryption
    private static final String ALGORITHM = "AES";
    //private static final byte[] KEY = "8pipp8pipp8pipp8".getBytes(); //Key for cipher 16, 24 or 32 byte

    public static String encrypt(String username) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Key key = new SecretKeySpec(keyGeneratedByDate(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedByteValue = cipher.doFinal(username.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedByteValue);
    }

    public static String decrypt(String encryptedUsername) throws Exception {
        Key key = new SecretKeySpec(keyGeneratedByDate(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedValue64 = Base64.getDecoder().decode(encryptedUsername);
        byte[] decryptedByteValue = cipher.doFinal(decryptedValue64);
        return new String(decryptedByteValue, StandardCharsets.UTF_8);
    }

    private static byte[] keyGeneratedByDate() {
        //Key generation
        String key = java.time.LocalDate.now()+"pipp8";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(key.getBytes(StandardCharsets.UTF_8));
            byte[] KEY = new byte[32];
            System.arraycopy(hash, 0, KEY, 0, 32);
            return KEY;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setUserCookies(HttpServletRequest request, HttpServletResponse response, UserBean userBean) throws ServletException, IOException {
        //Cookie
        UserDAO userDAO = new UserDAO();
        try {
            Cookie userIDCookie = new Cookie("userID", encrypt(userBean.getEmail()));
            Cookie userPasswordCookie = new Cookie("userPassword", encrypt(userDAO.doRetrieveHashAndSaltByUserId(userBean.getId_cred())[0]));

            //Set cookie to expire at midnight
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);


            // Set the cookie to expire at midnight
            userIDCookie.setMaxAge((int) Duration.between(now, midnight).getSeconds());
            userPasswordCookie.setMaxAge((int) Duration.between(now, midnight).getSeconds());

            // Add the cookie to the response
            userIDCookie.setHttpOnly(true);
            userPasswordCookie.setHttpOnly(true);
            //userIDCookie.setSecure(true); // solo se il sito è in HTTPS
            //userPasswordCookie.setSecure(true); // solo se il sito è in HTTPS
            response.addCookie(userPasswordCookie);
            response.addCookie(userIDCookie);

        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
