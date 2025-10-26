package com.verdemar.service.price;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceId;
import com.verdemar.domain.price.PriceRequestDTO;
import com.verdemar.domain.price.PriceResponseDTO;
import com.verdemar.exception.apartment.ApartmentNotAvailable;
import com.verdemar.repository.PriceRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {

  @Autowired private ModelMapper modelMapper;

  @Autowired private PriceRepository priceRepository;

  // Devuelve todos los precios
  @Override
  public List<Price> getAllPrices(Pageable pageable) {
    return priceRepository.findAll(pageable).getContent();
  }

  // Devuelve todos los precios en formato DTO
  @Override
  public List<PriceResponseDTO> getAllPriceDto() {
    List<Price> prices = priceRepository.findAll();
    return prices.stream().map(price -> modelMapper.map(price, PriceResponseDTO.class)).toList();
  }

  // Devuelve un precio por su ID (apartment, date)
  @Override
  public Price getPriceById(Short apartment, LocalDate date) {
    Optional<Price> price = priceRepository.findById(new PriceId(apartment, date));
    return price.orElseThrow(() -> new ApartmentNotAvailable(apartment));
  }

  // Crea un nuevo precio
  @Override
  public Price createPrice(Price price) {
    return priceRepository.save(price);
  }

  // Actualiza un precio existente
  @Override
  public Price updatePrice(Short apartmentId, LocalDate date, PriceRequestDTO priceDto) {
    PriceId priceId = new PriceId(apartmentId, date);

    Price price =
        priceRepository
            .findById(priceId)
            .orElseThrow(
                () ->
                    new RuntimeException(
                        "Price not found for apartment " + apartmentId + " and date " + date));

    price.setPrice(priceDto.getPrice());

    return priceRepository.save(price);
  }

  // Elimina un precio por su ID (apartment, date)
  @Override
  public void deletePrice(Short apartment, LocalDate date) {
    if (!priceRepository.existsById(new PriceId(apartment, date))) {
      throw new RuntimeException(
          "Price not found for apartment " + apartment + " and date " + date);
    }
    priceRepository.deleteById(new PriceId(apartment, date));
  }
}
