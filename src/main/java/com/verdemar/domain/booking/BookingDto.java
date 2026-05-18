package com.verdemar.domain.booking;

import com.verdemar.domain.booking.Booking.Status;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {
  private Long id = null; // ID opcional para actualizaciones
  private Integer clientId;
  private byte guests;
  private Integer apartmentId;
  private LocalDate startDate;
  private LocalDate endDate;
  private BigDecimal totalPrice; // Se genera al crear la reserva
  private Status status;
  private String notes;
  // getters y setters
}
