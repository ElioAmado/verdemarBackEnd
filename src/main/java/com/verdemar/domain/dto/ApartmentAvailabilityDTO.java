package com.verdemar.domain.dto;
import com.verdemar.domain.Apartment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class ApartmentAvailabilityDTO {
    private Apartment apartment;
    private boolean available;
}
