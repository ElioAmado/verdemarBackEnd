package com.verdemar.exception.booking;

public class BookingNotFoundException extends RuntimeException {
    
    public BookingNotFoundException(Long id) {
        super("Booking not found with id: " + id);
    }
}
