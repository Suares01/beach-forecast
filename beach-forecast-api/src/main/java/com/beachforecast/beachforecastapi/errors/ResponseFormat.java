package com.beachforecast.beachforecastapi.errors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseFormat {
  private Integer code;
  private String error;
  private String message;
  private Map<String, List<String>> messages;

  public ResponseFormat(Integer code, String error, Map<String, List<String>> messages) {
    this.code = code;
    this.error = error;
    this.messages = messages;
  }

  public ResponseFormat(Integer code, String error, String message) {
    this.code = code;
    this.error = error;
    this.message = message;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public Map<String, List<String>> getMessages() {
    return messages;
  }

  public void setMessages(Map<String, List<String>> messages) {
    this.messages = messages;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public static Map<String, List<String>> getErrorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }
}
