package com.verdemar.verdemar.domain.dto;

import java.time.LocalDate;

public class BookingDto {
    private Integer clientId;
    private Integer apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalPrice;
    private String status;
    private String notes;
    // getters y setters
}
