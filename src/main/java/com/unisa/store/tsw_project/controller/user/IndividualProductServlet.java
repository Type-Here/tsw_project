package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@WebServlet(name= "IndividualProduct", urlPatterns = "/desc")
public class IndividualProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> id = Optional.ofNullable(req.getParameter("id_prod"));
        DataValidator validator = new DataValidator();

        try {
            if(id.isEmpty()){
                resp.sendRedirect("/");
                return;
            }
            if(!validator.validatePattern(id.get(), DataValidator.PatternType.Int)){
                throw new InvalidParameterException("id");
            }

            ProductDAO dao = new ProductDAO();
            ProductBean product = dao.doRetrieveById(Integer.parseInt(id.get()));

            JSONMetaParser parser = new JSONMetaParser();
            parser.doParseMetaData(product, getServletContext());

            req.setAttribute("product", product);
            req.getRequestDispatcher("/WEB-INF/results/product.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
