package com.verdemar.controller;

import com.verdemar.domain.booking.BookingDto;
import com.verdemar.service.booking.BookingService; // Asegúrate de tener estos métodos en tu capa service o adáptalos
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*") // Ajusta según tus necesidades de CORS
public class DashboardController {

    @Autowired
    private BookingService bookingService;

    // GET /api/dashboard/kpis?start_date=...&end_date=...&apartment_type=...
    @GetMapping("/kpis")
        public ResponseEntity<Map<String, Object>> getDashboardKPIs(
                @RequestParam(name = "start_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                @RequestParam(name = "end_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                @RequestParam(name = "apartment_type", defaultValue = "ALL") String apartmentType) {

            // Spring ya parseó las fechas automáticamente a LocalDate de forma segura
            Map<String, Object> kpis = bookingService.getKPIs(startDate, endDate, apartmentType);
            return ResponseEntity.ok(kpis);
        }

    // GET /api/dashboard/occupancy?start_date=...&end_date=...&apartment_type=...
    @GetMapping("/occupancy")
    public ResponseEntity<Map<String, Object>> getOccupancyData(
            @RequestParam("start_date") String startDate,
            @RequestParam("end_date") String endDate,
            @RequestParam(value = "apartment_type", required = false, defaultValue = "ALL") String apartmentType) {

        Map<String, Object> occupancyData = bookingService.getOccupancyData(LocalDate.parse(startDate),
                LocalDate.parse(endDate), apartmentType);
        return ResponseEntity.ok(occupancyData);
    }


    // GET /api/dashboard/recent?limit=10
    @GetMapping("/recent")
    public ResponseEntity<List<BookingDto>> getRecentBookings(
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit) {

        List<BookingDto> recent = bookingService.getRecentBookings(limit);
        return ResponseEntity.ok(recent);
    }

    // GET /api/dashboard/apartments
    @GetMapping("/apartments")
    public ResponseEntity<List<Map<String, Object>>> getApartmentsSummary() {
        // Devuelve la lista resumida de apartamentos activos
        return ResponseEntity.ok(bookingService.getApartmentsSummary());
    }
}