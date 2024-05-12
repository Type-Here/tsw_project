package com.unisa.store.tsw_project.controller.admin;

import com.unisa.store.tsw_project.model.DAO.ProductDAO;
import com.unisa.store.tsw_project.model.beans.ProductBean;
import com.unisa.store.tsw_project.other.Data;
import com.unisa.store.tsw_project.other.DataValidator;
import com.unisa.store.tsw_project.other.exceptions.InvalidParameterException;
import com.unisa.store.tsw_project.other.exceptions.InvalidUserException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;

@WebServlet(name = "Console", urlPatterns = "/console")
@MultipartConfig(
        fileSizeThreshold = 1024 * 100, // 100 KB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 20   // 20 MB
)
public class ConsoleServlet extends HttpServlet {
    private StringBuilder jsonString;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Object> adminAtt = Optional.ofNullable(req.getSession().getAttribute("admin"));
        Optional<Object> user = Optional.ofNullable(req.getSession().getAttribute("user"));

        if(adminAtt.isEmpty() || !(adminAtt.get() instanceof Boolean b) || !b){
            throw new InvalidUserException(user.map(o -> (String) o).orElse("undefined"));
        }

        req.getRequestDispatcher("/WEB-INF/admin/console.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Object> adminAtt = Optional.ofNullable(req.getSession().getAttribute("admin"));
        Optional<Object> user = Optional.ofNullable(req.getSession().getAttribute("user"));

        if(adminAtt.isEmpty() || !(adminAtt.get() instanceof Boolean b) || !b){
            throw new InvalidUserException(user.map(o -> (String) o).orElse("undefined"));
        }

        req.setAttribute("upload", false);

        ProductBean p = new ProductBean();
        Map<String, String[]> parameters = req.getParameterMap();

        validateParametersAndSetProd(p, parameters); //Throws Error if Values are not valid

        try {

            ProductDAO productDAO = new ProductDAO();
            productDAO.doSave(p); /* If fails throws SQL Exception caught below */

            /* It will contain a String of JSON metadata to save on file */
            jsonString = new StringBuilder("{\n\"front\":\"");

            /*
             * So, relative path start in bin folder of tomcat (where tomcat is executed).
             * Upload folder needs to be specified manually
             */

            /* Get Resource is a more accurate way to get URL/Folder from Tomcat. Gives actual resource not temp WAR deploy */
            /* For testing in Idea this path is: <project_folder>/target/<project_name>-SNAPSHOT/ */
            URL resource = getServletContext().getResource("/metadata");

            /*Get Image Data from Request: Needed Content-Type: multipart/form-data in form enctype tag */
            Part frontImagePart = req.getPart("front-image");

            /* Get File Extension from its name */
            String[] partition = frontImagePart.getSubmittedFileName().split("\\.");
            String extension = partition[partition.length-1];

            /* Path contains the new file path to be created */
            Path path = Path.of(resource.getPath() + p.getPlatform() +
                                        File.separator + "img" + File.separator + p.getId_prod());
            File front = new File(path + File.separator + "0." + extension);
            jsonString.append("0.").append(extension).append("\",\n\"gallery\":[");

            /* Creates all directories needed to reach that path */
            Files.createDirectories(path);

            /* Creates The File - OutputStream needs file to be created first */
            if (!front.exists()) {
                if(!front.createNewFile()) throw new IOException("Unable to Create File:" + front.toURI());
            }

            /* Writes Image as Binary Data */
            FileOutputStream out = new FileOutputStream(front);
            out.write(frontImagePart.getInputStream().readAllBytes());
            out.close();

            /* Get all Parts. TO retrieve multiple images in gallery-images input in form */
            int i = 0;
            List<Part> parts = req.getParts().stream().toList();
            for(Part pa: parts){
                if(pa.getName().equals("gallery-images")){
                    /* Retrieve files extension */
                    String[] name = pa.getSubmittedFileName().split("\\.");
                    String ext = name[name.length - 1];

                    /* Create an empty image file */
                    File image = new File(path.toString() + File.separator + (++i) + "." + ext);
                    if(!image.createNewFile()) throw new IOException("Unable to Create File:" + front.toURI());

                    /* Write inside the file the image binary data */
                    FileOutputStream outS = new FileOutputStream(image);
                    outS.write(pa.getInputStream().readAllBytes());
                    outS.close();

                    /* Append data to JSON string */
                    if(i > 1) jsonString.append(",");
                    jsonString.append("\"").append(i).append(".").append(extension).append("\"");

                }
            }

            /* Save JSON String in File '0000xx.json' where xx is the new product id */
            jsonString.append("]\n}");
            String jsonName = String.format("%06d", p.getId_prod()) + ".json";
            File json = new File(resource.getPath() + p.getPlatform() + File.separator + jsonName);
            if(!json.createNewFile()) throw new IOException("Unable to Create File:" + front.toURI());
            FileOutputStream outJson = new FileOutputStream(json);
            outJson.write(jsonString.toString().getBytes(StandardCharsets.UTF_8));

            /* Update Product with metadata path */
            p.setMetadataPath(jsonName);
            productDAO.doUpdate(p);

            /* Set attribute to true to display info of saved file */
            req.setAttribute("upload", true);
            /*Product is saved, Dispatch to view */
            req.getRequestDispatcher("/WEB-INF/admin/console.jsp").forward(req, resp);

        } catch (SQLException | IOException e){
            throw new RuntimeException(e);

        } finally {
            Logger logger = (Logger) getServletContext().getAttribute("base-log");
            if(logger != null){
                logger.info("New Product:" + p + "\n - JSON: " + jsonString);
            }
        }


    }

