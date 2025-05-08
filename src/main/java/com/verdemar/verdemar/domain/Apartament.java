package com.verdemar.verdemar.domain;

import jakarta.persistence.*;
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
import java.util.List;

@Entity
@Table(name = "APARTAMENT")
@Data
public class Apartament {

    @Id
    @Column(name = "apartment_id", columnDefinition = "TINYINT")
    private Short id;

    @Column(columnDefinition = "TINYINT")
    private Short capacity;

    @Column(columnDefinition = "TINYINT")
    private Short floor;

    @Column(columnDefinition = "TINYINT")
    private Short bedrooms;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bed> beds;
}
