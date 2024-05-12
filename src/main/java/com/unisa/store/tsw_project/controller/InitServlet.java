package com.unisa.store.tsw_project.controller;

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
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@WebServlet(name = "InitService", urlPatterns = "/myinit", loadOnStartup = 0)
public class InitServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) throws ServletException {
        try {
            ServletContext app = getServletContext();
            Logger logger = Logger.getLogger("base-log");

            URL resource = getServletContext().getResource("/WEB-INF/log");
            Files.createDirectories(Path.of(resource.getPath()));
            File log = new File(resource + "log.txt");
            if(!log.exists()){
                if(!log.createNewFile()){
                    System.err.println("Unable to Create log File");
                    return;
                }
            }

            logger.addHandler(new FileHandler(log.getPath(), true));
            app.setAttribute("logger", logger);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
