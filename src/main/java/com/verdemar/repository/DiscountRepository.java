package com.verdemar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.verdemar.domain.discount.Discount;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
