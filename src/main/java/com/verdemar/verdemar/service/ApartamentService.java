package com.verdemar.verdemar.service;

import java.util.List;

import com.verdemar.verdemar.domain.Apartament;


public interface ApartamentService {
    // Define method signatures for the service here

    List<Apartament> getAllApartments();
    Apartament getApartmentById(Short id);
    Apartament createApartment(Apartament apartment);
    Apartament updateApartment(Short id, Apartament apartment);
    void deleteApartment(Short id);

}