    @Override
    public void destroy() {
    }


    /* --- Private Methods --- */

    private void validateParametersAndSetProd(ProductBean p, Map<String, String[]> parameters) {
        DataValidator validator = new DataValidator();
        for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();

            switch (key) {
                case "name":
                    validateAndSet(p::setName,
                            validator.validatePattern(value[0], DataValidator.PatternType.GenericAlphaNumeric), value[0], key);
                    break;

                case "price":
                    validateAndSet(p::setPrice,
                            validator.validatePattern(value[0], DataValidator.PatternType.Double, 0, null),
                            Double.parseDouble(value[0]), key);
                    break;

                case "type":
                    String val = value[0];
                    if(val.equals("physic")) p.setType(Data.Type.PHYSICAL.val);
                    else if(val.equals("digital")) p.setType(Data.Type.DIGITAL.val);
                    else throw new InvalidParameterException(key);
                    break;

                case "platform":
                    validateAndSet(p::setPlatform,
                            validator.validatePattern(value[0], DataValidator.PatternType.Platform), value[0], key);
                    break;

                case "developer":
                    validateAndSet(p::setDeveloper,
                            validator.validatePattern(value[0], DataValidator.PatternType.GenericAlphaNumeric), value[0], key);
                    break;

                case "key":
                    if(value[0] ==  null || value[0].isEmpty()) break;
                    validateAndSet(p::setKey,
                            validator.validatePattern(value[0], DataValidator.PatternType.GenericAlphaNumeric, 5, 15),
                            value[0], key);
                    break;

                case "condition":
                    if(value[0] ==  null || value[0].isEmpty()) break;
                    validateAndSet(p::setCondition,
                            validator.validatePattern(value[0], DataValidator.PatternType.Condition),
                            Data.Condition.valueOf(value[0]), key);
                    break;

                case "discount":
                    if(value[0] ==  null || value[0].isEmpty()) break;
                    validateAndSet(p::setDiscount,
                            validator.validatePattern(value[0], DataValidator.PatternType.Double, 0, 100),
                            Double.parseDouble(value[0]), key);
                    break;

                case "description":
                    if(value[0] !=  null && !value[0].isEmpty()) p.setDescription(value[0]);
                    else throw new InvalidParameterException(key);
                    break;

                case "front-image":
                case "gallery-images":
                    if(value == null) throw new InvalidParameterException(key);
                    break;

                default:
                    throw new InvalidParameterException(key);
            }
        }
    }

    private <T> void validateAndSet(Consumer<T> setter, boolean isValid, T value, String key) {
        if (!isValid) {
            throw new InvalidParameterException(key + "=" + value);
        }
        setter.accept(value);
    }


}
