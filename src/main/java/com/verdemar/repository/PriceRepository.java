package com.verdemar.repository;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceId;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
}
