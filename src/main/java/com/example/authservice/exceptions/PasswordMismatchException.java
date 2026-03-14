package com.example.authservice.exceptions;

public class PasswordMismatchException extends Exception {
    public PasswordMismatchException(String message) {
        super(message);
    }
}
