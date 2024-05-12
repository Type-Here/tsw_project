package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.DataInput;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "QueryServlet", urlPatterns = "/query")
public class SearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> queryString = Optional.ofNullable(req.getParameter("q"));
        DataValidator validator = new DataValidator();

        if(queryString.isEmpty() ||
                !validator.validatePattern(queryString.get(), DataValidator.PatternType.GenericAlphaNumeric)){
            return;
        }

        try {

            ProductDAO productDAO = new ProductDAO();
            List<ProductBean> products = productDAO.doRetrieveByName(queryString.get());

            JSONMetaParser parser = new JSONMetaParser();
            parser.doParseMetaData(products, getServletContext());

            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/results/index.jsp").forward(req,resp);

        } catch (SQLException e){
            throw new RuntimeException(e);
        }

    }

    @Override
    public void destroy() {
    }
}
