package com.hacken.controller;

import com.hacken.exception.CustomError;
import com.hacken.exception.TypeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError handleServiceException(TypeException ex, HttpServletRequest req) {
        return new CustomError(req.getRequestURI(), ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletRequest req) {
        return new CustomError(req.getRequestURI(), ex.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CustomError handleException(Exception ex, HttpServletRequest req) {
        return new CustomError(req.getRequestURI(), ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
    }
}
