package com.verdemar.verdemar.domain;

import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "PRICE")
@Data
@IdClass(PriceId.class)
public class Price {

    @Id
    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartament apartment;

    @Id
    private LocalDate date;

    @Column(nullable = false)
    private double price;

    // Getters y setters...
}

// Clase auxiliar para clave compuesta
class PriceId implements Serializable {
    private Short apartment;
    private LocalDate date;
}
    // equals y hashCode
