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
import java.util.*;

@WebServlet(name = "StoreServlet", urlPatterns = "/store")
public class StoreServlet extends HttpServlet {
    private DataValidator validator;


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestWith = req.getHeader("X-Requested-With");
        String responseAddress = "/WEB-INF/results/store.jsp";
        this.validator = new DataValidator();

        if (requestWith != null && requestWith.equals("XMLHttpRequest")) {
            //Set to Print partial HTML to send to JS in AJAX
            responseAddress = "/WEB-INF/include/tiles-printer.jsp";
        }

        //Set Page number and default maxPage
        Optional<String> pageNum = Optional.ofNullable(req.getParameter("page"));
        try {
            int prodNumber = (int) getServletContext().getAttribute("prod-number");
            int pages = (prodNumber / 18) + 1;
            int page = 1;
            if (pageNum.isPresent()) {
                int receivedValue = Integer.parseInt(pageNum.get());
                if(receivedValue <= pages && receivedValue > 0) {
                    page = Integer.parseInt(pageNum.get());
                }
            }

            int limit = 18;
            int offset = (page - 1) * 18;

            //Send to doFilter with Limit = 18 elements and Offset
            List<ProductBean> products = doFilter(req, limit, offset);

            //Add product list to request and Send to JSP for View
            req.setAttribute("maxPage", pages);
            req.setAttribute("page", page);
            req.setAttribute("products", products);
            req.getRequestDispatcher(responseAddress).forward(req, resp);

        } catch (NumberFormatException e){
            throw new InvalidParameterException("page");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /* ------------------------ PRIVATE METHOD ---------------------- */

    /**
     * Find Products that Apply to Filter in Filter Bar <br />
     * Filters; Platform, Developer, Category/ies, Minimum Price, Maximum Price
     * @param req HttpServletRequest
     * @param limit list of products retrieved size is limited to this value
     * @param offset offset to apply to DataBase query. Offset by Page
     * @return List of Product Beans that apply to Filter
     * @throws IOException if response fails
     * @throws SQLException if query fails
     */
    private List<ProductBean> doFilter(HttpServletRequest req, Integer limit, Integer offset) throws IOException, SQLException {

        //Get Parameters
        Optional<String> platform = Optional.ofNullable(req.getParameter("platform"));
        Optional<String> dev = Optional.ofNullable(req.getParameter("dev"));
        Optional<String[]> categories = Optional.ofNullable(req.getParameterValues("category"));
        Optional<String> minPrice = Optional.ofNullable(req.getParameter("minprice"));
        Optional<String> maxPrice = Optional.ofNullable(req.getParameter("maxprice"));


        //Params: Key=Column Name of DB, Value=Value from User
        Map<String, String> params = new LinkedHashMap<>();

        //Validate Parameters and Add them to params Map
        platform.ifPresent(s -> {
            if(s.isEmpty()) return;
            validator.validatePattern(s, DataValidator.PatternType.Platform);
            params.put("platform", s);
        });

        dev.ifPresent(s -> {
            if(s.isEmpty()) return;
            validator.validatePattern(s, DataValidator.PatternType.GenericAlphaNumeric);
            params.put("developer", s);
        });

        minPrice.ifPresent(s -> {
            if(s.isEmpty()) return;
            validator.validatePattern(s, DataValidator.PatternType.Double);
            params.put("minprice", s);
        });

        maxPrice.ifPresent(s -> {
            if(s.isEmpty()) return;
            validator.validatePattern(s, DataValidator.PatternType.Double);
            params.put("maxprice", s);
        });

        List<Integer> catIds = new ArrayList<>();
        categories.ifPresent(c -> {
            //Validate All possible Category Parameters (multiple checkbox)
            for (String categoryParam : c) {
                validator.validatePattern(categoryParam, DataValidator.PatternType.Int);
                catIds.add(Integer.parseInt(categoryParam));
            }
        });

        //Retrieve Data with applied filters
        ProductDAO productDAO = new ProductDAO();
        /* Filter By Parameters Map. If no parameters are set a select all is done instead */
        List<ProductBean> products = productDAO.doRetrieveByParameters(params, limit, offset, catIds);

        //Parse Metadata and retrieve Images Path of Product Result
        JSONMetaParser parser = new JSONMetaParser();
        parser.doParseMetaData(products, getServletContext());
        return products;

    }

    /* UNUSED FOR NOW */
    @SuppressWarnings({"unused"})
    private void filterByCategory(HttpServletRequest req, List<ProductBean> products, Optional<String[]> categories) {

        //Category filters are applied here from previous result
        categories.ifPresent(c ->{
            //Validate Parameters
            for(String categoryParam : c){
                validator.validatePattern(categoryParam, DataValidator.PatternType.Int);
            }
            //Remove product from list if its categories are not in ones chosen by user
            products.removeIf(productBean -> productBean.getCategoryBeanList().stream()
                    .noneMatch(prodCat -> {
                        for (String categoryParam : categories.get()) {
                            if (prodCat.getId_cat() == Integer.parseInt(categoryParam)) return true;
                        }
                        return false;
                    })
            );
        });
    }


}
