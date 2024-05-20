package com.unisa.store.tsw_project.other;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unisa.store.tsw_project.model.beans.MetaData;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import jakarta.servlet.ServletContext;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JSONMetaParser {

    public JSONMetaParser() {

    }

    /**
     * Parse of a Single ProductBean
     * @see JSONMetaParser#doParseMetaData(Collection, ServletContext)
     */
    public void doParseMetaData(ProductBean product, ServletContext application) throws IOException {
        List<ProductBean> p = new ArrayList<>();
        p.add(product);
        doParseMetaData(p, application);
    }

    /**
     * Parse JSON MetaData of a LIst /Se (subclasses fo Collection
     * @param products Collection of products to be parsed
     * @param application ServletContext to retrieve Path From
     * @throws IOException if Reader from file fails
     */
    public void doParseMetaData(Collection<ProductBean> products, ServletContext application) throws IOException {
        // For Each Product in list
        for( ProductBean prod : products) {
            String meta = prod.getMetadataPath();

            //MetaData Initializing
            String front = null;
            JsonArray galleryArr = null;
            List<String> gallery = new ArrayList<>();

            //Read Buffer from JSON
            BufferedReader bufferedReader = new BufferedReader(new FileReader(application.getRealPath("metadata/" + prod.getPlatform() + '/' + meta)));
            //Using Google GSON
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(bufferedReader, JsonElement.class);
            bufferedReader.close();

            /*
             * - JsonObject = Use ge("key") to retrieve a JsonElement (key:value/values)
             * - JsonElement = This is a JSON Element for a previous specified key: use getAs.... to retrieve its value (es getAsString() )
             * - JsonArray = If the returned element is an array save it here.
             */

            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                front = obj.get("front").getAsString();
                galleryArr = obj.get("gallery").getAsJsonArray();
                for (JsonElement o : galleryArr) {
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
    }

    public List<Integer> doParseHighlights(String path, ServletContext application) throws IOException{
        List<Integer> highlightsIds = new ArrayList<>();
        String pathCorrected;

        //MacOS Parsing of getResource gives problems with WhiteSpaces: replace %20 with a whitespace
        if(System.getProperty("os.name").contains("mac") || System.getProperty("os.name").contains("Mac")){
            pathCorrected = application.getResource(path).getFile().replaceAll("%20", " ");
        } else {
            pathCorrected = application.getResource(path).getFile();
        }

        try( BufferedReader bufferedReader = new BufferedReader(
                new FileReader(pathCorrected)) ){
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(bufferedReader, JsonElement.class).getAsJsonObject();
            for(int i = 1; i <= 5; i++){
                 highlightsIds.add(obj.get(String.valueOf(i)).getAsInt());
            }
        }

        return highlightsIds;
    }

    public List<Integer> doParseNewsletter(String path, ServletContext application) throws IOException {
        List<Integer> newsletterIds = new ArrayList<>();
        String pathCorrected;

        //MacOS Parsing of getResource gives problems with WhiteSpaces: replace %20 with a whitespace
        if(System.getProperty("os.name").contains("mac") || System.getProperty("os.name").contains("Mac")){
            pathCorrected = application.getResource(path).getFile().replaceAll("%20", " ");
        } else {
            pathCorrected = application.getResource(path).getFile();
        }

        try( BufferedReader bufferedReader = new BufferedReader(
                new FileReader(pathCorrected)) ){
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(bufferedReader, JsonElement.class).getAsJsonObject();
            for(int i = 1; i <= 6; i++){
                newsletterIds.add(obj.get(String.valueOf(i)).getAsInt());
            }
        }

        return newsletterIds;

    }

}