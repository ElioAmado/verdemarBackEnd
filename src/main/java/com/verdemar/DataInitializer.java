package com.verdemar;

import com.opencsv.bean.CsvToBeanBuilder;
import com.verdemar.domain.Client;
import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.booking.Booking;
import com.verdemar.domain.csv.PriceRow;
import com.verdemar.domain.price.Price;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.BookingRepository;
import com.verdemar.repository.ClientRepository;
import com.verdemar.repository.PriceRepository;
import com.verdemar.service.apartment.ApartmentService;
import com.verdemar.service.booking.BookingCSVReader;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

  @Autowired
  private ApartmentRepository apartmentRepository;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private ClientRepository clientRepository;

  @Autowired
  private PriceRepository priceRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private BookingCSVReader bookingCSVReader;

  @Autowired
  private ApartmentService apartmentService;

  @Override
  public void run(String... args) {
    // createInitialApartments();
   // createInitialClients();
    // createInitialPrices();
    // createInitialPricesCSV();
    createInitialBookings();
  }

  public void createInitialPrices() {
    if (priceRepository.count() == 0) {
      for (int i = 1; i <= 100; i++) {
        LocalDate startDate = LocalDate.of(2026, 4, 14);
        LocalDate endDate = LocalDate.of(2026, 10, 28);
        LocalDate currentDate = startDate;

        while (currentDate.isBefore(endDate)) {
          double randomValue = ThreadLocalRandom.current().nextDouble(100, 500);
          BigDecimal priceValue = BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);

          // Controlamos con orElse(null) para evitar que rompa si no encuentra el
          // apartamento
          Apartment apartment = apartmentRepository.findById((Integer) i).orElse(null);

          if (apartment != null) {
            Price price = new Price(apartment, currentDate, priceValue);
            priceRepository.save(price);
          }

          currentDate = currentDate.plusDays(1);
        }
      }
      System.out.println("✅ Precios iniciales aleatorios creados.");
    }
  }

  public List<Price> createInitialPricesCSV() {
    String path = "csv/precios.csv";

    try (InputStreamReader reader = new InputStreamReader(getClass().getClassLoader().getResourceAsStream(path))) {

      List<PriceRow> priceRows = new CsvToBeanBuilder<PriceRow>(reader)
          .withType(PriceRow.class)
          .withIgnoreLeadingWhiteSpace(true)
          .build()
          .parse();

      List<Price> prices = priceRows.stream().map(priceRow -> modelMapper.map(priceRow, Price.class)).toList();

      priceRepository.saveAll(prices);
      System.out.println("✅ Precios cargados desde el CSV.");
      return prices;

    } catch (Exception e) {
      e.printStackTrace();
      return List.of();
    }
  }

  private void createInitialApartments() {
    if (apartmentRepository.count() == 0) {
      // Limpiado el conflicto de ramas y el código duplicado
      List<Apartment> apartments = apartmentService.apartmentCSVReader("csv/apartaments.csv");

      if (apartments != null && !apartments.isEmpty()) {
        apartmentRepository.saveAll(apartments);
        System.out.println("✅ " + apartments.size() + " apartamentos cargados desde el CSV.");
      } else {
        System.out.println("⚠️ El CSV de apartamentos está vacío o no se pudo leer.");
      }
    }
  }

  private void createInitialClients() {
    if (clientRepository.count() == 0) {
      List<Client> clients = List.of(
          new Client(null, "Juan", "Pérez", "123456789", "juan.perez@example.com"),
          new Client(null, "Ana", "Gómez", "987654321", "ana.gomez@example.com"),
          new Client(null, "Luis", "Martínez", "555123456", "luis.martinez@example.com"));
      clientRepository.saveAll(clients);
      System.out.println("✅ Clientes iniciales creados.");
    }
  }

  private void createInitialBookings() {
    if (bookingRepository.count() > 0) return;

    if (bookingRepository.count() > 0)
      return;

    List<Client> clients = clientRepository.findAll();
    List<Apartment> apartments = apartmentRepository.findAll();

    // 🔥 CONTROL DE SEGURIDAD: Si alguna lista está vacía, evitamos el crash
    if (clients.isEmpty() || apartments.isEmpty()) {
      System.err.println(
          "⚠️ No se pueden crear reservas iniciales: Asegúrate de que las tablas de Clientes y Apartamentos tengan datos primero.");
      return;
    }

    Random random = new Random(42);

    String[] notes = {
        "Esperando el comprobante de transferencia.",
        "Niños pequeños, necesita protecciones.",
        "Solicita desayuno incluido.",
        "Visita médica, estancia extendida posible.",
        "Requiere parking disponible.",
        "Familia numerosa.",
        "Necesita camas separadas.",
        "Check-in tardío solicitado.",
        "Celebración de aniversario.",
        "Primera visita a la ciudad.",
        "Mascota pequeña, necesita confirmación.",
        "Solicita toallas extra.",
        "Viaje de negocios.",
        "Estancia de luna de miel.",
        "Requiere factura de empresa.",
        "Alérgico al polvo, solicita limpieza extra.",
        "Llegan en grupo, posible ruido.",
        "Solicita cuna para bebé.",
        "Necesita acceso para silla de ruedas.",
        "Turistas, primera vez en el país."
    };

    String[] paymentMethods = {"CREDIT_CARD", "BANK_TRANSFER", "STRIPE", "PAYPAL"};
    Booking.Status[] statuses = Booking.Status.values();

    LocalDate baseStartDate = LocalDate.of(2025, 1, 1);

    LocalDateTime baseCreatedAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
    LocalDateTime maxCreatedAt  = LocalDateTime.of(2026, 1, 31, 23, 59, 59);
    long secondsRange = ChronoUnit.SECONDS.between(baseCreatedAt, maxCreatedAt);


    List<Booking> bookings = new ArrayList<>(500);

    for (int i = 0; i < 30000; i++) {
        Booking booking = new Booking();

        booking.setClient(clients.get(random.nextInt(clients.size())));
        booking.setApartment(apartments.get(random.nextInt(apartments.size())));
        booking.setGuests((byte) (random.nextInt(8) + 1));

        LocalDate startDate = baseStartDate.plusDays(random.nextInt(365 * 3));
        booking.setStartDate(startDate);
        booking.setEndDate(startDate.plusDays(random.nextInt(30) + 1));

        double price = 100 + (random.nextDouble() * 4900);
        booking.setTotalPrice(BigDecimal.valueOf(Math.round(price * 100.0) / 100.0));

        booking.setStatus(statuses[random.nextInt(statuses.length)]);
        booking.setMethodPayment(paymentMethods[random.nextInt(paymentMethods.length)]);
        booking.setNotes(notes[random.nextInt(notes.length)]);

        // Fechas manuales (requiere el cambio en @PrePersist)
        LocalDateTime createdAt = baseCreatedAt.plusSeconds((long)(random.nextDouble() * secondsRange));
        booking.setCreatedAt(createdAt);
        booking.setUpdatedAt(createdAt.plusSeconds((long)(random.nextDouble() * 60 * 24 * 60 * 60)));

        bookings.add(booking);

        if (bookings.size() == 500) {
            bookingRepository.saveAll(bookings);
            bookings.clear();
        }
    }

    if (!bookings.isEmpty()) {
        bookingRepository.saveAll(bookings);
    }

    System.out.println("✅ 30.000 bookings creados correctamente.");
}}