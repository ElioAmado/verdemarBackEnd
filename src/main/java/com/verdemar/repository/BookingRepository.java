package com.verdemar.repository;

import com.verdemar.domain.apartment.ApartmentType;
import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.dto.BookingDateRange;

import java.math.BigDecimal;
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
    
// Encuentra reservas en un rango de fechas y filtrado por tipo de apartamento
// Si apartmentType es null, trae todos ('ALL')
@Query("""
    SELECT b FROM Booking b
    WHERE b.startDate <= :endDate AND b.endDate >= :startDate
      AND (:apartmentType IS NULL OR b.apartment.apartmentType = :apartmentType)
    """)
List<Booking> findBookingsForKPIs(
    @Param("startDate") LocalDate startDate,
    @Param("endDate") LocalDate endDate,
    @Param("apartmentType") ApartmentType apartmentType); // <- Cambiado a ApartmentType

    // Cuenta reservas activas donde el día evaluado cae dentro de la estancia
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 1 AND :targetDate >= b.startDate AND :targetDate < b.endDate")
    int countActiveBookingsByDate(@Param("targetDate") LocalDate targetDate);

    // Calcula el proporcional de ingresos o suma el total asignado a ese día
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.status = 1 AND :targetDate >= b.startDate AND :targetDate < b.endDate")
    BigDecimal calculateRevenueByDate(@Param("targetDate") LocalDate targetDate);
}
