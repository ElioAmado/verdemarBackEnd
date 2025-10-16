package com.verdemar.exception.apartment;

public class ApartmentNotFoundException extends RuntimeException {

  public ApartmentNotFoundException(Short id) {
    super("Apartment not found with id: " + id);
  }

}
