package com.verdemar.repository;

import com.verdemar.domain.Price;
import com.verdemar.domain.PriceId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceRepository extends JpaRepository<Price, PriceId> {
    // Métodos personalizados pueden añadirse aquí si es necesario
}
