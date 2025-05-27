package com.verdemar.verdemar.controller;

import com.verdemar.verdemar.domain.Booking;
import com.verdemar.verdemar.domain.dto.BookingDto;
import com.verdemar.verdemar.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Booking> getBookingById(@PathVariable Integer id) {
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
    public ResponseEntity<Booking> updateBooking(@PathVariable Integer id, @RequestBody Booking booking) {
        Booking updated = bookingService.updateBooking(id, booking);
        return ResponseEntity.ok(updated);
    }

    // Eliminar una reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Integer id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }
}
