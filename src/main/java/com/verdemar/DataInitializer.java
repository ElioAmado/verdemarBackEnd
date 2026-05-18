package com.verdemar;

import com.opencsv.bean.CsvToBeanBuilder;
import com.verdemar.domain.Client;
import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.csv.PriceRow;
import com.verdemar.domain.price.Price;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.ClientRepository;
import com.verdemar.repository.PriceRepository;
import com.verdemar.service.apartment.ApartmentService;
import com.verdemar.service.booking.BookingCSVReader;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
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
    createInitialApartments();
   // createInitialClients();
    createInitialPrices();
    // createInitialPricesCSV();
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
          Apartment apartment = apartmentRepository.findById((short) i).orElse(null);

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
}