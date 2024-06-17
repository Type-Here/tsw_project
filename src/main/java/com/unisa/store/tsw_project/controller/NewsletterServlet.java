package com.unisa.store.tsw_project.controller;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "NewsletterServlet", urlPatterns = {"/newsletter"})
public class NewsletterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ProductDAO productDAO = new ProductDAO();

        try {
            List<ProductBean> products = getSeasonDiscountGames(request);

            //Retrieve Images for Products from JSON
            JSONMetaParser parser = new JSONMetaParser();
            parser.doParseMetaData(products, getServletContext());

            //Add product list to request and Send to JSP for View
            request.setAttribute("newsletter", products);

            // Inoltra la richiesta a newsletter.jsp
            request.getRequestDispatcher("/WEB-INF/results/newsletter.jsp").forward(request, response);

        } catch (SQLException e){
            throw new RuntimeException(e); /*TODO*/
        }
    }

    private List<ProductBean> getSeasonDiscountGames(HttpServletRequest request) throws IOException, SQLException {
    	List<ProductBean> products = new ArrayList<>();
        ProductDAO productDAO = new ProductDAO();
        String season;
        //Get current month and set different season, different discount.
    	int month = java.time.LocalDate.now().getMonthValue();
    	if(month == 3 ) {
    		season = "spring";
    	} else if(month == 6) {
    		season = "summer";
    	} else if(month == 9) {
    		season = "autumn";
    	} else if(month == 12){
    		season = "winter";
    	} else {
    		season = "noSeason";
    	}

        request.setAttribute("season", season);

        for(int id : getSeasonDiscountGames(season)){
            ProductBean p = productDAO.doRetrieveById(id);
            //Protect from Errors: check if null
            if(p != null) products.add(p);
        }

    	return products;
    }

    private List<Integer> getSeasonDiscountGames(String season) throws IOException {

        //Parse JSON file to get the list of games on discount
        JSONMetaParser parser = new JSONMetaParser();
        List<Integer> discountGames = parser.doParseNewsletter(File.separator + "WEB-INF" +
                File.separator +"data" + File.separator + season +"-discount.json", getServletContext());
        return discountGames;

    }
}