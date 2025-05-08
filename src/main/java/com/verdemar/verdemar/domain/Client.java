package com.verdemar.verdemar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer id;

    private String name;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type", columnDefinition = "TINYINT", nullable = false)
    private ClientType type;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private String email;

    // Getters y setters...

    public enum ClientType {
        onebedroom,   // 0
        twobedrooms   // 1
    }
}
