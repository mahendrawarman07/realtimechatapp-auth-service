package com.example.authservice.exceptions;

public class UnknownUserException extends Exception {
    public UnknownUserException(String message) {
        super(message);
    }
}
