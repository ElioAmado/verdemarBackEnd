package com.verdemar.verdemar.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.verdemar.verdemar.domain.Client;
import com.verdemar.verdemar.domain.Booking;
import com.verdemar.verdemar.domain.dto.BookingDto;
import com.verdemar.verdemar.repository.ClientRepository;
import com.verdemar.verdemar.repository.BookingRepository;
import com.verdemar.verdemar.repository.ApartmentRepository;
import com.verdemar.verdemar.domain.Apartment;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(ApartmentRepository apartmentRepo, ClientRepository clientRepo) {
        ModelMapper modelMapper = new ModelMapper();

        // Conversor de apartmentId -> Apartment
        modelMapper.addConverter(new AbstractConverter<Short, Apartment>() {
            @Override
            protected Apartment convert(Short source) {
                if (source == null) return null;
                return apartmentRepo.findById(source)
                    .orElseThrow(() -> new RuntimeException("Apartamento no encontrado"));
            }
        });

        // Conversor de clientId -> Client
        modelMapper.addConverter(new AbstractConverter<Integer, Client>() {
            @Override
            protected Client convert(Integer source) {
                if (source == null) return null;
                return clientRepo.findById(source)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            }
        });

        // Define el mapeo personalizado de BookingDto a Booking
        modelMapper.addMappings(new PropertyMap<BookingDto, Booking>() {
            @Override
            protected void configure() {
                map().setStartDate(source.getStartDate());
                map().setEndDate(source.getEndDate());
                map().setTotalPrice(source.getTotalPrice());
                map().setStatus(source.getStatus());


                map().setNotes(source.getNotes());

                // Mapear apartmentId usando el converter configurado
                using(ctx -> modelMapper.map(ctx.getSource(), Apartment.class))
                    .map(source.getApartmentId(), destination.getApartment());

                // Mapear clientId usando el converter configurado
                using(ctx -> modelMapper.map(ctx.getSource(), Client.class))
                    .map(source.getClientId(), destination.getClient());
            }
        });

        return modelMapper;
    }
}
