package com.verdemar.service.booking;

import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.apartment.ApartmentType;
import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.booking.BookingChatbotDto;
import com.verdemar.domain.booking.BookingDto;
import com.verdemar.domain.booking.BookingInfo;
import com.verdemar.domain.dto.BookingDateRange;
import com.verdemar.exception.apartment.ApartmentNotFoundException;
import com.verdemar.exception.booking.BookingException;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.BookingRepository;
import com.verdemar.service.ml.PredictionMLService;
import com.verdemar.service.price.PriceService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.verdemar.domain.dto.OccupancyDataPointDto;

@Service
public class BookingServiceImpl implements BookingService {

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private PriceService priceService;

  @Autowired
  private ApartmentRepository apartmentRepository;

  @Autowired
  private BookingCSVReader bookingCSVReader;

  @Autowired
  private PredictionMLService predictionMLService;

  // Devuelve todos los bookings
  @Override
  public List<Booking> getAllBookings() {
    List<Booking> bookings = bookingRepository.findAll();

    return bookings;
  }

  @Override
  public Page<Booking> getAllBookings(Pageable pageable) {
    // JpaRepository ya se encarga de todo cuando le pasas el objeto pageable
    Page<Booking> bookings = bookingRepository.findAll(pageable);

    return bookings;
  }

  @Override
  public List<BookingDto> getAllBookingsDto() {
    List<Booking> bookings = bookingRepository.findAll();

    return bookings.stream().map(booking -> modelMapper.map(booking, BookingDto.class)).toList();
  }

  // Devuelve un booking por su ID
  @Override
  public BookingDto getBookingById(Long id) {
    Optional<Booking> booking = bookingRepository.findById(id);

    booking.orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
    return modelMapper.map(booking.get(), BookingDto.class);
  }

  // Crea un nuevo booking
  @Override
  public Booking createBooking(Booking booking) {
    return bookingRepository.save(booking);
  }

  // Crea un nuevo booking (usando BookingDto)
  @Override
  public BookingDto createBooking(BookingDto dto) {
    try {
      isValidBooking(dto);
    } catch (Exception e) {
      throw new BookingException("Invalid booking: dates overlap or apartment does not exist, cause:" + e.getMessage());
    }

    // Map DTO to entity
    Booking booking = modelMapper.map(dto, Booking.class);

    // Save entity; createdAt y updatedAt se asignarán automáticamente
    Booking savedBooking = bookingRepository.save(booking);

    // Map back to DTO so it includes the generated ID
    return modelMapper.map(savedBooking, BookingDto.class);
  }

  // Cambia el estado de una reserva
  @Override
  public BookingDto changeStatusBooking(Long id, Booking.Status status) {
    Optional<Booking> bookingOpt = bookingRepository.findById(id);
    if (bookingOpt.isEmpty()) {
      throw new RuntimeException("Booking not found with id: " + id);
    }
    Booking booking = bookingOpt.get();
    booking.setStatus(status);
    bookingRepository.save(booking);

    return modelMapper.map(booking, BookingDto.class);
  }

  // Actualiza un booking existente
  @Override
  public BookingDto updateBooking(Long id, BookingDto bookingDto) {
    if (!bookingRepository.existsById(id)) {
      throw new RuntimeException("Booking not found with id: " + id);
    }
    bookingDto.setId(id); // Asegura que se mantiene el mismo ID
    Booking booking = modelMapper.map(bookingDto, Booking.class);
    bookingRepository.save(booking);

    return bookingDto;
  }

  // Elimina un booking por su ID
  @Override
  public void deleteBooking(Long id) {
    if (!bookingRepository.existsById(id)) {
      throw new RuntimeException("Booking not found with id: " + id);
    }
    bookingRepository.deleteById(id);
  }

  // Verifica si una reserva es válida (comprueba que no se solapa con otras
  // reservas)
  @Override
  public Boolean isValidBooking(BookingDto bookingDto) {
    apartmentRepository.findById(bookingDto.getApartmentId())
        .orElseThrow(() -> new ApartmentNotFoundException(
            bookingDto.getApartmentId()));

    List<BookingDateRange> existingBookings = bookingRepository.findAllDatesByApartment(bookingDto.getApartmentId());

    for (BookingDateRange existingBooking : existingBookings) {
      boolean overlaps = !(bookingDto.getEndDate().isBefore(existingBooking.getFrom()) ||
          bookingDto.getStartDate().isAfter(existingBooking.getTo()));

      if (overlaps) {
        throw new BookingException(
            "The requested booking dates overlap with an existing booking from "
                + existingBooking.getFrom() + " to " + existingBooking.getTo() + ".");
      }
    }

    return true;
  }

