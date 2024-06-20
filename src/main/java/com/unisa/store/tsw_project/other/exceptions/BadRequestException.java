package com.unisa.store.tsw_project.other.exceptions;

public class BadRequestException extends Exception {
    public BadRequestException() { super("400: Bad Request Error"); ;}

    public BadRequestException(String message) {
        super(message);
    }
}
