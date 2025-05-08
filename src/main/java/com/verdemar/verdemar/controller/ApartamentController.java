package com.verdemar.verdemar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.verdemar.verdemar.domain.Apartament;
import com.verdemar.verdemar.service.ApartamentService;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
public class ApartamentController {

    @Autowired
    private ApartamentService apartamentService;

    @GetMapping
    public ResponseEntity<List<Apartament>> getAllApartments() {
        List<Apartament> apartments = apartamentService.getAllApartments();
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apartament> getApartmentById(@PathVariable Long id) {
        Apartament apartment = apartamentService.getApartmentById(id);
        return ResponseEntity.ok(apartment);
    }

    @PostMapping
    public ResponseEntity<Apartament> createApartment(@RequestBody Apartament apartment) {
        Apartament createdApartment = apartamentService.createApartment(apartment);
        return ResponseEntity.ok(createdApartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Apartament> updateApartment(@PathVariable Long id, @RequestBody Apartament apartment) {
        Apartament updatedApartment = apartamentService.updateApartment(id, apartment);
        return ResponseEntity.ok(updatedApartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Long id) {
        apartamentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }
}