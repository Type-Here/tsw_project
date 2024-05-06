package com.unisa.store.tsw_project;

import java.io.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.MetaData;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "HomeServlet", urlPatterns = "")
public class HomeServlet extends HttpServlet {

    public void init() { }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        ProductDAO productDAO = new ProductDAO();
        List<ProductBean> products = productDAO.doRetrieveAll();

        // For Each Product in list
        for( ProductBean prod : products){
            String meta = prod.getMetadataPath();

            //MetaData Initializing
            String desc = null;
            List<String> imagesList = new ArrayList<>();

            //Read Buffer from JSON
            BufferedReader bufferedReader = new BufferedReader(new FileReader(getServletContext().getRealPath("metadata/ps1/" + meta)));
            //Using Google GSON
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(bufferedReader, JsonElement.class);

            /*
             * - JsonObject = Use ge("key") to retrieve a JsonElement (key:value/values)
             * - JsonElement = This is a JSON Element for a previous specified key: use getAs.... to retrieve its value (es getAsString() )
             * - JsonArray = If the returned element is an array save it here.
             */

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                desc = obj.get("description").getAsString();
                JsonObject images = obj.getAsJsonObject("images");

                imagesList.add(images.get("front").getAsString());
            }

            //Save metadata in a new MetaData Object
            MetaData metaData = new MetaData();
            metaData.setDescription(desc);
            metaData.setImages(imagesList);

            //Add to product element
            prod.setMetaData(metaData);
        }

        //Add product list to request and Send to JSP for View
        req.setAttribute("products", products);
        req.getRequestDispatcher("/WEB-INF/results/index.jsp").forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void destroy() {
    }
}