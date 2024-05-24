package com.unisa.store.tsw_project.controller.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "AdminProdManager", urlPatterns = "/WEB-INF/admin/prod-manager")
public class ProdManagerServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Optional<String> header = Optional.ofNullable(req.getHeader("X-Requested-With"));
        if(header.isEmpty() || !header.get().equalsIgnoreCase("XMLHttpRequest")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        DataValidator validator = new DataValidator();

        Optional<String> ask = Optional.ofNullable(req.getParameter("ask"));
        if(ask.isEmpty() || !validator.validatePattern(ask.get(), DataValidator.PatternType.GenericAlphaNumeric)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            switch (ask.get()) {
                case "modifyProd":
                    Optional<String> id = Optional.ofNullable(req.getParameter("id"));
                    if(id.isEmpty() || !validator.validatePattern(id.get(), DataValidator.PatternType.Int)) {
                        throw new InvalidParameterException("id");
                    }

                    int idNum = Integer.parseInt(id.get());
                    doSendByID(idNum, resp);
                    return;

                case "accessProd":
                    int prodNumber = (int) Optional.of(getServletContext().getAttribute("prod-number")).orElse(10);
                    int limit = 10;
                    int pages = (prodNumber / limit) + 1;

                    Optional<String> requestPages = Optional.ofNullable(req.getParameter("requestPages"));
                    if(requestPages.isPresent() && requestPages.get().equals("true")) {
                        sendPageNumber(pages, resp);
                        return;
                    }
                    Optional<String> page = Optional.ofNullable(req.getParameter("page"));

                    if (page.isEmpty() || !validator.validatePattern(page.get(), DataValidator.PatternType.Int, 1, pages)) {
                        throw new InvalidParameterException("page");
                    }
                    int pageUSer = Integer.parseInt(page.get());

                    doRetrievePage(limit, pageUSer, resp);
                    break;

                case "searchByName":
                    Optional<String> search = Optional.ofNullable(req.getParameter("search"));
                    if (search.isEmpty() || !validator.validatePattern(search.get(), DataValidator.PatternType.Generic)) {
                        throw new InvalidParameterException("search");
                    }
                    doRetrieveByName(search.get(), resp);
                    break;
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    /* -------- PRIVATE METHODS -------- */
    private void doSendByID(int id, HttpServletResponse resp) throws IOException, SQLException {
        ProductDAO dao = new ProductDAO();
        ProductBean prod = dao.doRetrieveById(id);
        Gson gson = new Gson();
        String json = gson.toJson(prod);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
    }



    private void doRetrievePage(int limit, int pageUSer, HttpServletResponse resp)
            throws IOException, SQLException {

        int offset = limit * (pageUSer - 1);
        ProductDAO productDAO = new ProductDAO();
        List<ProductBean> products = productDAO.doRetrieveAll(limit, null, offset);
        doSendResponse(products, resp);
    }


    private void doRetrieveByName(String name, HttpServletResponse resp)
            throws IOException, SQLException {
        ProductDAO productDAO = new ProductDAO();
        List<ProductBean> products = productDAO.doRetrieveByName(name);
        doSendResponse(products, resp);
    }


    private void doSendResponse(List<ProductBean> products, HttpServletResponse resp) throws IOException {
        Type listType = new TypeToken<ArrayList<ProductBean>>() {}.getType();
        Gson gson = new Gson();
        String json = gson.toJson(products, listType);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(json);
        //resp.getWriter().flush();
    }

    private void sendPageNumber(Integer pages, HttpServletResponse resp) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(pages);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print(json);

    }

}