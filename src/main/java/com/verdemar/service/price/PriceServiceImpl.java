package com.verdemar.service.price;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceId;
import com.verdemar.domain.price.PriceRequestDTO;
import com.verdemar.repository.PriceRepository;

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
    public Price updatePrice(Short apartmentId, LocalDate date, PriceRequestDTO priceDto) {
        PriceId priceId = new PriceId(apartmentId, date);


        Price price = priceRepository.findById(priceId)
                .orElseThrow(() -> new RuntimeException(
                        "Price not found for apartment " + apartmentId + " and date " + date));


        price.setPrice(priceDto.getPrice());

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
