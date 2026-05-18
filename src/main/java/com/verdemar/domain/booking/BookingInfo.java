package com.verdemar.domain.booking;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.verdemar.domain.booking.Booking.Status;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingInfo {
private Long id; // ID opcional para actualizaciones
  private String clientName;
  private String clientLastName;
  private String clientPhone;
  private byte guests;
  private Integer apartmentId;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal totalPrice; // Se genera al crear la reserva
  private Status status;
  private String notes;
  // getters y setters
}
