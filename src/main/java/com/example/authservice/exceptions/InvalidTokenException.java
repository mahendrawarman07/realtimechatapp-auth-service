package com.example.authservice.exceptions;

public class InvalidTokenException extends Exception {
    public InvalidTokenException(String message) {
        super(message);
    }
}