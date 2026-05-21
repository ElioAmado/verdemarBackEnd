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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
      for (int i = 1; i <= 300; i++) {
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
    if (bookingRepository.count() > 0)
      return;

    List<Client> clients = clientRepository.findAll();
    List<Apartment> apartments = apartmentRepository.findAll();

    if (clients.isEmpty() || apartments.isEmpty()) {
      System.err.println("⚠️ No se pueden crear reservas iniciales: Faltan clientes o apartamentos.");
      return;
    }

    Random random = new Random(42);

    Map<Integer, List<LocalDate[]>> ocupacionesPorApartamento = new HashMap<>();
    for (Apartment apt : apartments) {
      ocupacionesPorApartamento.put(apt.getId(), new ArrayList<>());
    }

    String[] notes = { "Esperando comprobante.", "Solicita desayuno.", "Requiere parking.", "Viaje de negocios." };
    String[] paymentMethods = { "CREDIT_CARD", "BANK_TRANSFER", "STRIPE", "PAYPAL" };
    Booking.Status[] statuses = Booking.Status.values();

    // 📅 RANGO DINÁMICO PARA LAS RESERVAS
    LocalDate baseStartDate = LocalDate.of(2022, 1, 1);
    LocalDate maxStartDate = LocalDate.of(2027, 1, 1); // Hoy (Año 2026)
    // Calculamos cuántos días reales hay entre 2022 y hoy para usarlos de límite
    long totalDaysRange = ChronoUnit.DAYS.between(baseStartDate, maxStartDate);

    // Fechas de creación de auditoría
    LocalDateTime baseCreatedAt = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
    LocalDateTime maxCreatedAt = LocalDateTime.now();
    long secondsRange = ChronoUnit.SECONDS.between(baseCreatedAt, maxCreatedAt);

    List<Booking> bookings = new ArrayList<>(500);
    int creadasExitosamente = 0;

    // 🔥 Subimos los intentos máximos. Al validar solapamientos, el algoritmo
    // necesitará más margen para encontrar huecos libres en 30,000 registros.
    int intentos_Maximos = 300000;
    int intentos = 0;

    while (creadasExitosamente < 30000 && intentos < intentos_Maximos) {
      intentos++;

      Apartment selectedApartment = apartments.get(random.nextInt(apartments.size()));

      // ✨ CORRECCIÓN: Ahora el número aleatorio abarca desde 2022 hasta el día de hoy
      // en 2026
      LocalDate startDate = baseStartDate.plusDays(random.nextLong(totalDaysRange));
      LocalDate endDate = startDate.plusDays(random.nextInt(30) + 1);

      // 🔍 COMPROBACIÓN DE DISPONIBILIDAD EN MEMORIA
      List<LocalDate[]> rangosOcupados = ocupacionesPorApartamento.get(selectedApartment.getId());
      boolean estaOcupado = rangosOcupados.stream()
          .anyMatch(rango -> !startDate.isAfter(rango[1]) && !endDate.isBefore(rango[0]));

      if (estaOcupado) {
        continue;
      }

      rangosOcupados.add(new LocalDate[] { startDate, endDate });

      Booking booking = new Booking();
      booking.setClient(clients.get(random.nextInt(clients.size())));
      booking.setApartment(selectedApartment);
      booking.setGuests((byte) (random.nextInt(8) + 1));
      booking.setStartDate(startDate);
      booking.setEndDate(endDate);

      double price = 100 + (random.nextDouble() * 4900);
      booking.setTotalPrice(BigDecimal.valueOf(Math.round(price * 100.0) / 100.0));
      booking.setStatus(statuses[random.nextInt(statuses.length)]);
      booking.setMethodPayment(paymentMethods[random.nextInt(paymentMethods.length)]);
      booking.setNotes(notes[random.nextInt(notes.length)]);

      LocalDateTime createdAt = baseCreatedAt.plusSeconds((long) (random.nextDouble() * secondsRange));
      booking.setCreatedAt(createdAt);
      booking.setUpdatedAt(createdAt.plusSeconds((long) (random.nextDouble() * 60 * 24 * 60 * 60)));

      bookings.add(booking);
      creadasExitosamente++;

      if (bookings.size() == 500) {
        bookingRepository.saveAll(bookings);
        bookings.clear();
      }
    }

    if (!bookings.isEmpty()) {
      bookingRepository.saveAll(bookings);
    }

    System.out.println("✅ " + creadasExitosamente + " bookings reales (sin solapamientos) creados correctamente.");
    if (intentos >= intentos_Maximos) {
      System.out.println("⚠️ Se alcanzó el límite de intentos (" + intentos + "). El calendario se ha saturado con "
          + creadasExitosamente + " reservas.");
    }
  }
}