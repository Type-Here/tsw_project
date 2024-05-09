package com.unisa.store.tsw_project.controller.admin;

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

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@WebServlet(name = "Console", urlPatterns = "/console")
@MultipartConfig(
        fileSizeThreshold = 1024, // 1 KB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 20   // 20 MB
)
public class ConsoleServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Object> adminAtt = Optional.ofNullable(req.getSession().getAttribute("admin"));
        Optional<Object> user = Optional.ofNullable(req.getSession().getAttribute("user"));

        if(adminAtt.isEmpty() || !(adminAtt.get() instanceof Boolean b) || !b){
            throw new InvalidUserException(user.map(o -> (String) o).orElse("undefined"));
        }

        ProductBean p = new ProductBean();
        Map<String, String[]> parameters = req.getParameterMap();

        validateParametersAndSetProd(p, parameters); //Throws Error if Values are not valid




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
                            validator.validatePattern(value[0], DataValidator.PatternType.GenericAlphaNumeric), value[0]);
                    break;

                case "price":
                    validateAndSet(p::setPrice,
                            validator.validatePattern(value[0], DataValidator.PatternType.Double, 0, null),
                            Double.parseDouble(value[0]));
                    break;

                case "type":
                    validateAndSet(p::setType,
                            validator.validatePattern(value[0], DataValidator.PatternType.Bool),
                            Boolean.valueOf(value[0]));
                    break;

                case "platform":
                    validateAndSet(p::setPlatform,
                            validator.validatePattern(value[0], DataValidator.PatternType.GenericAlphaNumeric), value[0]);
                    break;

                case "developer":
                    validateAndSet(p::setDeveloper,
                            validator.validatePattern(value[0], DataValidator.PatternType.GenericAlphaNumeric), value[0]);
                    break;

                case "key":
                    if(value[0] ==  null) break;
                    validateAndSet(p::setKey,
                            validator.validatePattern(value[0], DataValidator.PatternType.GenericAlphaNumeric, 5, 15), value[0]);
                    break;

                case "condition":
                    if(value[0] ==  null) break;
                    validateAndSet(p::setCondition,
                            validator.validatePattern(value[0], DataValidator.PatternType.Condition),
                            Data.Condition.valueOf(value[0]));
                    break;

                case "discount":
                    if(value[0] ==  null) break;
                    validateAndSet(p::setDiscount,
                            validator.validatePattern(value[0], DataValidator.PatternType.Double, 0, 100),
                            Double.parseDouble(value[0]));
                    break;

                case "description":
                case "front-image":
                case "gallery-images":
                    /*TODO*/
                    break;

                default:
                    throw new InvalidParameterException(key);
            }
        }
    }

    private <T> void validateAndSet(Consumer<T> setter, boolean isValid, T value) {
        if (!isValid) {
            throw new InvalidParameterException((String)value);
        }
        setter.accept(value);
    }


}
