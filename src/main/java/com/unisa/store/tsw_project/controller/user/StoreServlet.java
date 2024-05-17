package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "StoreServlet", urlPatterns = "/store")
public class StoreServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> pageNum = Optional.ofNullable(req.getParameter("page"));
        try {
            int prodNumber = (int) getServletContext().getAttribute("prod-number");
            int pages = (prodNumber / 18) + 1;
            int page = 1;
            if (pageNum.isPresent() && Integer.parseInt(pageNum.get()) <= pages) {
                page = Integer.parseInt(pageNum.get());
            }

            int limit = 18;
            int offset = (page - 1) * 18;

            ProductDAO productDAO = new ProductDAO();
            List<ProductBean> products = productDAO.doRetrieveAll(limit, null, offset);

            //Retrieve Images for Products from JSON
            JSONMetaParser parser = new JSONMetaParser();
            parser.doParseMetaData(products, getServletContext());

            //Add product list to request and Send to JSP for View
            req.setAttribute("maxPage", pages);
            req.setAttribute("page", page);
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/results/store.jsp").forward(req, resp);

        } catch (NumberFormatException e){
            throw new InvalidParameterException("page");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
