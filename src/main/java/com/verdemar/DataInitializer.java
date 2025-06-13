package com.verdemar;

import com.verdemar.domain.ApartmentType;
import com.verdemar.domain.Client;
import com.verdemar.domain.Price;
import com.verdemar.domain.csv.PriceRow;
import com.opencsv.bean.CsvToBeanBuilder;
import com.verdemar.domain.Apartment;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.ClientRepository;
import com.verdemar.repository.PriceRepository;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ApartmentRepository apartmentRepository;
    private final ClientRepository clientRepository;
    private final PriceRepository priceRepository;
    private final ModelMapper modelMapper;

    public DataInitializer(ApartmentRepository apartmentRepository, ClientRepository clientRepository, 
                           PriceRepository priceRepository, ModelMapper modelMapper) {
        this.apartmentRepository = apartmentRepository;
        this.clientRepository = clientRepository;
        this.priceRepository = priceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void run(String... args) {
        createInitialApartments();
        createInitialClients();
        createInitialPrices();
        // createInitialPricesCSV();

    }

    public void createInitialPrices() {

        if (priceRepository.count() == 0) { // Prices already exist, no need to create them again

            List<Price> priceList;
            for (int i = 1; i <= 6; i++) {
                LocalDate startDate = LocalDate.now();
                LocalDate endDate = LocalDate.of(2025, 10, 28);
                LocalDate currentDate = startDate;
                
while (currentDate.isBefore(endDate)) {
    double randomValue = ThreadLocalRandom.current().nextDouble(100, 500); // Random price between 100 and 500
    BigDecimal priceValue = BigDecimal.valueOf(randomValue).setScale(2, RoundingMode.HALF_UP);

    Apartment apartment = apartmentRepository.findById((short) i).get();
    Price price = new Price(apartment, currentDate, priceValue);
    priceRepository.save(price);

    currentDate = currentDate.plusDays(1); // Increment the date by one day
}


            }
    }
}

    public List<Price> createInitialPricesCSV() {
        String path = "csv/precios.csv";

        try (InputStreamReader reader = new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream(path))) {

            List<PriceRow> priceRows = new CsvToBeanBuilder<PriceRow>(reader)
                    .withType(PriceRow.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build()
                    .parse();

            List<Price> prices = priceRows.stream()
                    .map(priceRow -> modelMapper.map(priceRow, Price.class))
                    .toList();

            // Guardar en base de datos
            priceRepository.saveAll(prices);

            return prices;

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }    

    private void createInitialApartments() {
        if (apartmentRepository.count() == 0) {
            List<Apartment> apartments = List.of(
                    new Apartment((short) 1, ApartmentType.TWO_BEDROOM, (short) 4, (short) 0, "Test pls delete",
                            Collections.emptyList()),
                    new Apartment((short) 2, ApartmentType.TWO_BEDROOM, (short) 4, (short) 0, "Test pls delete",
                            Collections.emptyList()),
                    new Apartment((short) 3, ApartmentType.TWO_BEDROOM, (short) 4, (short) 1, "Test pls delete",
                            Collections.emptyList()),
                    new Apartment((short) 4, ApartmentType.TWO_BEDROOM, (short) 4, (short) 1, "Test pls delete",
                            Collections.emptyList()),
                    new Apartment((short) 5, ApartmentType.ONE_BEDROOM, (short) 3, (short) 0, "Test pls delete",
                            Collections.emptyList()),
                    new Apartment((short) 6, ApartmentType.ONE_BEDROOM, (short) 3, (short) 1, "Test pls delete",
                            Collections.emptyList()));
            apartmentRepository.saveAll(apartments);
            System.out.println("Apartamentos iniciales creados.");
        }
    }

    private void createInitialClients() {
        if (clientRepository.count() == 0) {
            List<Client> clients = List.of(
                    new Client(null, "Juan", "Pérez", "123456789", "juan.perez@example.com"),
                    new Client(null, "Ana", "Gómez", "987654321", "ana.gomez@example.com"),
                    new Client(null, "Luis", "Martínez", "555123456", "luis.martinez@example.com"));
            clientRepository.saveAll(clients);
            System.out.println("Clientes iniciales creados.");
        }

    }
}
