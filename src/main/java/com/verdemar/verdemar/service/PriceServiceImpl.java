package com.verdemar.verdemar.service;

import com.verdemar.verdemar.domain.Price;
import com.verdemar.verdemar.repository.PriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;

    @Autowired
    public PriceServiceImpl(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public List<Price> getAllPrices() {
        return priceRepository.findAll();
    }

    @Override
    public Price getPriceById(Short apartment, LocalDate date) {
        Optional<Price> price = priceRepository.findById(new PriceId(apartment, date));
        return price.orElseThrow(() -> new RuntimeException("Price not found for apartment " + apartment + " and date " + date));
    }

    @Override
    public Price createPrice(Price price) {
        return priceRepository.save(price);
    }

    @Override
    public Price updatePrice(Short apartment, LocalDate date, Price price) {
        if (!priceRepository.existsById(new PriceId(apartment, date))) {
            throw new RuntimeException("Price not found for apartment " + apartment + " and date " + date);
        }
        price.setApartment(apartment); // Set the existing apartment ID
        price.setDate(date); // Set the existing date
        return priceRepository.save(price);
    }

    @Override
    public void deletePrice(Short apartment, LocalDate date) {
        if (!priceRepository.existsById(new PriceId(apartment, date))) {
            throw new RuntimeException("Price not found for apartment " + apartment + " and date " + date);
        }
        priceRepository.deleteById(new PriceId(apartment, date));
    }
}
