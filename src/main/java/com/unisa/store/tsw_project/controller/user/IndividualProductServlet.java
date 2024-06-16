package com.unisa.store.tsw_project.controller.user;

import com.google.gson.Gson;
import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.DAO.ReviewsDAO;
import com.unisa.store.tsw_project.model.beans.ConditionBean;
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
import java.math.BigDecimal;
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
                resp.sendRedirect(req.getContextPath());
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<String> ajax = Optional.ofNullable(req.getHeader("X-Requested-With"));
        if(ajax.isPresent() && ajax.get().equals("XMLHttpRequest")) {
            try {
                resolveAjax(req, resp);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "XmlHttpRequest Needed");
        }
    }


    /* ------- PRIVATE METHOD ------- */

    /**
     * Resolve Ajax request and send answer. <br/>
     * options: <br />
     * - fetchPrice: retrieve price of a specified condition of a product (requires id product and id condition in request)
     * @param req HttpServletRequest
     * @param resp HttpServletResponse
     * @throws IOException is response writing fails
     */
    private void resolveAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {

        DataValidator validator = new DataValidator();

        Optional<String> option = Optional.ofNullable(req.getParameter("opt"));

        if(option.isPresent() && option.get().equals("fetchPrice")) {
           Optional<String> id = Optional.ofNullable(req.getParameter("id_prod"));
           Optional<String> condition = Optional.ofNullable(req.getParameter("condition"));

           if(id.isEmpty() || condition.isEmpty()) {
               resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid id or condition");
               return;
           }
           validator.validatePattern(id.get(), DataValidator.PatternType.Int, 1, null);
           validator.validatePattern(condition.get(), DataValidator.PatternType.Int, 0, 5);

           ProductDAO dao = new ProductDAO();
           ProductBean p = dao.doRetrieveById(Integer.parseInt(id.get()));

           ConditionBean cond = p.getConditions().stream().filter(c -> c.getId_cond() == Integer.parseInt(condition.get())).findFirst().orElseThrow(() -> new InvalidParameterException("condition"));
           String price = String.format("%.2f", p.getPrice().multiply(BigDecimal.valueOf(( 1 - (double) cond.getCondition().discount /100))));

           Gson gson = new Gson();
           String json = gson.toJson(price);
           resp.setContentType("application/json");
           resp.getWriter().write(json);
           resp.getWriter().flush();

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No valid option");
        }
    }

}
