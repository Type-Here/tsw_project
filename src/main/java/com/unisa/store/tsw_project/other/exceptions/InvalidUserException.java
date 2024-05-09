package com.unisa.store.tsw_project.other.exceptions;

public class InvalidUserException extends RuntimeException{
    public InvalidUserException() {
        super("Invalid User - Try Again ");
    }

    public InvalidUserException(String message) {
        super("Invalid User: "+ message + " - Try Again ");
    }
}
