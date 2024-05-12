package com.unisa.store.tsw_project.other;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.unisa.store.tsw_project.model.beans.MetaData;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import jakarta.servlet.ServletContext;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JSONMetaParser {
    private final List<ProductBean> products;
    private final ServletContext application;

    public JSONMetaParser(List<ProductBean> products, ServletContext application) {
        this.products = products;
        this.application = application;
    }

    public void doParseMetaData() throws FileNotFoundException {
        // For Each Product in list
        for( ProductBean prod : products) {
            String meta = prod.getMetadataPath();

            //MetaData Initializing
            String front = null;
            JsonArray galleryArr = null;
            List<String> gallery = new ArrayList<>();

            //Read Buffer from JSON
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.application.getRealPath("metadata/" + prod.getPlatform() + '/' + meta)));
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


}
