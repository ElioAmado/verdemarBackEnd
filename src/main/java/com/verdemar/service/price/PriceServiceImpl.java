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
  public Price getPriceById(Integer apartment, LocalDate date) {
    Optional<Price> price = priceRepository.findById(new PriceId(apartment, date));
    return price.orElseThrow(() -> new ApartmentNotAvailable(apartment));
  }

  @Override
  public List<Price> getPricesByMounth(Integer apartmentId , int month, int year) {
    LocalDate startDate = LocalDate.of(year, month, 1);
    LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
    return priceRepository.findByApartmentIdAndDateBetween(apartmentId, startDate, endDate);
  }

  // Crea un nuevo precio
  @Override
  public Price createPrice(PriceResponseDTO price) {
    Price newPrice = modelMapper.map(price, Price.class);
    return priceRepository.save(newPrice);
  }

  @Override
  public List<Price> createPrice(List<PriceResponseDTO> prices) {
    List<Price> newPrices = prices.stream()
        .map(priceDto -> modelMapper.map(priceDto, Price.class))
        .toList();
    List<Price> savedPrices = priceRepository.saveAll(newPrices);
    return savedPrices; // Devuelve el primer precio guardado como ejemplo
  }

  // Actualiza un precio existente
  @Override
  public Price updatePrice(Integer apartmentId, LocalDate date, PriceRequestDTO priceDto) {
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

  @Override
  public List<Price> bulkUpdatePrices(List<PriceResponseDTO> prices) {
    List<Price> updatedPrices = prices.stream().map(priceDto -> {
      PriceId priceId = new PriceId(priceDto.getApartmentId(), priceDto.getDate());
      Price price =
          priceRepository
              .findById(priceId)
              .orElseThrow(
                  () ->
                      new RuntimeException(
                          "Price not found for apartment " + priceDto.getApartmentId() + " and date " + priceDto.getDate()));
      price.setPrice(priceDto.getPrice());
      return price;
    }).toList();

    return priceRepository.saveAll(updatedPrices);
  }

  // Elimina un precio por su ID (apartment, date)
  @Override
  public void deletePrice(Integer apartment, LocalDate date) {
    if (!priceRepository.existsById(new PriceId(apartment, date))) {
      throw new RuntimeException(
          "Price not found for apartment " + apartment + " and date " + date);
    }
    priceRepository.deleteById(new PriceId(apartment, date));
  }
}
