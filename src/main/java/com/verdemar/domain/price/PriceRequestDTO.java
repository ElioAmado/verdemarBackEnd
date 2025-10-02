package com.verdemar.domain.price;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceRequestDTO {
    private BigDecimal price;
}
