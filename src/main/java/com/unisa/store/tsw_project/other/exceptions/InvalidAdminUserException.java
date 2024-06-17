package com.unisa.store.tsw_project.other.exceptions;

public class InvalidAdminUserException extends RuntimeException{
    public InvalidAdminUserException() {
        super("Invalid User - Try Again ");
    }

    public InvalidAdminUserException(String message) {
        super("Invalid User: "+ message + " - Try Again ");
    }
}
