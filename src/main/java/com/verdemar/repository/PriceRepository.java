package com.verdemar.repository;

import com.verdemar.domain.price.Price;
import com.verdemar.domain.price.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
  // Métodos personalizados pueden añadirse aquí si es necesario
}
