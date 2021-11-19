package com.example.demo.controller;

import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
public class ApiError {

  private final String message;
  private final int status;

  ApiError(Throwable throwable, HttpStatus status) {
    this(throwable.getMessage(), status);
  }

  ApiError(String message, HttpStatus status) {
    this.message = message;
    this.status = status.value();
  }

  public String getMessage() {
    return message;
  }

  public int getStatus() {
    return status;
  }

}
