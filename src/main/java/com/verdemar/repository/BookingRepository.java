package com.verdemar.repository;

import com.verdemar.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    // Aquí puedes agregar métodos personalizados si lo necesitas
}
