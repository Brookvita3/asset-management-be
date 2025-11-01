package com.example.demo.exception;

public class DataNotFound extends RuntimeException {
  public DataNotFound(String message) {
    super(message);
  }
}
