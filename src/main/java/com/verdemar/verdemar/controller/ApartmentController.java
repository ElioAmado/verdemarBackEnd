package com.verdemar.verdemar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.verdemar.verdemar.domain.Apartment;
import com.verdemar.verdemar.domain.ApartmentType;
import com.verdemar.verdemar.service.ApartmentService;

import java.util.List;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @GetMapping
    public ResponseEntity<List<Apartment>> getAllApartments() {
        List<Apartment> apartments = apartmentService.getAllApartments();
        return ResponseEntity.ok(apartments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Apartment> getApartmentById(@PathVariable Short id) {
        Apartment apartment = apartmentService.getApartmentById(id);
        return ResponseEntity.ok(apartment);
    }

    @PostMapping
    public ResponseEntity<Apartment> createApartment(@RequestBody Apartment apartment) {
        Apartment createdApartment = apartmentService.createApartment(apartment);
        return ResponseEntity.ok(createdApartment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Apartment> updateApartment(@PathVariable Short id, @RequestBody Apartment apartment) {
        Apartment updatedApartment = apartmentService.updateApartment(id, apartment);
        return ResponseEntity.ok(updatedApartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApartment(@PathVariable Short id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    public ResponseEntity<ApartmentType[]> getApartamentsTypes() {
        ApartmentType[] apartmentsTypes = apartmentService.getApartamentsTypes();
        return ResponseEntity.ok(apartmentsTypes);
    }
}
