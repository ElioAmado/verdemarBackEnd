package com.verdemar.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.verdemar.config.LocalDateConverter;
import com.verdemar.domain.booking.Booking.Status;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class BookingRow {
  @CsvBindByName(column = "id")
  private Long id = null; // ID opcional para actualizaciones

  @CsvBindByName(column = "client_id")
  private Integer clientId;

  @CsvBindByName(column = "guests")
  private byte guests;

  @CsvBindByName(column = "apartment_id")
  private Integer apartmentId;

  @CsvCustomBindByName(column = "startDate", converter = LocalDateConverter.class)
  private LocalDate startDate;

  @CsvBindByName(column = "status")
  private Status status;

  @CsvBindByName(column = "notes")
  private String notes;
}
