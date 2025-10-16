package com.verdemar.domain.discount;

import java.time.LocalDate;
import java.util.List;

import com.verdemar.domain.apartment.Apartment;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Entity
@Data
public class Discount {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private int discount;
    private Boolean isPercentage;
    /**
     * Many-to-many relationship with Apartment.
     * A discount can apply to multiple apartments, and an apartment can have multiple discounts.
     */
    @ManyToMany
    @JoinTable(
        name = "apartment_discount",
        joinColumns = @JoinColumn(name = "discount_id"),
        inverseJoinColumns = @JoinColumn(name = "apartment_id")
    )
    private List<Apartment> apartments;
    
}
