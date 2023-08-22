package com.beachforecast.beachforecastapi.errors;

abstract class ApiException extends RuntimeException {
  private Integer code;
  private String error;

  ApiException(String message, Integer code, String error) {
    super(message);
    this.code = code;
    this.error = error;
  }

  public Integer getCode() {
    return this.code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getError() {
    return this.error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
