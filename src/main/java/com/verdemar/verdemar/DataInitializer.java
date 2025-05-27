package com.verdemar.verdemar;

import com.verdemar.verdemar.domain.ApartmentType;
import com.verdemar.verdemar.domain.Client;
import com.verdemar.verdemar.domain.Apartment;
import com.verdemar.verdemar.repository.ApartmentRepository;
import com.verdemar.verdemar.repository.ClientRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ApartmentRepository apartmentRepository;
    private final ClientRepository clientRepository;

    public DataInitializer(ApartmentRepository apartmentRepository, ClientRepository clientRepository) {
        this.apartmentRepository = apartmentRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) {
        if (apartmentRepository.count() == 0) {
            List<Apartment> apartments = List.of(
                new Apartment((short) 1, ApartmentType.TWO_BEDROOM, (short) 4, (short) 0, "Test pls delete", Collections.emptyList()),
                new Apartment((short) 2, ApartmentType.TWO_BEDROOM, (short) 4, (short) 0, "Test pls delete", Collections.emptyList()),
                new Apartment((short) 3, ApartmentType.TWO_BEDROOM, (short) 4, (short) 1, "Test pls delete", Collections.emptyList()),
                new Apartment((short) 4, ApartmentType.TWO_BEDROOM, (short) 4, (short) 1, "Test pls delete", Collections.emptyList()),
                new Apartment((short) 5, ApartmentType.ONE_BEDROOM, (short) 3, (short) 0, "Test pls delete", Collections.emptyList()),
                new Apartment((short) 6, ApartmentType.ONE_BEDROOM, (short) 3, (short) 1, "Test pls delete", Collections.emptyList())
            );
            apartmentRepository.saveAll(apartments);
            System.out.println("Apartamentos iniciales creados.");
        }

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
