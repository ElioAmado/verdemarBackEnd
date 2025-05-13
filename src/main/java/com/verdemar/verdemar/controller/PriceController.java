package com.verdemar.verdemar.controller;

import com.verdemar.verdemar.domain.Price;
import com.verdemar.verdemar.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    // Obtener todos los precios
    @GetMapping
    public ResponseEntity<List<Price>> getAllPrices() {
        List<Price> prices = priceService.getAllPrices();
        return ResponseEntity.ok(prices);
    }

    // Obtener un precio por ID (apartment, date)
    @GetMapping("/{apartment}/{date}")
    public ResponseEntity<Price> getPriceById(@PathVariable Short apartment, @PathVariable LocalDate date) {
        Price price = priceService.getPriceById(apartment, date);
        return ResponseEntity.ok(price);
    }

    // Crear un nuevo precio
    @PostMapping
    public ResponseEntity<Price> createPrice(@RequestBody Price price) {
        Price createdPrice = priceService.createPrice(price);
        return ResponseEntity.ok(createdPrice);
    }

    // Actualizar un precio
    @PutMapping("/{apartment}/{date}")
    public ResponseEntity<Price> updatePrice(@PathVariable Short apartment, @PathVariable LocalDate date, @RequestBody Price price) {
        Price updatedPrice = priceService.updatePrice(apartment, date, price);
        return ResponseEntity.ok(updatedPrice);
    }

    // Eliminar un precio
    @DeleteMapping("/{apartment}/{date}")
    public ResponseEntity<Void> deletePrice(@PathVariable Short apartment, @PathVariable LocalDate date) {
        priceService.deletePrice(apartment, date);
        return ResponseEntity.noContent().build();
    }
}
