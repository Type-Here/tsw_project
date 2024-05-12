package com.unisa.store.tsw_project.controller;

import java.io.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.MetaData;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "HomeServlet", urlPatterns = "")
public class HomeServlet extends HttpServlet {

    public void init() { }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ProductDAO productDAO = new ProductDAO();

        try {
            List<ProductBean> products = productDAO.doRetrieveAll(18, "discount");

            JSONMetaParser parser = new JSONMetaParser();
            parser.doParseMetaData(products, getServletContext());

            //Add product list to request and Send to JSP for View
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/results/index.jsp").forward(req,resp);

        } catch (SQLException e){
            throw new RuntimeException(e); /*TODO*/
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void destroy() {
    }
}