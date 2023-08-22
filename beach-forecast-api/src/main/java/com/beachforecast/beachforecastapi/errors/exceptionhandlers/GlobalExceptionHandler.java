package com.beachforecast.beachforecastapi.errors.exceptionhandlers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.beachforecast.beachforecastapi.errors.ResponseFormat;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseFormat> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors()
        .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

    HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    return ResponseEntity.status(httpStatus.value())
        .body(
            new ResponseFormat(httpStatus.value(), httpStatus.getReasonPhrase(), ResponseFormat.getErrorsMap(errors)));
  }

  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ResponseFormat> handleGeneralExceptions(Exception ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());

    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    return ResponseEntity.status(httpStatus.value())
        .body(
            new ResponseFormat(httpStatus.value(), httpStatus.getReasonPhrase(), ResponseFormat.getErrorsMap(errors)));
  }

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<ResponseFormat> handleRuntimeExceptions(RuntimeException ex) {
    List<String> errors = Collections.singletonList(ex.getMessage());

    HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

    return ResponseEntity.status(httpStatus.value())
        .body(
            new ResponseFormat(httpStatus.value(), httpStatus.getReasonPhrase(), ResponseFormat.getErrorsMap(errors)));
  }
}
