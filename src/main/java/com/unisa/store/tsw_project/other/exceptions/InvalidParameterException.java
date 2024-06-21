package com.unisa.store.tsw_project.other.exceptions;

public class InvalidParameterException extends RuntimeException{
    public InvalidParameterException() {
        super("Invalid Parameter. ");
    }

    public InvalidParameterException(String message) {
        super("E: " + message);
    }
}
