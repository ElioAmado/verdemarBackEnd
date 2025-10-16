package com.verdemar.exception;

public class DiscountNotFoundException  extends RuntimeException {
    public DiscountNotFoundException(Long id) {
        super("Discount with ID " + id + " was not found.");
    }
    
}
