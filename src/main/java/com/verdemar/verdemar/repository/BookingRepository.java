package com.verdemar.verdemar.repository;

import com.verdemar.verdemar.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Aquí puedes agregar métodos personalizados si lo necesitas
}
