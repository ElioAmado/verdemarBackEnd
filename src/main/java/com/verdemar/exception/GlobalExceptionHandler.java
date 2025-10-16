package com.verdemar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.verdemar.exception.apartment.ApartmentNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApartmentNotFoundException.class)
    public ResponseEntity<?> handleApartmentNotFoundException(ApartmentNotFoundException ex , WebRequest request){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage()); 
    }
    
    @ExceptionHandler(DiscountNotFoundException.class)
    public ResponseEntity<String> handleDiscountNotFound(DiscountNotFoundException ex) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

  // Otros manejadores de excepción si quieres...
}
