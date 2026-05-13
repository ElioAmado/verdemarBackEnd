package com.verdemar.service.booking;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.booking.BookingChatbotDto;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.booking.BookingInfo;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.exception.apartment.ApartmentNotFoundException;
import com.verdemar.exception.booking.BookingException;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.BookingRepository;
import com.verdemar.service.price.PriceService;

import jakarta.persistence.EntityNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

  @Autowired private ModelMapper modelMapper;

  @Autowired private BookingRepository bookingRepository;

  @Autowired private PriceService priceService;

  @Autowired private ApartmentRepository apartmentRepository;

  // Devuelve todos los bookings
  @Override
  public List<Booking> getAllBookings() {
    List<Booking> bookings = bookingRepository.findAll();

    return bookings;
  }

  @Override
  public List<BookingDto> getAllBookingsDto() {
    List<Booking> bookings = bookingRepository.findAll();

    return bookings.stream().map(booking -> modelMapper.map(booking, BookingDto.class)).toList();
  }

  // Devuelve un booking por su ID
  @Override
  public BookingDto getBookingById(Long id) {
    Optional<Booking> booking = bookingRepository.findById(id);

    booking.orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    return modelMapper.map(booking.get(), BookingDto.class);
  }

  // Crea un nuevo booking
  @Override
  public Booking createBooking(Booking booking) {
    return bookingRepository.save(booking);
  }

  // Crea un nuevo booking (usando BookingDto)
  @Override
  public BookingDto createBooking(BookingDto dto) {
    try {
      isValidBooking(dto);
    } catch (Exception e) {
      throw new BookingException("Invalid booking: dates overlap or apartment does not exist, cause:" + e.getMessage());
    }

    // Map DTO to entity
    Booking booking = modelMapper.map(dto, Booking.class);

    // Save entity; createdAt y updatedAt se asignarán automáticamente
    Booking savedBooking = bookingRepository.save(booking);

    // Map back to DTO so it includes the generated ID
    return modelMapper.map(savedBooking, BookingDto.class);
  }

  // Cambia el estado de una reserva
  @Override
  public BookingDto changeStatusBooking(Long id, Booking.Status status) {
    Optional<Booking> bookingOpt = bookingRepository.findById(id);
    if (bookingOpt.isEmpty()) {
      throw new RuntimeException("Booking not found with id: " + id);
    }
    Booking booking = bookingOpt.get();
    booking.setStatus(status);
    bookingRepository.save(booking);

    return modelMapper.map(booking, BookingDto.class);
  }

  // Actualiza un booking existente
  @Override
  public BookingDto updateBooking(Long id, BookingDto bookingDto) {
    if (!bookingRepository.existsById(id)) {
      throw new RuntimeException("Booking not found with id: " + id);
    }
    bookingDto.setId(id); // Asegura que se mantiene el mismo ID
    Booking booking = modelMapper.map(bookingDto, Booking.class);
    bookingRepository.save(booking);

    return bookingDto;
  }

  // Elimina un booking por su ID
  @Override
  public void deleteBooking(Long id) {
    if (!bookingRepository.existsById(id)) {
      throw new RuntimeException("Booking not found with id: " + id);
    }
    bookingRepository.deleteById(id);
  }


  // Verifica si una reserva es válida (comprueba que no se solapa con otras reservas)
@Override
public Boolean isValidBooking(BookingDto bookingDto) {
    apartmentRepository.findById(bookingDto.getApartmentId())
        .orElseThrow(() -> new ApartmentNotFoundException(
            bookingDto.getApartmentId()));

    List<BookingDateRange> existingBookings =
        bookingRepository.findAllDatesByApartment(bookingDto.getApartmentId());

    for (BookingDateRange existingBooking : existingBookings) {
        boolean overlaps =
            !(bookingDto.getEndDate().isBefore(existingBooking.getFrom()) ||
              bookingDto.getStartDate().isAfter(existingBooking.getTo()));

        if (overlaps) {
            throw new BookingException(
                "The requested booking dates overlap with an existing booking from "
                + existingBooking.getFrom() + " to " + existingBooking.getTo() + ".");
        }
    }

    return true;
}


  // Calcula el precio total de una reserva
  @Override
  public BigDecimal getTotalPrice(Short apartmentId, LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must be before end date");
    }

    BigDecimal totalPrice = BigDecimal.ZERO;
    LocalDate currentDate = startDate;

    while (currentDate.isBefore(endDate)) {
      // Suponemos que getPriceById nunca devuelve null
      BigDecimal dailyPrice = priceService.getPriceById(apartmentId, currentDate).getPrice();
      if (dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Daily price must be greater than zero");
      }

      totalPrice = totalPrice.add(dailyPrice);
      currentDate = currentDate.plusDays(1);
    }

    if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Total price must be greater than zero");
    }

    return totalPrice;
  }

  // Devuelve todas las fechas de reserva para un apartamento específico
  @Override
  public List<BookingDateRange> getAllDatesByApartment(Short apartmentId) {
    return bookingRepository.findAllDatesByApartment(apartmentId);
  }

  @Override
  public List<BookingInfo> getBookingInfosByMounthAndAparment(short apartmentId, int mounth, int year) {
    // Obtener bookings desde el repositorio
    List<Booking> bookings = bookingRepository.findBookingsByApartmentAndMonth(apartmentId, mounth, year);

    // Mapear cada Booking a BookingInfo
    List<BookingInfo> bookingsInfo = bookings.stream()
        .map(booking -> modelMapper.map(booking, BookingInfo.class))
        .toList();

    return bookingsInfo;
  }

  // En BookingService.java (o BookingServiceImpl)
@Override
public BookingChatbotDto createBookingFromChatbot(BookingChatbotDto dto) {

    Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
        .orElseThrow(() -> new EntityNotFoundException(
            "Apartamento " + dto.getApartmentId() + " no encontrado"));

    // Calcular precio usando la lógica ya existente
    BigDecimal totalPrice = getTotalPrice(dto.getApartmentId(), dto.getStartDate(), dto.getEndDate());

    Booking booking = new Booking();
    booking.setApartment(apartment);
    booking.setStartDate(dto.getStartDate());
    booking.setEndDate(dto.getEndDate());
    booking.setGuests(dto.getGuests() != null ? dto.getGuests() : 1);
    booking.setTotalPrice(totalPrice);
    booking.setStatus(Booking.Status.PENDING);
    booking.setMethodPayment(dto.getMethodPayment());
    booking.setNotes(dto.getNotes());
    // client queda null — el bot no gestiona login

    Booking saved = bookingRepository.save(booking);

    // Devolver solo lo que el bot necesita
    BookingChatbotDto response = new BookingChatbotDto();
    response.setBookingId(saved.getId());
    response.setApartmentId(dto.getApartmentId());
    response.setStartDate(saved.getStartDate());
    response.setEndDate(saved.getEndDate());
    response.setGuests(saved.getGuests());
    response.setTotalPrice(saved.getTotalPrice());
    response.setStatus(saved.getStatus());
    response.setMethodPayment(saved.getMethodPayment());
    response.setNotes(saved.getNotes());

    return response;
}
}
