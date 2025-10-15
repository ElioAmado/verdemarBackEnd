package com.verdemar.service.price;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceRequestDTO;
import com.verdemar.domain.price.PriceResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface PriceService {

  List<Price> getAllPrices();

  List<PriceResponseDTO> getAllPriceDto();

  Price getPriceById(Short apartment, LocalDate date);

  Price createPrice(Price price);

  Price updatePrice(Short apartment, LocalDate date, PriceRequestDTO price);

  void deletePrice(Short apartment, LocalDate date);
}
