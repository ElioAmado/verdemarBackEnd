package com.verdemar.service.apartment;

import java.time.LocalDate;
import java.util.List;

import com.verdemar.domain.Apartment;
import com.verdemar.domain.ApartmentType;
import com.verdemar.domain.dto.ApartmentAvailabilityDTO;


public interface ApartmentService {
    // Define method signatures for the service here

    List<Apartment> getAllApartments();
    Apartment getApartmentById(Short id);
    Apartment createApartment(Apartment apartment);
    Apartment updateApartment(Short id, Apartment apartment);
    void deleteApartment(Short id);
    ApartmentType[] getApartmentTypes();

    // Additional methods for availability and filtering
    List<Apartment> getAvailableApartments(LocalDate startDate, LocalDate endDate, ApartmentType apartmentType);
    List<Short> getAllIds();
    List<ApartmentAvailabilityDTO> getAvailabilityList(
        LocalDate startDate, LocalDate endDate, ApartmentType apartmentType);

}
