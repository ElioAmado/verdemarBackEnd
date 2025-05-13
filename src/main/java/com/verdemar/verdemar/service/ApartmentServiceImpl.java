package com.verdemar.verdemar.service;

import com.verdemar.verdemar.domain.Apartment;
import com.verdemar.verdemar.repository.ApartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApartmentServiceImpl implements ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Override
    public List<Apartment> getAllApartments() {
        // Devuelve todos los apartmentos
        return apartmentRepository.findAll();
    }

    @Override
    public Apartment getApartmentById(Short id) {
        // Obtiene un apartmento por ID
        Optional<Apartment> apartment = apartmentRepository.findById(id);
        return apartment.orElseThrow(() -> new RuntimeException("Apartment not found with id: " + id));
    }

    @Override
    public Apartment createApartment(Apartment apartment) {
        // Crea un nuevo apartmento
        return apartmentRepository.save(apartment);
    }

    @Override
    public Apartment updateApartment(Short id, Apartment apartment) {
        // Verifica si el apartmento existe antes de actualizar
        if (!apartmentRepository.existsById(id)) {
            throw new RuntimeException("Apartment not found with id: " + id);
        }
        apartment.setId(id); // Aseguramos que se mantiene el mismo ID
        return apartmentRepository.save(apartment);
    }

    @Override
    public void deleteApartment(Short id) {
        // Elimina un apartmento por ID
        if (!apartmentRepository.existsById(id)) {
            throw new RuntimeException("Apartment not found with id: " + id);
        }
        apartmentRepository.deleteById(id);
    }
}
