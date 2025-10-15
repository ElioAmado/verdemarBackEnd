package com.verdemar.domain.dto;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookingDateRange {
  private LocalDate from;
  private LocalDate to;

  public ChronoLocalDate to() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'to'");
  }
}
