package com.verdemar.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "APARTAMENT")
@Data
public class Apartament {

    @Id
    @Column(name = "apartment_id")
    private Short id;

    private Short capacity;
    private Short floor;
    private Short bedrooms;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Price> prices;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    // Getters y setters...
}
