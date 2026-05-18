package com.verdemar.domain.discount;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class DiscountRequestDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private int discount;
    private Boolean isPercentage;
    private List<Integer> apartmentIds;
}
