package com.verdemar.verdemar.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verdemar.verdemar.domain.Apartament;
import com.verdemar.verdemar.service.ApartamentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApartamentController.class)
public class ApartamentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApartamentService apartamentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Apartament sampleApartment;

    @BeforeEach
    void setUp() {
        sampleApartment = new Apartament();
        sampleApartment.setId((short) 1);
        sampleApartment.setApartamentType(getEnumValue("ONE_BED_ROOM"));
        sampleApartment.setCapacity((short) 2);
        sampleApartment.setFloor((short) 1);
        sampleApartment.setBedrooms((short) 1);
        sampleApartment.setDescription("Apartamento con una habitación.");
        sampleApartment.setBeds(Collections.emptyList()); // o null
    }

    private Apartament getApartmentMock() {
        return sampleApartment;
    }

    // Helper para establecer el enum interno correctamente
    private Apartament getEnumValue(String value) {
        try {
            java.lang.reflect.Field field = Apartament.class.getDeclaredField("apartamentType");
            field.setAccessible(true);
            field.set(sampleApartment, Enum.valueOf(
                (Class<Enum>) field.getType(), value));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sampleApartment;
    }

    @Test
    void shouldReturnAllApartments() throws Exception {
        when(apartamentService.getAllApartments()).thenReturn(Collections.singletonList(getApartmentMock()));

        mockMvc.perform(get("/api/apartments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Apartamento con una habitación."));
    }

    @Test
    void shouldReturnApartmentById() throws Exception {
        when(apartamentService.getApartmentById(1L)).thenReturn(getApartmentMock());

        mockMvc.perform(get("/api/apartments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacity").value(2));
    }

    @Test
    void shouldCreateApartment() throws Exception {
        when(apartamentService.createApartment(any(Apartament.class))).thenReturn(getApartmentMock());

        mockMvc.perform(post("/api/apartments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleApartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bedrooms").value(1));
    }

    @Test
    void shouldUpdateApartment() throws Exception {
        when(apartamentService.updateApartment(eq(1L), any(Apartament.class))).thenReturn(getApartmentMock());

        mockMvc.perform(put("/api/apartments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleApartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.floor").value(1));
    }

    @Test
    void shouldDeleteApartment() throws Exception {
        Mockito.doNothing().when(apartamentService).deleteApartment(1L);

        mockMvc.perform(delete("/api/apartments/1"))
                .andExpect(status().isNoContent());
    }
}
