package com.verdemar.service;

import java.time.LocalDate;
import java.util.List;

import com.verdemar.domain.Apartment;
import com.verdemar.domain.ApartmentType;


public interface ApartmentService {
    // Define method signatures for the service here

    List<Apartment> getAllApartments();
    Apartment getApartmentById(Short id);
    Apartment createApartment(Apartment apartment);
    Apartment updateApartment(Short id, Apartment apartment);
    void deleteApartment(Short id);
    ApartmentType[] getApartmentTypes();
    List<Apartment> getAvailableApartments(LocalDate startDate, LocalDate endDate, ApartmentType apartmentType);

}
