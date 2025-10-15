package com.verdemar.controller;

import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.service.booking.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

  @Autowired private BookingService bookingService;

  // Gets

  // Obtener todas las reservas
  @GetMapping
  public ResponseEntity<List<BookingDto>> getAllBookings() {
    List<BookingDto> bookings = bookingService.getAllBookings();
    return ResponseEntity.ok(bookings);
  }

  // Obtener una reserva por ID
  @GetMapping("/{id}")
  public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
    BookingDto booking = bookingService.getBookingById(id);
    return ResponseEntity.ok(booking);
  }

  @GetMapping("/check")
  public ResponseEntity<BigDecimal> checkPrice(
      @RequestParam("apartmentId") short apartmentId,
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    BigDecimal totalPrice = bookingService.getTotalPrice(apartmentId, startDate, endDate);
    return ResponseEntity.ok(totalPrice);
  }

  @GetMapping("/getDates/{apartmentId}")
  public ResponseEntity<List<BookingDateRange>> getDates(
      @PathVariable("apartmentId") short apartmentId) {

    List<BookingDateRange> dates = bookingService.getAllDatesByApartment(apartmentId);
    return ResponseEntity.ok(dates);
  }

  // Posts

  // Crear una nueva reserva (usando BookingDto)
  @PostMapping
  public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto dto) {
    BookingDto created = bookingService.createBooking(dto);
    return ResponseEntity.ok(created);
  }

  // Puts

  // Actualizar una reserva
  @PutMapping("/{id}")
  public ResponseEntity<BookingDto> updateBooking(
      @PathVariable Long id, @RequestBody BookingDto booking) {
    BookingDto updated = bookingService.updateBooking(id, booking);
    return ResponseEntity.ok(updated);
  }

  // Deletes

  // Eliminar una reserva
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
    bookingService.deleteBooking(id);
    return ResponseEntity.noContent().build();
  }
}
