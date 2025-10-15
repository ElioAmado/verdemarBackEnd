package com.verdemar.domain.dto;

import com.verdemar.domain.apartment.Apartment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApartmentAvailabilityDTO {
  private Apartment apartment;
  private boolean available;

  public ApartmentAvailabilityDTO(short apartmentId, boolean available) {
    this.apartment = new Apartment();
    this.apartment.setId(apartmentId);
    this.available = available;
  }
}
