package com.verdemar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApartmentException.class)
  public ResponseEntity<String> handleApartmentException(ApartmentException ex) {
    // Puedes personalizar la respuesta, aquí solo devuelvo el mensaje
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
  }

  // Otros manejadores de excepción si quieres...
}
