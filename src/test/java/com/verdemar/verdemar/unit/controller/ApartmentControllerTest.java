package com.verdemar.verdemar.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verdemar.verdemar.controller.ApartmentController;
import com.verdemar.verdemar.domain.Apartment;
import com.verdemar.verdemar.service.ApartmentService;
import com.verdemar.verdemar.domain.ApartmentType;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApartmentController.class)
public class ApartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApartmentService apartmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Apartment sampleApartment;

    @BeforeEach
    public void setup() {
        sampleApartment = new Apartment();
        sampleApartment.setId((short) 1);
        sampleApartment.setApartmentType(ApartmentType.ONE_BED_ROOM);
        sampleApartment.setCapacity((short) 2);
        sampleApartment.setFloor((short) 1);
        sampleApartment.setBedrooms((short) 1);
        sampleApartment.setDescription("Nice cozy apartment");
        sampleApartment.setBeds(Collections.emptyList());
    }

    @Test
    public void testGetAllApartments() throws Exception {
        Mockito.when(apartmentService.getAllApartments()).thenReturn(List.of(sampleApartment));

        mockMvc.perform(get("/api/apartments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleApartment.getId().intValue()))
                .andExpect(jsonPath("$[0].description").value("Nice cozy apartment"));
    }

    @Test
    public void testGetApartmentById() throws Exception {
        Mockito.when(apartmentService.getApartmentById((short) 1)).thenReturn(sampleApartment);

        mockMvc.perform(get("/api/apartments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Nice cozy apartment"));
    }

    @Test
    public void testCreateApartment() throws Exception {
        Mockito.when(apartmentService.createApartment(any(Apartment.class))).thenReturn(sampleApartment);

        mockMvc.perform(post("/api/apartments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleApartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateApartment() throws Exception {
        Mockito.when(apartmentService.updateApartment(eq((short) 1), any(Apartment.class))).thenReturn(sampleApartment);

        mockMvc.perform(put("/api/apartments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleApartment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteApartment() throws Exception {
        Mockito.doNothing().when(apartmentService).deleteApartment((short) 1);

        mockMvc.perform(delete("/api/apartments/1"))
                .andExpect(status().isNoContent());
    }
}