  // Calcula el precio total de una reserva
  @Override
  public BigDecimal getTotalPrice(Integer apartmentId, LocalDate startDate, LocalDate endDate) {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must be before end date");
    }

    BigDecimal totalPrice = BigDecimal.ZERO;
    LocalDate currentDate = startDate;

    while (currentDate.isBefore(endDate)) {
      // Suponemos que getPriceById nunca devuelve null
      BigDecimal dailyPrice = priceService.getPriceById(apartmentId, currentDate).getPrice();
      if (dailyPrice.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Daily price must be greater than zero");
      }

      totalPrice = totalPrice.add(dailyPrice);
      currentDate = currentDate.plusDays(1);
    }

    if (totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Total price must be greater than zero");
    }

    return totalPrice;
  }

  // Devuelve todas las fechas de reserva para un apartamento específico
  @Override
  public List<BookingDateRange> getAllDatesByApartment(Integer apartmentId) {
    return bookingRepository.findAllDatesByApartment(apartmentId);
  }

  @Override
  public List<BookingInfo> getBookingInfosByMounthAndAparment(Integer apartmentId, int mounth, int year) {
    // Obtener bookings desde el repositorio
    List<Booking> bookings = bookingRepository.findBookingsByApartmentAndMonth(apartmentId, mounth, year);

    // Mapear cada Booking a BookingInfo
    List<BookingInfo> bookingsInfo = bookings.stream()
        .map(booking -> modelMapper.map(booking, BookingInfo.class))
        .toList();

    return bookingsInfo;
  }

  // En BookingService.java (o BookingServiceImpl)
