package com.unisa.store.tsw_project.controller.user;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.DAO.ReviewsDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.model.beans.ReviewsBean;
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
import java.util.List;
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
            int idProd = Integer.parseInt(id.get());

            ProductDAO dao = new ProductDAO();
            ProductBean product = dao.doRetrieveById(idProd);
            JSONMetaParser parser = new JSONMetaParser();
            parser.doParseMetaData(product, getServletContext());

            ReviewsDAO rewDAO = new ReviewsDAO();
            List<ReviewsBean> reviews = rewDAO.doRetrieveByProductId(idProd);
            Double averageVote = (reviews.stream().map(r ->(double) r.getVote()).reduce(0.0,(a, r) -> a + r / reviews.size()));
            req.setAttribute("averageVote", averageVote);
            req.setAttribute("reviews", reviews);
            req.setAttribute("product", product);
            req.getRequestDispatcher("/WEB-INF/results/product.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
