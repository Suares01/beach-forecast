package com.beachforecast.beachforecastapi.errors;

import org.springframework.http.HttpStatus;

public class ConflictException extends ApiException {
  public ConflictException(String message) {
    super(message, HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase());
  }
}
