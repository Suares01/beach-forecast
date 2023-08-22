package com.beachforecast.beachforecastapi.errors.exceptionhandlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.beachforecast.beachforecastapi.errors.BadRequestException;
import com.beachforecast.beachforecastapi.errors.ConflictException;
import com.beachforecast.beachforecastapi.errors.NotFoundException;
import com.beachforecast.beachforecastapi.errors.ResponseFormat;

@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ResponseFormat> handleNotFoundExceptions(NotFoundException exception, WebRequest webRequest) {
    return ResponseEntity.status(exception.getCode())
        .body(new ResponseFormat(exception.getCode(), exception.getError(), exception.getMessage()));
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Object> handleConflictExceptions(ConflictException exception, WebRequest webRequest) {
    return ResponseEntity.status(exception.getCode())
        .body(new ResponseFormat(exception.getCode(), exception.getError(), exception.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> handleBadRequestExceptions(BadRequestException exception, WebRequest webRequest) {
    return ResponseEntity.status(exception.getCode())
        .body(new ResponseFormat(exception.getCode(), exception.getError(), exception.getMessage()));
  }
}
