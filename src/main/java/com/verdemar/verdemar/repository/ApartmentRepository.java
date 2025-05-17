package com.verdemar.verdemar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verdemar.verdemar.domain.Apartment;
import com.verdemar.verdemar.domain.ApartmentType;

public interface ApartmentRepository extends JpaRepository<Apartment, Short> {
    public List<Apartment> findByApartmentType(ApartmentType apartmentType);
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find apartments by specific criteria
    // or to perform complex queries using JPQL or native SQL.
    
}
