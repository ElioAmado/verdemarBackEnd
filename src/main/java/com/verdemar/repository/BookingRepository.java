package com.verdemar.repository;

import com.verdemar.domain.Booking;
import com.verdemar.domain.dto.BookingDateRange;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT new com.verdemar.domain.dto.BookingDateRange(b.startDate, b.endDate) FROM Booking b WHERE b.apartment.id = :apartmentId")
    List<BookingDateRange> findAllDatesByApartment(@Param("apartmentId") Short apartmentId);

    @Query("SELECT b FROM Booking b WHERE b.startDate < :endDate AND b.endDate > :startDate")
    List<Booking> findByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    // Aquí puedes agregar métodos personalizados si lo necesitas
}
