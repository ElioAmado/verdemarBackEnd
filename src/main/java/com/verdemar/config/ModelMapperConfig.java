package com.verdemar.config;

import com.verdemar.domain.Client;
import com.verdemar.domain.apartment.Apartment;
import com.verdemar.repository.ApartmentRepository;
import com.verdemar.repository.ClientRepository;
import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
  @Bean
  ModelMapper modelMapper(
      ClientRepository clientRepository, ApartmentRepository apartmentRepository) {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.addConverter(
        new AbstractConverter<Short, Apartment>() {
          @Override
          protected Apartment convert(Short source) {
            if (source == null) return null;
            return apartmentRepository
                .findById(source)
                .orElseThrow(() -> new RuntimeException("Apartamento no encontrado"));
          }
        });
    modelMapper.addConverter(
        new AbstractConverter<Integer, Client>() {
          @Override
          protected Client convert(Integer source) {
            if (source == null) return null;
            return clientRepository
                .findById(source)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
          }
        });
    return new ModelMapper();
  }
}
