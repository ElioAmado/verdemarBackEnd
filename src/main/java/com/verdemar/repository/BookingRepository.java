package com.verdemar.repository;

import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.dto.BookingDateRange;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  // Encuentra todas las fechas de reserva para un apartmento específico
  @Query(
      "SELECT new com.verdemar.domain.dto.BookingDateRange(b.startDate, b.endDate) FROM Booking b WHERE b.apartment.id = :apartmentId")
  List<BookingDateRange> findAllDatesByApartment(@Param("apartmentId") Integer apartmentId);
  
  // Encuentra reservas que se solapan con un rango de fechas dado
  @Query("SELECT b FROM Booking b WHERE b.startDate < :endDate AND b.endDate > :startDate")
  List<Booking> findByDateRange(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  @Query("""
      SELECT b
      FROM Booking b
      WHERE b.apartment.id = :apartmentId
        AND (
          (MONTH(b.startDate) = :month AND YEAR(b.startDate) = :year)
          OR (MONTH(b.endDate) = :month AND YEAR(b.endDate) = :year)
        )
      ORDER BY b.startDate
      """)
  List<Booking> findBookingsByApartmentAndMonth(
      @Param("apartmentId") Integer apartmentId,
      @Param("month") int month,
      @Param("year") int year);

}
