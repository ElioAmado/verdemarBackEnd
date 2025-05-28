package com.verdemar.service;

import com.verdemar.domain.Booking;
import com.verdemar.domain.dto.BookingDto;
import com.verdemar.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Integer id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Integer id, Booking booking) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        booking.setId(id); // Asegura que se mantiene el mismo ID
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Integer id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Booking createBooking(BookingDto dto) {
        Booking booking = modelMapper.map(dto, Booking.class);
        return bookingRepository.save(booking);
    }


}
