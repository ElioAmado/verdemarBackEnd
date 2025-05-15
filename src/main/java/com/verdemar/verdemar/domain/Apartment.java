package com.verdemar.verdemar.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.verdemar.verdemar.domain.ApartmentType;
import java.util.List;

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
    private ApartmentType apartmentType;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Short capacity;

    @Column(columnDefinition = "TINYINT", nullable = false)
    private Short floor;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;
}
