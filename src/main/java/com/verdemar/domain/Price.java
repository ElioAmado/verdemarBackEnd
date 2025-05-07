package com.verdemar.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "PRICE")
@IdClass(PriceId.class)
public class Price {

    @Id
    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartament apartment;

    @Id
    private LocalDate date;

    private double price;

    // Getters y setters...
}

// Clase auxiliar para clave compuesta
class PriceId implements Serializable {
    private Short apartment;
    private LocalDate date;

    // equals y hashCode
}