@Override
public BookingChatbotDto createBookingFromChatbot(BookingChatbotDto dto) {

    Apartment apartment = apartmentRepository.findById(dto.getApartmentId())
        .orElseThrow(() -> new EntityNotFoundException(
            "Apartamento " + dto.getApartmentId() + " no encontrado"));

    BigDecimal totalPrice = getTotalPrice(dto.getApartmentId(), dto.getStartDate(), dto.getEndDate());

    Booking booking = new Booking();
    booking.setApartment(apartment);
    booking.setStartDate(dto.getStartDate());
    booking.setEndDate(dto.getEndDate());
    booking.setGuests(dto.getGuests() != null ? dto.getGuests() : 1);
    booking.setTotalPrice(totalPrice);
    booking.setStatus(Booking.Status.PENDING);
    booking.setMethodPayment(dto.getMethodPayment());
    booking.setNotes(dto.getNotes());
    
    // ── 🔥 EL ESCUDO MANUAL: Forzamos los valores para evitar el "null" en BD ──
    booking.setCreatedAt(LocalDateTime.now());
    booking.setUpdatedAt(LocalDateTime.now());

    Booking saved = bookingRepository.save(booking);

    // Devolver solo lo que el bot necesita
    BookingChatbotDto response = new BookingChatbotDto();
    response.setBookingId(saved.getId());
    response.setApartmentId(dto.getApartmentId());
    response.setStartDate(saved.getStartDate());
    response.setEndDate(saved.getEndDate());
    response.setGuests(saved.getGuests());
    response.setTotalPrice(saved.getTotalPrice());
    response.setStatus(saved.getStatus());
    response.setMethodPayment(saved.getMethodPayment());
    response.setNotes(saved.getNotes());

    return response;
}

  @Override
  @Transactional // Recomendado para procesamientos por lotes
  public List<BookingDto> createBookingsFromCSV(String path) {
    // 1. Leer los DTOs desde el CSV usando tu utilidad existente
    List<BookingDto> dtosFromCsv = bookingCSVReader.bookingCSVReader(path);
    List<BookingDto> savedBookings = new java.util.ArrayList<>();

    for (BookingDto dto : dtosFromCsv) {
      try {
        // 2. Validar disponibilidad (reutilizando tu lógica de overlaps)
        isValidBooking(dto);

        // 3. Calcular el precio total (reutilizando tu lógica de PriceService)
        BigDecimal total = getTotalPrice(
            dto.getApartmentId(),
            dto.getStartDate(),
            dto.getEndDate());

        // 4. Mapear a entidad y configurar campos faltantes
        Booking booking = modelMapper.map(dto, Booking.class);
        booking.setTotalPrice(total);

        // Si el ID viene del CSV y quieres forzar una creación nueva,
        // asegúrate de cómo maneja JPA el ID manual.
        // Si quieres que la DB genere uno nuevo, haz booking.setId(null);

        // 5. Guardar
        Booking saved = bookingRepository.save(booking);
        savedBookings.add(modelMapper.map(saved, BookingDto.class));

      } catch (Exception e) {
        // Logueamos el error de una fila específica y continuamos con las demás
        System.err.println("❌ Error procesando fila de reserva para apto "
            + dto.getApartmentId() + ": " + e.getMessage());
      }
    }

    return savedBookings;
  }
  
  @Override
  public Map<String, Object> getKPIs(LocalDate startDate, LocalDate endDate, String apartmentType) {

    ApartmentType tipoFiltro = null;

    // 1. Validar y convertir de forma segura para evitar el
    // IllegalArgumentException
    if (apartmentType != null && !apartmentType.trim().isEmpty() && !apartmentType.equalsIgnoreCase("ALL")) {
      try {
        tipoFiltro = ApartmentType.valueOf(apartmentType.trim());
      } catch (IllegalArgumentException e) {
        tipoFiltro = null;
      }
    }

    // 2. Recuperar las reservas de la BD
    List<Booking> bookings = bookingRepository.findBookingsForKPIs(startDate, endDate, tipoFiltro);

    // 3. Calcular las métricas
    long totalBookings = bookings.size();
    long totalNights = 0;
    double totalRevenue = 0.0;

    for (Booking booking : bookings) {
      if (booking.getStartDate() != null && booking.getEndDate() != null) {
        long nights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        totalNights += nights;
      }
      if (booking.getTotalPrice() != null) {
        totalRevenue += booking.getTotalPrice().doubleValue();
      }
    }

    // 4. Construir y devolver el mapa con los resultados EXACTOS que espera el
    // Frontend
    Map<String, Object> kpis = new HashMap<>();
    kpis.put("totalBookings", totalBookings);
    kpis.put("totalNights", totalNights);
    kpis.put("totalRevenue", totalRevenue);

    // CORRECCIÓN AQUÍ: Cambiado a promedio por RESERVA
    kpis.put("averageRevenuePerBooking", totalBookings > 0 ? totalRevenue / totalBookings : 0.0);

    // Campos informativos que requiere tu interfaz TypeScript:
    kpis.put("apartmentTypeFiltered", apartmentType != null ? apartmentType : "ALL");
    kpis.put("startDateFiltered", startDate.toString());
    kpis.put("endDateFiltered", endDate.toString());

    return kpis;
  }

  @Override
  public Map<String, Object> getKPIs(LocalDate startDate, LocalDate endDate) {
    // 1. Recuperar todas las reservas en el rango de fechas
    List<Booking> bookings = bookingRepository.findBookingsForKPIs(startDate, endDate, null);

    // 2. Inicializar contadores para las métricas
    long totalBookings = bookings.size();
    long totalNights = 0;
    double totalRevenue = 0.0;

    // 3. Calcular noches totales e ingresos totales acumulados
    for (Booking booking : bookings) {
      if (booking.getStartDate() != null && booking.getEndDate() != null) {
        long nights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        totalNights += nights;
      }
      if (booking.getTotalPrice() != null) {
        totalRevenue += booking.getTotalPrice().doubleValue();
      }
    }

    // 4. Construir el mapa de resultados con los KPIs globales EXACTOS
    Map<String, Object> kpis = new HashMap<>();
    kpis.put("totalBookings", totalBookings);
    kpis.put("totalNights", totalNights);
    kpis.put("totalRevenue", totalRevenue);

    // CORRECCIÓN AQUÍ: Cambiado a promedio por RESERVA
    kpis.put("averageRevenuePerBooking", totalBookings > 0 ? totalRevenue / totalBookings : 0.0);

    // Campos informativos que requiere tu interfaz TypeScript:
    kpis.put("apartmentTypeFiltered", "ALL");
    kpis.put("startDateFiltered", startDate.toString());
    kpis.put("endDateFiltered", endDate.toString());

    return kpis;
  }
  @Override
  public Map<String, Object> getOccupancyData(LocalDate startDate, LocalDate endDate, String apartmentType) {
    // Implementa la lógica para calcular la tasa de ocupación
    // Esto puede implicar contar las noches reservadas vs las noches disponibles
    // para el tipo de apartamento y rango de fechas dado.

    // Por simplicidad, aquí solo devolvemos un ejemplo estático.
    Map<String, Object> occupancyData = new HashMap<>();
    occupancyData.put("occupancyRate", 75.0); // Ejemplo: 75% de ocupación
    occupancyData.put("startDateFiltered", startDate);
    occupancyData.put("endDateFiltered", endDate);
    occupancyData.put("apartmentTypeFiltered", apartmentType);

    return occupancyData;
  }

  @Override
  public List<BookingDto> getRecentBookings(int limit) {
    // Implementa la lógica para recuperar las reservas más recientes
    // Esto puede implicar ordenar las reservas por fecha de creación o fecha de
    // inicio
    // y limitar el resultado al número especificado.
    // Por simplicidad, aquí solo devolvemos una lista vacía.
    return List.of(); // Reemplaza con la lógica real para obtener las reservas recientes
  }

  @Override
  public List<Map<String, Object>> getApartmentsSummary() {
    // Implementa la lógica para recuperar un resumen de los apartamentos activos
    // Esto puede implicar contar el número de reservas por apartamento, su tasa de
    // ocupación

    // Por simplicidad, aquí solo devolvemos una lista vacía.
    return List.of(); // Reemplaza con la lógica real para obtener el resumen de apartamentos
  }

  @Override
  public List<OccupancyDataPointDto> getOccupancyData(LocalDate startDate, LocalDate endDate) {
    List<OccupancyDataPointDto> chartData = new ArrayList<>();
    LocalDate today = LocalDate.now();

    // 1. Obtener capacidad hotelera real
    int totalApartments = (int) apartmentRepository.count();
    if (totalApartments == 0)
      totalApartments = 100;

    // 2. Traer las predicciones anuales desde FastAPI por rango diario
    Map<String, Integer> aiPredictions = predictionMLService.getBookingsPredictionsForYear(endDate.getYear());

    System.out.println("¿El mapa tiene datos? " + !aiPredictions.isEmpty());
    System.out.println("Contenido de prueba para el 2026-06-01: " + aiPredictions.get("2026-06-01"));

    // 3. Recorrer el rango temporal día por día
    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {

      if (date.isBefore(today) || date.isEqual(today)) {
        // ==========================================
        // PASADO / PRESENTE: Datos Reales (SQL)
        // ==========================================
        int bookedApartments = bookingRepository.countActiveBookingsByDate(date);
        BigDecimal dailyRevenue = bookingRepository.calculateRevenueByDate(date);
        if (dailyRevenue == null)
          dailyRevenue = BigDecimal.ZERO;

        double occupancyRate = ((double) bookedApartments / totalApartments) * 100;

        chartData.add(OccupancyDataPointDto.builder()
            .date(date)
            .actualOccupancyRate(occupancyRate)
            .actualBookedApartments(bookedApartments)
            .actualTotalApartments(totalApartments)
            .actualRevenue(dailyRevenue)
            .isHistorical(true)
            .isPrediction(false)
            .build());

      } else {
        // ==========================================
        // FUTURO: Ocupación Real Confirmada (SQL) + Predicción (IA)
        // ==========================================

        // A) LEER LO REAL CONFIRMADO PARA EL FUTURO
        int bookedApartmentsReal = bookingRepository.countActiveBookingsByDate(date);
        BigDecimal dailyRevenueReal = bookingRepository.calculateRevenueByDate(date);
        if (dailyRevenueReal == null)
          dailyRevenueReal = BigDecimal.ZERO;

        double actualOccupancyRateReal = ((double) bookedApartmentsReal / totalApartments) * 100;

        // B) LEER LA PREDICCIÓN DE LA IA
        String dateKey = date.toString(); // "yyyy-MM-dd"
        int predictedBooked = aiPredictions.getOrDefault(dateKey, 0);
        double predictedOccupancyRate = ((double) predictedBooked / totalApartments) * 100;

        // Estimación monetaria de la IA (ej: 150€ por apartamento)
        BigDecimal estimatedRevenue = BigDecimal.valueOf(predictedBooked * 150.0);

        // C) CONSTRUIR EL DTO MIXTO
        chartData.add(OccupancyDataPointDto.builder()
            .date(date)
            // Enviamos los datos reales del futuro (on-the-books)
            .actualOccupancyRate(actualOccupancyRateReal)
            .actualBookedApartments(bookedApartmentsReal)
            .actualTotalApartments(totalApartments)
            .actualRevenue(dailyRevenueReal)
            // Enviamos las predicciones de la IA en la misma fila
            .predictedOccupancyRate(predictedOccupancyRate)
            .predictedBookedApartments(predictedBooked)
            .predictedRevenue(estimatedRevenue)
            .predictionConfidence(0.85)
            .isHistorical(false)
            .isPrediction(true) // Sigue siendo zona de predicción para el Frontend
            .build());
      }
    }

    return chartData;
  }
    
    // ... Tus otros métodos del controlador (createBooking, etc.)
}

