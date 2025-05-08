package com.verdemar.verdemar.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class PriceId implements Serializable {
    private Short apartment;
    private LocalDate date;

    public PriceId() {}

    public PriceId(Short apartment, LocalDate date) {
        this.apartment = apartment;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceId)) return false;
        PriceId that = (PriceId) o;
        return Objects.equals(apartment, that.apartment) &&
               Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(apartment, date);
    }
}

