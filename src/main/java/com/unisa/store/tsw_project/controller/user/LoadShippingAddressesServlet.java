package com.unisa.store.tsw_project.controller.user;

import com.google.gson.Gson;
import com.unisa.store.tsw_project.model.DAO.ShippingAddressesDAO;
import com.unisa.store.tsw_project.model.beans.ShippingAddressesBean;
import com.unisa.store.tsw_project.model.beans.UserBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "LoadShippingAddresses", urlPatterns = "/loadShippingAddresses")
public class LoadShippingAddressesServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Verify User
        if (request.getSession().getAttribute("userlogged") == null){
            response.sendRedirect(request.getContextPath()+"/index"); //Pagina utente
            return;
        }

        ShippingAddressesDAO shippingAddressesDAO = new ShippingAddressesDAO();
        UserBean userBean = (UserBean) request.getSession().getAttribute("userlogged");
        try {
            List<ShippingAddressesBean> shippingAddresses = shippingAddressesDAO.doRetrieveAllByUserId(userBean.getId());
            Gson gson = new Gson();
            String json = gson.toJson(shippingAddresses);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
