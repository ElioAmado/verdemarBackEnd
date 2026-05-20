package com.verdemar.domain.booking;

import com.verdemar.domain.Client;
import com.verdemar.domain.apartment.Apartment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOOKING")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "booking_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  private byte guests;

  @ManyToOne
  @JoinColumn(name = "apartment_id", nullable = false)
  private Apartment apartment;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "total_price", precision = 10, scale = 2)
  private BigDecimal totalPrice;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "status", columnDefinition = "TINYINT", nullable = false)
  private Status status = Status.PENDING;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  // @PrePersist
  // protected void onCreate() {
  //     createdAt = updatedAt = LocalDateTime.now();
  // }

  @PreUpdate
  protected void onUpdate() {
      updatedAt = LocalDateTime.now();
  }

  @Column(name = "method_payment", length = 50)
  private String methodPayment;

  @Column(columnDefinition = "TEXT")
  private String notes;

  public enum Status {
    PENDING, // 0 Creando la reserva
    CONFIRMED, // 1 Reserva ya pagada
    CANCELLED, // 2 Reserva cancelada
    COMPLETED // 3 Reserva finalizada
  }

  // Getters y setters...
}
