package com.unisa.store.tsw_project.controller;

import com.unisa.store.tsw_project.model.DAO.CategoryDAO;
import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.CategoryBean;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@WebServlet(name = "InitService", urlPatterns = "/myinit", loadOnStartup = 0)
public class InitServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) {
        try {
            System.out.println("\n----- INIT ------");

            /* Variables */
            ProductDAO productDAO = new ProductDAO();
            CategoryDAO categoryDAO = new CategoryDAO();

            /* IMPORTANT: ServletContext needs to be gotten from Config! */
            ServletContext app = config.getServletContext();

            /* Load Category List in Application at Startup */
            List<CategoryBean> catlist = categoryDAO.doRetrieveAll();
            app.setAttribute("category", catlist);

            /* Load Developers List in Application at Startup */
            List<String> developers = productDAO.doRetrieveDevelopers();
            app.setAttribute("developers", developers);

            /* Load MaxPrice of a product Available in Shop */
            BigDecimal maxPrice = new BigDecimal("3000").setScale(2, RoundingMode.HALF_UP);
            app.setAttribute("maxPrice", maxPrice);

            /* Load Product Number */
            int prodNum = productDAO.doCountAll();
            app.setAttribute("prod-number", prodNum);

            /* Set Logger if context-param Logging in web.xml is set to true */
            Boolean isLog = (Boolean) app.getAttribute("Logging");
            if(isLog != null && isLog){
                Logger logger = Logger.getLogger("base-log");
                URL resource = getServletContext().getResource("/WEB-INF/log");
                Files.createDirectories(Path.of(resource.getPath()));
                File log = new File(resource + "log.txt");
                if(!log.exists()){
                    if(!log.createNewFile()){
                        System.err.println("Unable to Create log File");

                    }
                }

                logger.addHandler(new FileHandler(log.getPath(), true));
                app.setAttribute("logger", logger);
            }


            /*Get IDs Info from highlights.json for Carousel Products */
            JSONMetaParser parser = new JSONMetaParser();
            List<Integer> high = parser.doParseHighlights(File.separator + "WEB-INF" +
                                        File.separator +"data" + File.separator + "highlights.json", config.getServletContext());
            List<ProductBean> highProd = new ArrayList<>();


            //Get Products from IDs
            for(int id : high){
                ProductBean p = productDAO.doRetrieveCarousel(id);
                //Protect from Errors: check if null
                if(p != null) highProd.add(p);
            }
            //Get Metadata Image for Products
            parser.doParseMetaData(highProd, config.getServletContext());

            //Set list in ServletContext
            app.setAttribute("highlights", highProd);


            /* Set Discount Code Value */
            HashMap<String, Double> discountCodeMap = new HashMap<>();
            discountCodeMap.put("ESTATE88!", 12.0);
            app.setAttribute("discountCode", discountCodeMap);

            /* Set Shipping Price -- Only if Order Total is Under 100€*/
            BigDecimal shippingCost = new BigDecimal("15.0");
            app.setAttribute("shippingCost", shippingCost);

            System.out.println("--- END OF INIT ---");

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
