package com.verdemar.repository;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.apartment.ApartmentType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ApartmentRepository extends JpaRepository<Apartment, Short> {
  public List<Apartment> findByApartmentType(ApartmentType apartmentType);

  @Query("SELECT a.id FROM Apartment a")
  List<Short> findAllIds();

  // Custom query methods can be defined here if needed
  // For example, you can add methods to find apartments by specific criteria
  // or to perform complex queries using JPQL or native SQL.

}
