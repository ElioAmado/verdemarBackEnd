package com.verdemar.service.apartment;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.apartment.ApartmentType;
import com.verdemar.domain.dto.ApartmentAvailabilityDTO;
import java.time.LocalDate;
import java.util.List;

public interface ApartmentService {
  // Define method signatures for the service here

  List<Apartment> getAllApartments();

  Apartment getApartmentById(Integer id);

  Apartment createApartment(Apartment apartment);

  Apartment updateApartment(Integer id, Apartment apartment);

  void deleteApartment(Integer id);

  ApartmentType[] getApartmentTypes();

  // Additional methods for availability and filtering
  List<Apartment> getAvailableApartments(
      LocalDate startDate, LocalDate endDate, ApartmentType apartmentType);

  List<Integer> getAllIds();

  List<ApartmentAvailabilityDTO> getAvailabilityList(
      LocalDate startDate, LocalDate endDate, ApartmentType apartmentType);

  List<ApartmentAvailabilityDTO> getAvailabilityListWithoutType(LocalDate start, LocalDate end);
  public List<Apartment> apartmentCSVReader(String path);
}
