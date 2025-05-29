package com.verdemar.controller;

import com.verdemar.domain.Booking;
import com.verdemar.domain.Price;
import com.verdemar.domain.dto.BookingDto;
import com.verdemar.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Obtener una reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Booking booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    // Crear una nueva reserva (usando BookingDto)
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDto dto) {
        Booking created = bookingService.createBooking(dto);
        return ResponseEntity.ok(created);
    }

    // Actualizar una reserva
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        Booking updated = bookingService.updateBooking(id, booking);
        return ResponseEntity.ok(updated);
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Double> checkPrice(
            @RequestParam("apartmentId") short apartmentId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        double totalPrice = bookingService.getTotalPrice(apartmentId, startDate, endDate);
        return ResponseEntity.ok(totalPrice);
    }

    
}
