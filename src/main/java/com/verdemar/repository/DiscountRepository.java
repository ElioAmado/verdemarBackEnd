package com.verdemar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verdemar.domain.discount.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findByApartmentsId(Integer apartmentId);
}
