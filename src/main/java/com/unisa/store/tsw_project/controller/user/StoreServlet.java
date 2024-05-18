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
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestWith = req.getHeader("X-Requested-With");
        if (requestWith != null && requestWith.equals("XMLHttpRequest")) {
            goAjax(req, resp);
            return;
        }

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

    /* ------------------------ PRIVATE METHOD ---------------------- */

    private void goAjax(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        /*resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");*/
        DataValidator validator = new DataValidator();
        try {
            //Params: Key=Column Name of DB, Value=Value from User
            Map<String, String> params = new LinkedHashMap<>();
            //Get Parameters
            Optional<String> platform = Optional.ofNullable(req.getParameter("platform"));
            Optional<String> dev = Optional.ofNullable(req.getParameter("dev"));
            Optional<String[]> categories = Optional.ofNullable(req.getParameterValues("category"));
            Optional<String> minPrice = Optional.ofNullable(req.getParameter("minprice"));
            Optional<String> maxPrice = Optional.ofNullable(req.getParameter("maxprice"));

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

            //Retrieve Data with applied filters
            ProductDAO productDAO = new ProductDAO();
            List<ProductBean> products = productDAO.doRetrieveByParameters(params);

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

            //Parse Metadata and retrieve Images Path of Product Result
            JSONMetaParser parser = new JSONMetaParser();
            parser.doParseMetaData(products, getServletContext());

            //Set to request Attribute and Send to Dispatcher
            req.setAttribute("products", products);

            //Tiles Printer JSP: Is an incomplete html only to be included inside a page with AJAX
            req.getRequestDispatcher("/WEB-INF/include/tiles-printer.jsp").forward(req, resp);

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        /* ArrayList<String> a = new ArrayList<>();
         a.add(pep);
        String json = new Gson().toJson(a);
        resp.getWriter().write(json); */
    }

}
