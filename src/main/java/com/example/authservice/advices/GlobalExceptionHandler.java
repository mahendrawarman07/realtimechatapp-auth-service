package com.example.authservice.advices;

import com.example.authservice.dtos.ExceptionDto;
import com.example.authservice.exceptions.ExpiredTokenException;
import com.example.authservice.exceptions.InvalidTokenException;
import com.example.authservice.exceptions.PasswordMismatchException;
import com.example.authservice.exceptions.UnknownUserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionDto> handleInvalidTokenException() {

        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Unauthorized access, token is invalid. Please try again with correct credentials");
        /// any number of params we can add in exception dto.

        return new ResponseEntity<>(
                exceptionDto,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ExceptionDto> handleExpiredTokenException() {

        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Unauthorized access, token has expired. Please try again with correct credentials");
        /// any number of params we can add in exception dto.

        return new ResponseEntity<>(
                exceptionDto,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ExceptionDto> handleInvalidPasswordException() {

        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage("Unauthorized access, password mismatch. Please try again with correct credentials");
        /// any number of params we can add in exception dto.

        return new ResponseEntity<>(
                exceptionDto,
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UnknownUserException.class)
    public ResponseEntity<ExceptionDto> handleUnknownUserException(UnknownUserException ex) {

        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setMessage(ex.getMessage());
        /// any number of params we can add in exception dto.

        return new ResponseEntity<>(
                exceptionDto,
                HttpStatus.NOT_FOUND
        );
    }
}
