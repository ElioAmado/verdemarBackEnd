package com.verdemar.verdemar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verdemar.verdemar.domain.Apartment;

public interface ApartmentRepository extends JpaRepository<Apartment, Short> {
    // Custom query methods can be defined here if needed
    // For example, you can add methods to find apartments by specific criteria
    // or to perform complex queries using JPQL or native SQL.
    
}
