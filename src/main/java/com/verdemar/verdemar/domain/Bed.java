package com.verdemar.verdemar.domain;

import lombok.Data;
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

@Entity
@Table(name = "BED")
@Data
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id", columnDefinition = "TINYINT")
    private Short id;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartment apartment;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Short amount = 0;

    @Column(columnDefinition = "TINYINT")
    private Short width;

    @Column(columnDefinition = "TINYINT")
    private Short length;

    @Enumerated(EnumType.STRING)
    @Column(name = "bed_type", nullable = false)
    private BedType bedType;

    // Getters y setters...

    public enum BedType {
        SINGLE, DOUBLE, EXTRA
    }
}

