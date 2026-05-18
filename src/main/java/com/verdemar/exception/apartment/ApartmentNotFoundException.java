package com.verdemar.exception.apartment;

public class ApartmentNotFoundException extends RuntimeException {

  public ApartmentNotFoundException(Integer id) {
    super("Apartment not found with id: " + id);
  }

}
