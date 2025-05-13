package com.verdemar.verdemar.service;

import com.verdemar.verdemar.domain.Apartament;
import com.verdemar.verdemar.repository.ApartamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApatamentServiceImpl implements ApartamentService {

    private final ApartamentRepository apartamentRepository;

    @Autowired
    public ApatamentServiceImpl(ApartamentRepository apartamentRepository) {
        this.apartamentRepository = apartamentRepository;
    }

    @Override
    public List<Apartament> getAllApartments() {
        // Devuelve todos los apartamentos
        return apartamentRepository.findAll();
    }

    @Override
    public Apartament getApartmentById(Short id) {
        // Obtiene un apartamento por ID
        Optional<Apartament> apartment = apartamentRepository.findById(id);
        return apartment.orElseThrow(() -> new RuntimeException("Apartment not found with id: " + id));
    }

    @Override
    public Apartament createApartment(Apartament apartment) {
        // Crea un nuevo apartamento
        return apartamentRepository.save(apartment);
    }

    @Override
    public Apartament updateApartment(Short id, Apartament apartment) {
        // Verifica si el apartamento existe antes de actualizar
        if (!apartamentRepository.existsById(id)) {
            throw new RuntimeException("Apartment not found with id: " + id);
        }
        apartment.setId(id); // Aseguramos que se mantiene el mismo ID
        return apartamentRepository.save(apartment);
    }

    @Override
    public void deleteApartment(Short id) {
        // Elimina un apartamento por ID
        if (!apartamentRepository.existsById(id)) {
            throw new RuntimeException("Apartment not found with id: " + id);
        }
        apartamentRepository.deleteById(id);
    }
}
