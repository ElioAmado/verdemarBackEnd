package com.verdemar.acceptance;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.verdemar.csvReader.BookingCSVReader;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.service.booking.BookingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class FullReserveTest {

    private static final Logger log = LoggerFactory.getLogger(FullReserveTest.class);

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingCSVReader bookingCSVReader;

    @Test
    public void createExamplesBooking() throws Exception {
        List<BookingDto> bookingsDTO = bookingCSVReader.bookingCSVReader("csv/bookings.csv");

        if (bookingsDTO.isEmpty()) {
            throw new RuntimeException("No bookings found in CSV");
        }

        for (BookingDto bookingDto : bookingsDTO) {
            try {
                bookingService.createBooking(bookingDto);
                log.info("✅ Created booking: {}", bookingDto);
            } catch (IllegalArgumentException e) {
                log.warn("⚠️ Invalid booking: {}", e.getMessage());
                continue;
            }
        }
    }

    @Test
    public void testBookingCSVReader() {
        List<BookingDto> bookingsDTO = bookingCSVReader.bookingCSVReader("csv/bookings.csv");
        if (bookingsDTO.isEmpty()) {
            throw new RuntimeException("No bookings found in CSV");
        }
        for (BookingDto booking : bookingsDTO) {
            System.out.println(booking);
        }
    }
}
