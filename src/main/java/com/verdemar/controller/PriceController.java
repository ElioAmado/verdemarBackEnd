package com.verdemar.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.verdemar.domain.Price;
import com.verdemar.service.price.PriceService;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    @Autowired
    private PriceService priceService;


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
