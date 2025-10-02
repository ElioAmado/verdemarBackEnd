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

            // List<Price> priceList;
            for (int i = 1; i <= 6; i++) {
                LocalDate startDate = LocalDate.of(2026, 6, 14);
                LocalDate endDate = LocalDate.of(2026, 10, 28);
                LocalDate currentDate = startDate;

                while (currentDate.isBefore(endDate)) {
                    double randomValue = ThreadLocalRandom.current().nextDouble(100, 500); // Random price between 100
                                                                                           // and 500
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
                    new Apartment((short) 1, ApartmentType.TWO_BEDROOM, (short) 4, (short) 0,
                            "Disfruta de un espacioso apartamento de dos dormitorios en planta baja, ideal para familias o grupos de hasta 4 personas. Su ubicación facilita el acceso sin escaleras, perfecto para todas las edades.",
                            Collections.emptyList()),

                    new Apartment((short) 2, ApartmentType.TWO_BEDROOM, (short) 4, (short) 0,
                            "Confort y funcionalidad se combinan en este acogedor apartamento de dos dormitorios situado en planta baja. Apto para hasta 4 huéspedes, es una excelente opción para unas vacaciones tranquilas y cómodas.",
                            Collections.emptyList()),

                    new Apartment((short) 3, ApartmentType.TWO_BEDROOM, (short) 4, (short) 1,
                            "Este apartamento de dos dormitorios en primera planta ofrece vistas elevadas y un ambiente luminoso. Con capacidad para 4 personas, es ideal para quienes buscan un espacio acogedor y tranquilo.",
                            Collections.emptyList()),

                    new Apartment((short) 4, ApartmentType.TWO_BEDROOM, (short) 4, (short) 1,
                            "Ubicado en la primera planta, este apartamento de dos dormitorios es perfecto para familias o grupos de amigos que deseen privacidad y comodidad durante su estancia. Acomoda hasta 4 personas.",
                            Collections.emptyList()),

                    new Apartment((short) 5, ApartmentType.ONE_BEDROOM, (short) 2, (short) 0,
                            "Este encantador apartamento de un dormitorio en planta baja es ideal para parejas o viajeros individuales. Cómodo y accesible, es una opción perfecta para una escapada relajante.",
                            Collections.emptyList()),

                    new Apartment((short) 6, ApartmentType.ONE_BEDROOM, (short) 2, (short) 1,
                            "Disfruta de un ambiente íntimo y acogedor en este apartamento de un dormitorio en primera planta. Con capacidad para 2 personas, es ideal para una estancia tranquila en pareja.",
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
