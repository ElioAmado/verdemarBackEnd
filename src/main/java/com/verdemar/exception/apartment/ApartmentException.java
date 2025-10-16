package com.verdemar.exception.apartment;

public class ApartmentException extends RuntimeException {

  public ApartmentException(String message) {
    super(message);
  }

  public ApartmentException(String message, Throwable cause) {
    super(message, cause);
  }
}
