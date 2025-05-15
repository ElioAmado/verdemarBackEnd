package com.verdemar.verdemar.service;

import java.util.List;
import com.verdemar.verdemar.domain.ApartmentType;
import com.verdemar.verdemar.domain.Apartment;


public interface ApartmentService {
    // Define method signatures for the service here

    List<Apartment> getAllApartments();
    Apartment getApartmentById(Short id);
    Apartment createApartment(Apartment apartment);
    Apartment updateApartment(Short id, Apartment apartment);
    void deleteApartment(Short id);
    ApartmentType[] getApartamentsTypes();

}
