package com.verdemar.exception.apartment;

public class ApartmentNotAvailable extends ApartmentException {
  public ApartmentNotAvailable(Integer id) {
    super("Apartment with id " + id + " is not available for the selected dates.");
  }

    
}
