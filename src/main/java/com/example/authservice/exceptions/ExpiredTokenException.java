package com.example.authservice.exceptions;

public class ExpiredTokenException extends Exception{
    public ExpiredTokenException(String message) {
        super(message);
    }
}
