package com.verdemar.controller;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.apartment.ApartmentType;
import com.verdemar.domain.dto.ApartmentAvailabilityDTO;
import com.verdemar.exception.ApartmentException;
import com.verdemar.service.apartment.ApartmentService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/apartments")
public class ApartmentController {

  // Autowired service

  @Autowired private ApartmentService apartmentService;

  // Gets

  // Devuelve todos los apartamentos
  @GetMapping
  public ResponseEntity<List<Apartment>> getAllApartments() {
    List<Apartment> apartments = apartmentService.getAllApartments();
    return ResponseEntity.ok(apartments);
  }

  // Devuelve un apartamento por su ID
  @GetMapping("/{id}")
  public ResponseEntity<Apartment> getApartmentById(@PathVariable Short id) {
    Apartment apartment = apartmentService.getApartmentById(id);
    return ResponseEntity.ok(apartment);
  }

  // Devuelve los tipos de apartamentos disponibles
  @GetMapping("/types")
  public ResponseEntity<ApartmentType[]> getApartmentTypes() {
    ApartmentType[] apartmentsTypes = apartmentService.getApartmentTypes();
    return ResponseEntity.ok(apartmentsTypes);
  }

  // Devuelve la disponibilidad de apartamentos para un rango de fechas y tipo
  @GetMapping("/available")
  public List<ApartmentAvailabilityDTO> getAvailableApartments(
      @RequestParam String type, @RequestParam String startDate, @RequestParam String endDate) {
    try {
      LocalDate start = LocalDate.parse(startDate.trim());
      LocalDate end = LocalDate.parse(endDate.trim());
      ApartmentType apartmentType = ApartmentType.valueOf(type.trim().toUpperCase());

      return apartmentService.getAvailabilityList(start, end, apartmentType);

    } catch (IllegalArgumentException e) {
      throw new ApartmentException("Tipo de apartamento inválido: " + type);
    } catch (java.time.format.DateTimeParseException e) {
      throw new ApartmentException("Formato de fecha inválido. Usa yyyy-MM-dd");
    }
  }

  // Devuelve todos los IDs de los apartamentos
  @GetMapping("/ids")
  public List<Short> getAllIds() {
    return apartmentService.getAllIds();
  }

  // Posts

  // @PostMapping
  // public ResponseEntity<Apartment> createApartment(@RequestBody Apartment apartment) {
  //     Apartment createdApartment = apartmentService.createApartment(apartment);
  //     return ResponseEntity.ok(createdApartment);
  // }

  // Puts

  // Actualiza un apartamento existente
  @PutMapping("/{id}")
  public ResponseEntity<Apartment> updateApartment(
      @PathVariable Short id, @RequestBody Apartment apartment) {
    Apartment updatedApartment = apartmentService.updateApartment(id, apartment);
    return ResponseEntity.ok(updatedApartment);
  }

  // Deletes

  // @DeleteMapping("/{id}")
  // public ResponseEntity<Void> deleteApartment(@PathVariable Short id) {
  //     apartmentService.deleteApartment(id);
  //     return ResponseEntity.noContent().build();
  // }

}
