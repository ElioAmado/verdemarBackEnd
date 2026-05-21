package com.verdemar.domain.price;

import com.verdemar.domain.apartment.Apartment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PRICE")
@Data
@IdClass(PriceId.class)
@NoArgsConstructor
@AllArgsConstructor
public class Price {

  @Id
  @ManyToOne
  @JoinColumn(name = "apartment_id", nullable = false)
  private Apartment apartment;

  @Id private LocalDate date;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal price;
}
