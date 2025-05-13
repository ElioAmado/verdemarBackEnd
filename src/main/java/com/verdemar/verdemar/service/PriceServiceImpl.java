package com.verdemar.verdemar.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.verdemar.verdemar.domain.Price;
import com.verdemar.verdemar.domain.PriceId;
import com.verdemar.verdemar.repository.PriceRepository;

@Service
public class PriceServiceImpl implements PriceService {

    @Autowired
    private  PriceRepository priceRepository;

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
    public Price updatePrice(Short apartmentId, LocalDate date, Price price) {
        PriceId priceId = new PriceId(apartmentId, date);

        if (!priceRepository.existsById(priceId)) {
            throw new RuntimeException("Price not found for apartment " + apartmentId + " and date " + date);
        }

        if (!price.getApartment().getId().equals(apartmentId) || !price.getDate().equals(date)) {
            throw new RuntimeException("Mismatched apartment or date in body vs URL");
        }

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
