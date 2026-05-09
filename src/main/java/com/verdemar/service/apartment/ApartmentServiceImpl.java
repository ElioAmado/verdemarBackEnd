package com.verdemar.service.apartment;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.apartment.ApartmentType;
import com.verdemar.domain.dto.ApartmentAvailabilityDTO;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.exception.apartment.ApartmentNotFoundException;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.BookingRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApartmentServiceImpl implements ApartmentService {

  @Autowired private ApartmentRepository apartmentRepository;

  @Autowired private BookingRepository bookingRepository;

  // Devuelve todos los apartmentos
  @Override
  public List<Apartment> getAllApartments() {
    // Devuelve todos los apartmentos
    return apartmentRepository.findAll();
  }

  // Devuelve un apartmento por su ID
  @Override
  public Apartment getApartmentById(Short id) {
    // Obtiene un apartmento por ID
    Optional<Apartment> apartment = apartmentRepository.findById(id);
    return apartment.orElseThrow(() -> new ApartmentNotFoundException(id));
  }

  // Crea un nuevo apartmento
  @Override
  public Apartment createApartment(Apartment apartment) {
    // Crea un nuevo apartmento
    return apartmentRepository.save(apartment);
  }

  // Actualiza un apartmento existente
  @Override
  public Apartment updateApartment(Short id, Apartment apartment) {
    // Verifica si el apartmento existe antes de actualizar
    if (!apartmentRepository.existsById(id)) {
      throw new RuntimeException("Apartment not found with id: " + id);
    }
    apartment.setId(id); // Aseguramos que se mantiene el mismo ID
    return apartmentRepository.save(apartment);
  }

  // Elimina un apartmento por su ID
  @Override
  public void deleteApartment(Short id) {
    // Elimina un apartmento por ID
    if (!apartmentRepository.existsById(id)) {
      throw new RuntimeException("Apartment not found with id: " + id);
    }
    apartmentRepository.deleteById(id);
  }

  // Devuelve todos los tipos de apartmentos
  @Override
  public ApartmentType[] getApartmentTypes() {
    return ApartmentType.values();
  }

  // Devuelve los apartmentos disponibles para un rango de fechas y tipo
  @Override
  public List<Apartment> getAvailableApartments(
      LocalDate startDate, LocalDate endDate, ApartmentType apartmentType) {
    List<Apartment> apartments = apartmentRepository.findByApartmentType(apartmentType);
    return apartments;
  }

  // Devuelve todos los IDs de los apartmentos
  @Override
  public List<Short> getAllIds() {
    // Devuelve una lista de todos los IDs de los apartmentos
    return apartmentRepository.findAllIds();
  }

  // Devuelve la disponibilidad de los apartmentos para un rango de fechas y tipo
  @Override
  public List<ApartmentAvailabilityDTO> getAvailabilityList(
      LocalDate startDate, LocalDate endDate, ApartmentType apartmentType) {

    List<Apartment> apartments = apartmentRepository.findByApartmentType(apartmentType);

    return apartments.stream()
        .map(
            apartment -> {
              List<BookingDateRange> bookings =
                  bookingRepository.findAllDatesByApartment(apartment.getId());
              boolean isAvailable =
                  bookings.stream()
                      .noneMatch(
                          b -> !startDate.isAfter(b.getTo()) && !endDate.isBefore(b.getFrom()));
              return new ApartmentAvailabilityDTO(apartment, isAvailable);
            })
        .collect(Collectors.toList());
  }

  @Override
  public List<ApartmentAvailabilityDTO> getAvailabilityListWithoutType(
      LocalDate startDate, LocalDate endDate) {

    List<Apartment> apartments = apartmentRepository.findAll();

    return apartments.stream()
        .map(
            apartment -> {
              List<BookingDateRange> bookings =
                  bookingRepository.findAllDatesByApartment(apartment.getId());
              boolean isAvailable =
                  bookings.stream()
                      .noneMatch(
                          b -> !startDate.isAfter(b.getTo()) && !endDate.isBefore(b.getFrom()));
              return new ApartmentAvailabilityDTO(apartment, isAvailable);
            })
        .collect(Collectors.toList());
  }
}
