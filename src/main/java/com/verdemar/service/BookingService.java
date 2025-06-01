package com.verdemar.service;

import com.verdemar.domain.Booking;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.domain.dto.BookingDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    Booking createBooking(Booking booking);

    Booking updateBooking(Long id, BookingDto booking);

    Booking createBooking(BookingDto bookingDto);

    void deleteBooking(Long id);

    double getTotalPrice(Short apartmentId, LocalDate startDate, LocalDate endDate);

    List<BookingDateRange> getAllDatesByApartment(Short apartmentId);
}
