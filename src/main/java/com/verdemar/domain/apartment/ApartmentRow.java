package com.verdemar.domain.apartment;

import lombok.Data;

@Data
public class ApartmentRow {
    private Integer id;
    private String type; // Lo leemos como String para convertirlo luego
    private short capacity;
    private short floor;
    private String description;

    // Getters y Setters...
}