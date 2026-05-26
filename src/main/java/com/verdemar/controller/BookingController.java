package com.verdemar.controller;

import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.booking.BookingChatbotDto;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.booking.BookingInfo;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.service.booking.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

  @Autowired private BookingService bookingService;

  @Value("${urlspringboot}")
  private String url;
  // Gets

  // Obtener todas las reservas
  @GetMapping() // O la ruta que estés utilizando
  public ResponseEntity<Page<Booking>> getAllBookings(
      @PageableDefault(size = 10, page = 0) Pageable pageable) {

    Page<Booking> bookings = bookingService.getAllBookings(pageable);
    return ResponseEntity.ok(bookings);
  }

  // Obtener todas las reservas (DTO)
  @GetMapping("/dto")
  public ResponseEntity<List<BookingDto>> getAllBookingsDto() {
    List<BookingDto> bookings = bookingService.getAllBookingsDto();
    return ResponseEntity.ok(bookings);
  }

  // Obtener una reserva por ID
  @GetMapping("/{id}")
  public ResponseEntity<BookingDto> getBookingById(@PathVariable Long id) {
    BookingDto booking = bookingService.getBookingById(id);
    return ResponseEntity.ok(booking);
  }

  @GetMapping("/check")
  public ResponseEntity<BigDecimal> checkPrice(
      @RequestParam("apartmentId") Integer apartmentId,
      @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

    BigDecimal totalPrice = bookingService.getTotalPrice(apartmentId, startDate, endDate);
    return ResponseEntity.ok(totalPrice);
  }

  @GetMapping("/getDates/{apartmentId}")
  public ResponseEntity<List<BookingDateRange>> getDates(
      @PathVariable("apartmentId") Integer apartmentId) {

    List<BookingDateRange> dates = bookingService.getAllDatesByApartment(apartmentId);
    return ResponseEntity.ok(dates);
  }

  // Devuelve las reservas de un mes
  @GetMapping("/getDates/{apartmentId}/{mounth}/{year}")
  public ResponseEntity<List<BookingInfo>> getDatesByMounth(
    @PathVariable("apartmentId") Integer apartmentId,
    @PathVariable("mounth") int mounth,
    @PathVariable("year") int year) {
      return ResponseEntity.ok(bookingService.getBookingInfosByMounthAndAparment(apartmentId, mounth, year));
    }


  // Posts

  // Crear una nueva reserva (usando BookingDto)
  @PostMapping
  public ResponseEntity<BookingDto> createBooking(@RequestBody BookingDto dto) {
    BookingDto created = bookingService.createBooking(dto);
    return ResponseEntity.ok(created);
  }

  // Puts

  // Actualizar una reserva
  @PutMapping("/{id}")
  public ResponseEntity<BookingDto> updateBooking(
      @PathVariable Long id, @RequestBody BookingDto booking) {
    BookingDto updated = bookingService.updateBooking(id, booking);
    return ResponseEntity.ok(updated);
  }

  // Deletes

  // Eliminar una reserva
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
    bookingService.deleteBooking(id);
    return ResponseEntity.noContent().build();
  }

  // En BookingController.java

/**
 * Endpoint exclusivo para el chatbot de Amazon Lex.
 * Recibe únicamente los campos que el bot puede recoger y
 * devuelve solo lo que el bot necesita mostrar al usuario.
 */
@PostMapping("/chatbot")
public ResponseEntity<BookingChatbotDto> createBookingFromChatbot(
        @RequestBody BookingChatbotDto dto) {
    BookingChatbotDto created = bookingService.createBookingFromChatbot(dto);
    return ResponseEntity.ok(created);
}

private String getSlotValue(Map<String, Object> slots, String slotName) {
  if (slots == null || !slots.containsKey(slotName))
    return null;

  // Estructura real de Lex v2:
  // slots → { "apartmentId": { "value": { "interpretedValue": "3" } } }
  Map<String, Object> slot = (Map<String, Object>) slots.get(slotName);
  if (slot == null)
    return null;

  Map<String, Object> value = (Map<String, Object>) slot.get("value");
  if (value == null)
    return null;

  return (String) value.get("interpretedValue");
}

private Map<String, Object> buildLexResponse(String message) {
  return Map.of(
      "sessionState", Map.of(
          "dialogAction", Map.of("type", "Close"),
          "intent", Map.of(
              "name", "ReservarApartamento",
              "state", "Fulfilled")),
      "messages", List.of(
          Map.of(
              "contentType", "PlainText",
              "content", message)));
}

private Map<String, Object> extractSlots(Map<String, Object> lexEvent) {
  // Estructura real del payload Lex v2:
  // { "sessionState": { "intent": { "slots": { ... } } } }
  Map<String, Object> sessionState = (Map<String, Object>) lexEvent.get("sessionState");
  Map<String, Object> intent = (Map<String, Object>) sessionState.get("intent");
  return (Map<String, Object>) intent.get("slots");
}

@PostMapping("/lex-webhook")
public ResponseEntity<Map<String, Object>> lexWebhook(@RequestBody Map<String, Object> lexEvent) {

  // 1. Extraer slots del payload de Lex v2
  Map<String, Object> slots = extractSlots(lexEvent);

  // 2. Construir tu DTO
  BookingChatbotDto dto = new BookingChatbotDto();
  dto.setApartmentId(1);
  dto.setStartDate(LocalDate.parse(getSlotValue(slots, "startDate")));
  dto.setEndDate(LocalDate.parse(getSlotValue(slots, "endDate")));
  dto.setGuests(Byte.parseByte(getSlotValue(slots, "guests")));
  dto.setMethodPayment(null);

  // 3. Llamar al servicio
  BookingChatbotDto created = bookingService.createBookingFromChatbot(dto);

  // 4. Devolver respuesta en formato Lex v2
  return ResponseEntity.ok(buildLexResponse(
      "Entre a este enlace para realizar el pago: " + url + "/booking/" + created.getBookingId() +
          " Total: " + created.getTotalPrice() + "€. Estado: " + created.getStatus()));
}
}
