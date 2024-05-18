package com.unisa.store.tsw_project.other;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.Arrays;

public class DataValidator {
    public enum PatternType{
        Generic, GenericAlphaNumeric, Email, Surname, Phone, Password, CAP, Birthdate, Username, Path, Double, Int, Condition, Bool, Platform, Category;
    }

    /**
     * Verify data respect pattern type
     * @param data to check
     * @param patternType type of pattern to use for check
     * @return true string is valid, false if not
     * @see DataValidator#validatePattern(String, PatternType, Integer, Integer);
     */
    public boolean validatePattern(String data, PatternType patternType){
        if(!validatePattern(data, patternType,null, null)) throw new InvalidParameterException();
        return true;
    }

    /**
     Verify data respect pattern type
     * @param data to check
     * @param patternType type of pattern to use for check
     * @param min if patternType is Int or Double return true if value is > of min. If it's a different pattern checks if string length is bigger than min
     * @param max if patternType is Int or Double return true if value is < of max. If it's a different pattern checks if string length is smaller than max
     * @return true if valide, false if not
     */
    public boolean validatePattern(String data, PatternType patternType,
                                   Integer min, Integer max){
        try {
            if(data == null) return false;
            if(!patternType.equals(PatternType.Double) && !patternType.equals(PatternType.Int)){
                if(min != null && data.length() < min) return false;
                if(max != null && data.length() > max) return false;
            }

            return switch (patternType) {
                case Birthdate -> {
                    LocalDate date = LocalDate.parse(data);
                    yield date.isBefore(LocalDate.now().minusYears(16));
                }
                case CAP -> data.matches("[0-9]{5,6}");
                case Phone -> data.matches("\\+39 [0-9]{8,10}");
                case Email -> data.matches("^\\w+[\\w.-]+@[\\w.-]+[.]\\w+$");
                case Username -> data.matches("^(?=.*[a-z])(?=.*[A-Z])(?!.*\\s).{3,}$");
                case Surname -> data.matches("^[a-zA-Z0-9' ]+$");
                case Password -> data.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[!£$%&/()=?'^])(?=.*[0-9]).{8,}$");
                case Int -> {
                    int val = Integer.parseInt(data);
                    yield (max == null || !(val > max)) && (min == null || !(val < min));
                }
                case Double -> {
                    double vald = Double.parseDouble(data);
                    yield (max == null || !(vald > max)) && (min == null || !(vald < min));
                }
                case GenericAlphaNumeric -> data.matches("^[a-zA-Z0-9_\\- ]+$");
                case Generic -> data.matches("^[a-zA-Z0-9_.'&$\"\\- ]+$");
                case Condition -> data.matches("[XABCDE]");
                case Bool -> data.equalsIgnoreCase("true") || data.equalsIgnoreCase("false");
                case Platform ->Arrays.stream(Data.Platform.values()).anyMatch(e -> data.equalsIgnoreCase(e.name()));
                default -> false;
            };
        } catch (NullPointerException|NumberFormatException e){
            return false;
        }
    }
}
