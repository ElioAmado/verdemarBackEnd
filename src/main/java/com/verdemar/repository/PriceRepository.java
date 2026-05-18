package com.verdemar.repository;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceId;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
    List<Price> findByApartmentIdAndDateBetween(
    Integer apartmentId,
    LocalDate startDate,
    LocalDate endDate
);

}
