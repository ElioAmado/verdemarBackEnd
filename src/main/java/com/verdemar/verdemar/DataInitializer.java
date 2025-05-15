package com.verdemar.verdemar;

import com.verdemar.verdemar.domain.ApartmentType;
import com.verdemar.verdemar.domain.Apartment;
import com.verdemar.verdemar.repository.ApartmentRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ApartmentRepository apartmentRepository;

    public DataInitializer(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
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
    }
}
