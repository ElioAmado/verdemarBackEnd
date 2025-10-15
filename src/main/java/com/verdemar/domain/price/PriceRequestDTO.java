package com.verdemar.domain.price;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceRequestDTO {
  private BigDecimal price;
}
