package com.verdemar.repository;

import com.verdemar.domain.Bed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BedRepository extends JpaRepository<Bed, Short> {
  // Aquí puedes agregar métodos personalizados si lo necesitas
}
