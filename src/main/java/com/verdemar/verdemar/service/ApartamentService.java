package com.verdemar.verdemar.service;

import java.util.List;

import com.verdemar.verdemar.domain.Apartament;


public interface ApartamentService {
    // Define method signatures for the service here

    List<Apartament> getAllApartments();
    Apartament getApartmentById(Long id);
    Apartament createApartment(Apartament apartment);
    Apartament updateApartment(Long id, Apartament apartment);
    void deleteApartment(Long id);

}