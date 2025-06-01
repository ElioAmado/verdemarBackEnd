package com.verdemar.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.verdemar.domain.Booking.Status;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookingDto {
    private Long id = null; // ID opcional para actualizaciones
    private Integer clientId;
    private byte guests;
    private Short apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private Status status;
    private String notes;
    // getters y setters
}
