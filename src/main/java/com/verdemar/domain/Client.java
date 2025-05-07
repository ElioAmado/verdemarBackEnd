package com.verdemar.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id")
    private Integer id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ClientType type;

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private String email;

    // Getters y setters...

    public enum ClientType {
        onebedroom, twobedrooms
    }
}
