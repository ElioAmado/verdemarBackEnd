package com.verdemar.service;

import com.verdemar.domain.Apartment;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.domain.ApartmentType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

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

    @Override
    public ApartmentType[] getApartmentTypes() {
        return ApartmentType.values();
    }

    @Override
    public List<Apartment> getAvailableApartments(
        LocalDate startDate, LocalDate endDate, ApartmentType apartmentType) {
            List<Apartment> apartments = apartmentRepository.findByApartmentType(apartmentType);
        return apartments;
    }

    @Override
    public List<Short> getAllIds() {
        // Devuelve una lista de todos los IDs de los apartmentos
        return apartmentRepository.findAllIds();
    }
}
