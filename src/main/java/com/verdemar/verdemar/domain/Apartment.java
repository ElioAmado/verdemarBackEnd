package com.verdemar.verdemar.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "APARTMENT")
@Data
public class Apartment {

    public enum ApartmentType {
        ONE_BED_ROOM, TWO_BED_ROOM
    }

    @Id
    @Column(name = "apartment_id", columnDefinition = "TINYINT")
    private Short id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ApartmentType apartmentType;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Short capacity;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Short floor;

    @Column(columnDefinition = "TINYINT")
    private Short bedrooms;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;
}
