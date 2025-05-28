package com.verdemar.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verdemar.controller.BookingController;
import com.verdemar.domain.Apartment;
import com.verdemar.domain.Booking;
import com.verdemar.domain.Client;
import com.verdemar.domain.dto.BookingDto;
import com.verdemar.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Booking sampleBooking;

    @BeforeEach
    void setUp() {
        Client client = new Client();
        client.setId(1);

        Apartment apartment = new Apartment();
        apartment.setId((short) 1);

        sampleBooking = new Booking();
        sampleBooking.setId(1);
        sampleBooking.setClient(client);
        sampleBooking.setApartment(apartment);
        sampleBooking.setStartDate(LocalDate.of(2025, 6, 1));
        sampleBooking.setEndDate(LocalDate.of(2025, 6, 7));
        sampleBooking.setTotalPrice(BigDecimal.valueOf(500));
        sampleBooking.setStatus(Booking.Status.CONFIRMED);
        sampleBooking.setNotes("Test note");
    }

    @Test
    void testGetAllBookings() throws Exception {
        Mockito.when(bookingService.getAllBookings()).thenReturn(List.of(sampleBooking));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleBooking.getId()))
                .andExpect(jsonPath("$[0].notes").value("Test note"));
    }

    @Test
    void testCreateBooking() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setClientId(sampleBooking.getClient().getId());
        bookingDto.setApartmentId(sampleBooking.getApartment().getId());
        bookingDto.setStartDate(sampleBooking.getStartDate());
        bookingDto.setEndDate(sampleBooking.getEndDate());
        bookingDto.setNotes(sampleBooking.getNotes());

        Mockito.when(bookingService.createBooking(Mockito.any(BookingDto.class)))
            .thenReturn(sampleBooking);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sampleBooking.getId()));
    }

    @Test
    void testGetBookingById() throws Exception {
        Mockito.when(bookingService.getBookingById(1)).thenReturn(sampleBooking);

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testUpdateBooking() throws Exception {
        sampleBooking.setNotes("Updated note");

        Mockito.when(bookingService.updateBooking(Mockito.eq(1), Mockito.any(Booking.class)))
                .thenReturn(sampleBooking);

        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("Updated note"));
    }

    @Test
    void testDeleteBooking() throws Exception {
        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());
    }
}
