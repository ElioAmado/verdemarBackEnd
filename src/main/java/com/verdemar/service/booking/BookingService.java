package com.verdemar.service.booking;

import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.booking.Booking.Status;
import com.verdemar.domain.booking.BookingChatbotDto;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.booking.BookingInfo;
import com.verdemar.domain.dto.BookingDateRange;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface BookingService {

  List<Booking> getAllBookings();

  public List<BookingDto> getAllBookingsDto();

  BookingDto getBookingById(Long id);

  Booking createBooking(Booking booking);

  BookingDto updateBooking(Long id, BookingDto booking);

  BookingDto createBooking(BookingDto bookingDto);

  Boolean isValidBooking(BookingDto bookingDto);

  BookingDto changeStatusBooking(Long id, Status status);

  void deleteBooking(Long id);

  BigDecimal getTotalPrice(Short apartmentId, LocalDate startDate, LocalDate endDate);

  List<BookingDateRange> getAllDatesByApartment(Short apartmentId);

  List<BookingInfo> getBookingInfosByMounthAndAparment(short apartmentId, int mounth, int year);

  BookingChatbotDto createBookingFromChatbot(BookingChatbotDto dto);

  List<BookingDto> createBookingsFromCSV(String path);
}
