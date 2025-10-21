package com.verdemar.unit.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.verdemar.controller.BookingController;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.service.booking.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Unit tests for BookingController using MockMvc. */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(BookingController.class)
class BookingControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BookingService bookingService;

  @Test
  @DisplayName("GET /api/bookings should return all bookings")
  void testGetAllBookings() throws Exception {
    BookingDto dto = new BookingDto();
    dto.setId(1L);
    dto.setGuests((byte) 2);
    dto.setNotes("Reserva test");

    Mockito.when(bookingService.getAllBookings()).thenReturn(List.of());

    mockMvc
        .perform(get("/api/booking"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].guests").value(2))
        .andExpect(jsonPath("$[0].notes").value("Reserva test"));
  }

  @Test
  @DisplayName("GET /api/bookings/{id} should return booking by id")
  void testGetBookingById() throws Exception {
    BookingDto dto = new BookingDto();
    dto.setId(2L);
    dto.setGuests((byte) 3);

    Mockito.when(bookingService.getBookingById(2L)).thenReturn(dto);

    mockMvc
        .perform(get("/api/booking/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.guests").value(3));
  }

  @Test
  @DisplayName("POST /api/bookings should create booking")
  void testCreateBooking() throws Exception {
    BookingDto dto = new BookingDto();
    dto.setId(3L);
    dto.setGuests((byte) 4);

    Mockito.when(bookingService.createBooking(any(BookingDto.class))).thenReturn(dto);

    mockMvc
        .perform(
            post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"guests\":4,\"notes\":\"Nueva reserva\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.guests").value(4));
  }

  @Test
  @DisplayName("PUT /api/bookings/{id} should update booking")
  void testUpdateBooking() throws Exception {
    BookingDto dto = new BookingDto();
    dto.setId(4L);
    dto.setGuests((byte) 5);
    dto.setNotes("Actualizada");

    Mockito.when(bookingService.updateBooking(eq(4L), any(BookingDto.class))).thenReturn(dto);

    mockMvc
        .perform(
            put("/api/booking/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"guests\":5,\"notes\":\"Actualizada\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(4))
        .andExpect(jsonPath("$.guests").value(5))
        .andExpect(jsonPath("$.notes").value("Actualizada"));
  }

  @Test
  @DisplayName("DELETE /api/bookings/{id} should delete booking")
  void testDeleteBooking() throws Exception {
    mockMvc.perform(delete("/api/booking/5")).andExpect(status().isNoContent());

    Mockito.verify(bookingService).deleteBooking(5L);
  }

  @Test
  @DisplayName("GET /api/bookings/check should return total price")
  void testCheckPrice() throws Exception {
    Mockito.when(
            bookingService.getTotalPrice(
                eq((short) 1),
                eq(LocalDate.parse("2025-10-01")),
                eq(LocalDate.parse("2025-10-05"))))
        .thenReturn(BigDecimal.valueOf(500));

    mockMvc
        .perform(
            get("/api/booking/check")
                .param("apartmentId", "1")
                .param("startDate", "2025-10-01")
                .param("endDate", "2025-10-05"))
        .andExpect(status().isOk())
        .andExpect(content().string("500"));
  }

  @Test
  @DisplayName("GET /api/booking/getDates/{apartmentId} should return booking date ranges")
  void testGetDates() throws Exception {
    BookingDateRange range =
        new BookingDateRange(LocalDate.parse("2025-10-01"), LocalDate.parse("2025-10-10"));

    Mockito.when(bookingService.getAllDatesByApartment((short) 2)).thenReturn(List.of(range));

    mockMvc
        .perform(get("/api/bookings/getDates/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].startDate").value("2025-10-01"))
        .andExpect(jsonPath("$[0].endDate").value("2025-10-10"));
  }
}
