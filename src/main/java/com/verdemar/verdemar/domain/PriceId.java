package com.verdemar.verdemar.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PriceId implements Serializable {

    private Short apartment;
    private LocalDate date;

    // Constructor, equals y hashCode

    public PriceId() {
    }

    public PriceId(Short apartment, LocalDate date) {
        this.apartment = apartment;
        this.date = date;
    }

    // Getters y Setters

    public Short getApartment() {
        return apartment;
    }

    public void setApartment(Short apartment) {
        this.apartment = apartment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // equals y hashCode para PriceId

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceId priceId = (PriceId) o;
        return Objects.equals(apartment, priceId.apartment) &&
               Objects.equals(date, priceId.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartment, date);
    }
}
