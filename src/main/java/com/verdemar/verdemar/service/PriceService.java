package com.verdemar.verdemar.service;

import com.verdemar.verdemar.domain.Price;
import java.util.List;

public interface PriceService {

    List<Price> getAllPrices();

    Price getPriceById(Short apartment, LocalDate date);

    Price createPrice(Price price);

    Price updatePrice(Short apartment, LocalDate date, Price price);

    void deletePrice(Short apartment, LocalDate date);
}
