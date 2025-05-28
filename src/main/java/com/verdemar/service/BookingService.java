package com.verdemar.service;

import com.verdemar.domain.Booking;
import com.verdemar.domain.dto.BookingDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<Booking> getAllBookings();

    Booking getBookingById(Integer id);

    Booking createBooking(Booking booking);

    Booking updateBooking(Integer id, Booking booking);

    Booking createBooking(BookingDto bookingDto);

    void deleteBooking(Integer id);

    double getTotalPrice(short apartmentId, LocalDate startDate, LocalDate endDate);
}
