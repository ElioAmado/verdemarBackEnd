package com.verdemar.domain.apartment;

import com.verdemar.domain.Bed;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "APARTMENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apartment {

  @Id
  @Column(name = "apartment_id", columnDefinition = "TINYINT")
  private Short id;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", nullable = false)
  private ApartmentType apartmentType; // Cambiar a type

  @Column(columnDefinition = "TINYINT", nullable = false)
  private Short capacity; // Cambiar a byte

  @Column(columnDefinition = "TINYINT", nullable = false)
  private Short floor; // Cambiar a byte

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Bed> beds;
}
