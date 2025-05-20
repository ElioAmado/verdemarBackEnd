package com.verdemar.verdemar.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.verdemar.verdemar.domain.Apartment;
import com.verdemar.verdemar.domain.ApartmentType;
import com.verdemar.verdemar.exception.ApartmentException;
import com.verdemar.verdemar.service.ApartmentService;


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
    public ResponseEntity<ApartmentType[]> getApartmentTypes() {
        ApartmentType[] apartmentsTypes = apartmentService.getApartmentTypes();
        return ResponseEntity.ok(apartmentsTypes);
    }

    @GetMapping("/available")
    public List<Apartment> getAvailableApartments(
            @RequestParam String type,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate.trim());
            LocalDate end = LocalDate.parse(endDate.trim());
            ApartmentType apartmentType = ApartmentType.valueOf(type.trim().toUpperCase());

            return apartmentService.getAvailableApartments(start, end, apartmentType);

        } catch (IllegalArgumentException e) {
            throw new ApartmentException("Tipo de apartamento inválido: " + type);
        } catch (java.time.format.DateTimeParseException e) {
            throw new ApartmentException("Formato de fecha inválido. Usa yyyy-MM-dd");
        }
    }

    
}
