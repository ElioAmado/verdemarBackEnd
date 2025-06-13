package com.verdemar.service.booking;

import com.verdemar.domain.Booking;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.domain.dto.BookingDto;
import com.verdemar.repository.BookingRepository;
import com.verdemar.service.price.PriceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PriceService priceService;

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking getBookingById(Long id) {
        Optional<Booking> booking = bookingRepository.findById(id);
        return booking.orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    }

    @Override
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Long id, BookingDto bookingDto) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingDto.setId(id); // Asegura que se mantiene el mismo ID
        Booking booking = modelMapper.map(bookingDto, Booking.class);
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found with id: " + id);
        }
        bookingRepository.deleteById(id);
    }



    @Override
    public Booking createBooking(BookingDto dto) {
        Booking booking = modelMapper.map(dto, Booking.class);
        return bookingRepository.save(booking);
    }

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

    @Override
    public List<BookingDateRange> getAllDatesByApartment(Short apartmentId) {
        return bookingRepository.findAllDatesByApartment(apartmentId);
    }




}
