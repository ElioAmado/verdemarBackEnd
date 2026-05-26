package com.verdemar.domain.booking;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO exclusivo para el chatbot de Amazon Lex.
 * Solo contiene los campos que Lex puede recoger y los que
 * el usuario necesita ver en la respuesta del bot.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingChatbotDto {

    // ── Slots que Lex recoge del usuario ──────────────────────────────

    /** ID del apartamento (slot: apartmentId) */
    private Integer apartmentId;

    /** Fecha de entrada en formato ISO: 2025-06-15 (slot: startDate) */
    private LocalDate startDate;

    /** Fecha de salida en formato ISO: 2025-06-22 (slot: endDate) */
    private LocalDate endDate;

    /** Número de huéspedes (slot: guests) */
    private Byte guests;

    /** Método de pago opcional: tarjeta, transferencia… (slot: methodPayment) */
    private String methodPayment;

    /** Notas adicionales opcionales (slot: notes) */
    private String notes;

    // ── Campos calculados / devueltos al bot tras crear la reserva ────

    /** ID de la reserva creada — para mostrar la referencia al usuario */
    private Long bookingId;

    /** Precio total calculado por el backend */
    private BigDecimal totalPrice;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
      createdAt = updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
      updatedAt = LocalDateTime.now();
  }

    /** Estado de la reserva tras crearla (siempre PENDING al inicio) */
    private Booking.Status status;
}