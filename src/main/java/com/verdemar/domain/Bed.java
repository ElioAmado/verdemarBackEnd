package com.verdemar.domain;

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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BED")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bed {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bed_id", columnDefinition = "TINYINT")
  private short id; // Cambiar a byte

  @ManyToOne
  @JoinColumn(name = "apartment_id", nullable = false)
  private Apartment apartment;

  @Column(columnDefinition = "TINYINT", nullable = false)
  private Short amount = 0;

  @Column(columnDefinition = "SMALLINT") // Añadir nota que es cm
  private Short width;

  @Column(columnDefinition = "SMALLINT") // Añadir nota que es cm
  private Short length;

  @Enumerated(EnumType.STRING)
  @Column(name = "bed_type", nullable = false)
  private BedType bedType;

  // Getters y setters...

  public enum BedType {
    SINGLE,
    DOUBLE,
    EXTRA
  }
}
