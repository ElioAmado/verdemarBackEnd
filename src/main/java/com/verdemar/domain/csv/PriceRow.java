package com.verdemar.domain.csv;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.verdemar.config.LocalDateConverter;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PriceRow {

  @CsvBindByName(column = "apartment_id")
  private int apartmentId;

  @CsvCustomBindByName(column = "date", converter = LocalDateConverter.class)
  private LocalDate date;

  @CsvBindByName(column = "price")
  private double price;
}
