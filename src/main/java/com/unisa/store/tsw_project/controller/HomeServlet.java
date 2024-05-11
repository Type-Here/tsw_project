package com.unisa.store.tsw_project.controller;

import java.io.*;

import java.sql.SQLException;
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

        try {
            List<ProductBean> products = productDAO.doRetrieveAll(18, "discount");

            // For Each Product in list
            for( ProductBean prod : products){
                String meta = prod.getMetadataPath();

                //MetaData Initializing
                String front = null;
                JsonArray galleryArr = null;
                List<String> gallery = new ArrayList<>();

                //Read Buffer from JSON
                BufferedReader bufferedReader = new BufferedReader(new FileReader(getServletContext().getRealPath("metadata/" + prod.getPlatform() + '/' + meta)));
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
                    front = obj.get("front").getAsString();
                    galleryArr = obj.get("gallery").getAsJsonArray();
                    for(JsonElement o : galleryArr){
                        gallery.add(o.getAsString()); /*TODO*/
                    }
                }

                //Save metadata in a new MetaData Object
                MetaData metaData = new MetaData();
                metaData.setPath("metadata/" + prod.getPlatform() + "/img/" + prod.getId_prod() + '/');
                metaData.setFront(front);
                metaData.setGallery(gallery);

                //Add to product element
                prod.setMetaData(metaData);
            }

            //Add product list to request and Send to JSP for View
            req.setAttribute("products", products);
            req.getRequestDispatcher("/WEB-INF/results/index.jsp").forward(req,resp);

        } catch (SQLException e){
            throw new RuntimeException(e); /*TODO*/
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void destroy() {
    }
}