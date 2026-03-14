package com.example.authservice.services;

import com.example.authservice.exceptions.ExpiredTokenException;
import com.example.authservice.exceptions.InvalidTokenException;
import com.example.authservice.exceptions.PasswordMismatchException;
import com.example.authservice.exceptions.UnknownUserException;
import com.example.authservice.models.User;


public interface UserService {
    User signUp(String name, String email, String password);
    String login(String email, String password) throws UnknownUserException,PasswordMismatchException;

    User validateToken(String tokenValue) throws InvalidTokenException, ExpiredTokenException;

    User getUserByEmail(String email) throws UnknownUserException;
}
