package com.verdemar.verdemar.service;

import java.time.LocalDate;
import java.util.List;

import com.verdemar.verdemar.domain.Price;

public interface PriceService {

    List<Price> getAllPrices();

    Price getPriceById(Short apartment, LocalDate date);

    Price createPrice(Price price);

    Price updatePrice(Short apartment, LocalDate date, Price price);

    void deletePrice(Short apartment, LocalDate date);
}
