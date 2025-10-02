package com.verdemar.service.price;

import java.time.LocalDate;
import java.util.List;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceRequestDTO;

public interface PriceService {

    List<Price> getAllPrices();

    Price getPriceById(Short apartment, LocalDate date);

    Price createPrice(Price price);

    Price updatePrice(Short apartment, LocalDate date, PriceRequestDTO price);

    void deletePrice(Short apartment, LocalDate date);
}
