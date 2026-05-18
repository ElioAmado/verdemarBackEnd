package com.verdemar.controller;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceRequestDTO;
import com.verdemar.domain.price.PriceResponseDTO;
import com.verdemar.repository.PriceRepository;
import com.verdemar.service.price.PriceService;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/price")
public class PriceController {

  @Autowired private PriceService priceService;
  
  // Gets
  // Obtener todos los precios
  @GetMapping
  public ResponseEntity<List<Price>> getAllPrices(Pageable pageable) {

      // Pageable pageable = PageRequest.of(page, size);
      List<Price> prices = priceService.getAllPrices(pageable);
      return ResponseEntity.ok(prices);
  }

  @GetMapping("/dto")
  public ResponseEntity<List<PriceResponseDTO>> getAllPricesDto() {
    List<PriceResponseDTO> prices = priceService.getAllPriceDto();
    return ResponseEntity.ok(prices);
  }

  // Obtener un precio por ID (apartment, date)
  @GetMapping("/{apartment}/{date}")
  public ResponseEntity<Price> getPriceById(
      @PathVariable Integer apartment, @PathVariable LocalDate date) {
    Price price = priceService.getPriceById(apartment, date);
    return ResponseEntity.ok(price);
  }

  @GetMapping("/bymonth")
  public ResponseEntity<List<Price>> getPricesByMounth(
    @RequestParam Integer apartmentId,
      @RequestParam int month, @RequestParam int year) {
    List<Price> prices = priceService.getPricesByMounth(apartmentId, month, year);
    return ResponseEntity.ok(prices);
  }

  // Crear un nuevo precio
  @PostMapping
  public ResponseEntity<Price> createPrice(@RequestBody PriceResponseDTO price) {
  Price createdPrice = priceService.createPrice(price);
  return ResponseEntity.ok(createdPrice);
  }
  
  @PostMapping("/bulk")
  public ResponseEntity<List<Price>> createPrices(@RequestBody List<PriceResponseDTO> prices) {
    List<Price> createdPrices = priceService.createPrice(prices);
    return ResponseEntity.ok(createdPrices);
  }

  // Puts

  // Actualizar un precio
  @PutMapping("/{apartment}/{date}")
  public ResponseEntity<Price> updatePrice(
      @PathVariable Integer apartment,
      @PathVariable LocalDate date,
      @RequestBody PriceRequestDTO priceDto) {
    Price updatedPrice = priceService.updatePrice(apartment, date, priceDto);
    return ResponseEntity.ok(updatedPrice);
  }

  @PutMapping("/bulk")
  public ResponseEntity<List<Price>> bulkUpdatePrices(
      @RequestBody List<PriceResponseDTO> prices) {
    List<Price> updatedPrices = priceService.bulkUpdatePrices(prices);
    return ResponseEntity.ok(updatedPrices);
  }
  // Deletes

  // Eliminar un precio
   @DeleteMapping("/{apartment}/{date}")
   public ResponseEntity<Void> deletePrice(
   @PathVariable Integer apartment,
   @PathVariable LocalDate date) {
   priceService.deletePrice(apartment, date);
   return ResponseEntity.noContent().build();
   }
}
