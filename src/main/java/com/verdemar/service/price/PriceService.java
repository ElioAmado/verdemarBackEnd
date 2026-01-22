package com.verdemar.service.price;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceRequestDTO;
import com.verdemar.domain.price.PriceResponseDTO;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PriceService {

  List<Price> getAllPrices(Pageable pageable);

  List<PriceResponseDTO> getAllPriceDto();

  Price getPriceById(Short apartment, LocalDate date);

  List<Price> getPricesByMounth(short apartmentId, int month, int year);

  Price createPrice(PriceResponseDTO price);

  List<Price> createPrice(List<PriceResponseDTO> prices);

  Price updatePrice(Short apartment, LocalDate date, PriceRequestDTO price);

  List<Price> bulkUpdatePrices(List<PriceResponseDTO> prices);

  void deletePrice(Short apartment, LocalDate date);
}
