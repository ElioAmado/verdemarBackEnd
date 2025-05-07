package com.verdemar.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "BED")
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private Short id;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartament apartment;

    private Short amount;
    private Short width;
    private Short length;

    @Enumerated(EnumType.STRING)
    @Column(name = "bed_type")
    private BedType bedType;

    // Getters y setters...

    public enum BedType {
        SINGLE, DOUBLE, EXTRA
    }
}
