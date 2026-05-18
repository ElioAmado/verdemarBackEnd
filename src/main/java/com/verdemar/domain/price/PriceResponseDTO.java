package com.verdemar.domain.price;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceResponseDTO {
  private Integer apartmentId;
  private LocalDate date;
  private BigDecimal price;
}
