package com.verdemar.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "BOOKING")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "apartment_id", nullable = false)
    private Apartament apartment;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "total_price")
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;

    @Column(columnDefinition = "TEXT")
    private String notes;

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    // Getters y setters...
}
