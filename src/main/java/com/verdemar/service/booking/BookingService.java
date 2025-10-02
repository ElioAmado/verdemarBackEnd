package com.verdemar.service.booking;

import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.dto.BookingDateRange;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<Booking> getAllBookings();

    Booking getBookingById(Long id);

    Booking createBooking(Booking booking);

    Booking updateBooking(Long id, BookingDto booking);

    Booking createBooking(BookingDto bookingDto);

    void deleteBooking(Long id);

    BigDecimal getTotalPrice(Short apartmentId, LocalDate startDate, LocalDate endDate);

    List<BookingDateRange> getAllDatesByApartment(Short apartmentId);
}
