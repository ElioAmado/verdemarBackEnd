package com.verdemar.service.price;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceRequestDTO;
import com.verdemar.domain.price.PriceResponseDTO;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PriceService {

  Page<Price> getAllPrices(Pageable pageable);

  List<PriceResponseDTO> getAllPriceDto();

  Price getPriceById(Short apartment, LocalDate date);

  Price createPrice(Price price);

  Price updatePrice(Short apartment, LocalDate date, PriceRequestDTO price);

  void deletePrice(Short apartment, LocalDate date);
}
