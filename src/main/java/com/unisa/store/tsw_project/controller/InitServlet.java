package com.unisa.store.tsw_project.controller;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.JSONMetaParser;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@WebServlet(name = "InitService", urlPatterns = "/myinit", loadOnStartup = 0)
public class InitServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {

            System.out.println("\n----- INIT: MI sono avviata ------");
            ServletContext app = config.getServletContext();
            /*Logger logger = Logger.getLogger("base-log");

            URL resource = getServletContext().getResource("/WEB-INF/log");
            Files.createDirectories(Path.of(resource.getPath()));
            File log = new File(resource + "log.txt");
            if(!log.exists()){
                if(!log.createNewFile()){
                    System.err.println("Unable to Create log File");

                }
            }

            logger.addHandler(new FileHandler(log.getPath(), true));
            app.setAttribute("logger", logger);*/

            JSONMetaParser parser = new JSONMetaParser();
            List<Integer> high = parser.doParseHighlights("/WEB-INF/data/highlights.json", config.getServletContext());
            List<ProductBean> highProd = new ArrayList<>();
            ProductDAO dao = new ProductDAO();
            System.out.println("\n----- INIT: MI sono avviata ------");

            for(int id : high){
                highProd.add(dao.doRetrieveById(id));
            }
            parser.doParseMetaData(highProd, config.getServletContext());

            app.setAttribute("highlights", highProd);
            System.out.println(highProd + "\n" + highProd.get(0));

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
