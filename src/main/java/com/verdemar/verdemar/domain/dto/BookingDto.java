package com.verdemar.verdemar.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.verdemar.verdemar.domain.Booking.Status;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookingDto {
    private Integer id = null; // ID opcional para actualizaciones
    private Integer clientId;
    private Integer apartmentId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal totalPrice;
    private Status status;
    private String notes;
    // getters y setters
}
