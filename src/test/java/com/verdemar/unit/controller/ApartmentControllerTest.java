package com.verdemar.unit.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.verdemar.controller.ApartmentController;
import com.verdemar.domain.apartment.Apartment;
import com.verdemar.domain.apartment.ApartmentType;
import com.verdemar.domain.dto.ApartmentAvailabilityDTO;
import com.verdemar.service.apartment.ApartmentService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** Unit tests for ApartmentController using MockMvc. */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ApartmentController.class)
class ApartmentControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ApartmentService apartmentService;

  @Test
  @DisplayName("GET /api/apartments should return all apartments")
  void testGetAllApartments() throws Exception {
    Apartment apt =
        new Apartment(
            (short) 1, ApartmentType.ONE_BEDROOM, (short) 2, (short) 1, "Bonito estudio", null);

    Mockito.when(apartmentService.getAllApartments()).thenReturn(List.of(apt));

    mockMvc
        .perform(get("/api/apartment"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].apartmentType").value("ONE_BEDROOM"))
        .andExpect(jsonPath("$[0].capacity").value(2))
        .andExpect(jsonPath("$[0].floor").value(1))
        .andExpect(jsonPath("$[0].description").value("Bonito estudio"));
  }

  @Test
  @DisplayName("GET /api/apartments/{id} should return apartment by id")
  void testGetApartmentById() throws Exception {
    Apartment apt =
        new Apartment(
            (short) 2, ApartmentType.ONE_BEDROOM, (short) 4, (short) 2, "Duplex con vistas", null);

    Mockito.when(apartmentService.getApartmentById((short) 2)).thenReturn(apt);

    mockMvc
        .perform(get("/api/apartment/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.apartmentType").value("ONE_BEDROOM"))
        .andExpect(jsonPath("$.capacity").value(4))
        .andExpect(jsonPath("$.floor").value(2))
        .andExpect(jsonPath("$.description").value("Duplex con vistas"));
  }

  @Test
  @DisplayName("PUT /api/apartments/{id} should update apartment")
  void testUpdateApartment() throws Exception {
    Apartment apt =
        new Apartment(
            (short) 3, ApartmentType.ONE_BEDROOM, (short) 3, (short) 1, "Suite reformada", null);

    Mockito.when(
            apartmentService.updateApartment(Mockito.eq((short) 3), Mockito.any(Apartment.class)))
        .thenReturn(apt);

    mockMvc
        .perform(
            put("/api/apartment/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"id\":3,\"apartmentType\":\"ONE_BEDROOM\",\"capacity\":3,\"floor\":1,\"description\":\"Suite reformada\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.apartmentType").value("ONE_BEDROOM"))
        .andExpect(jsonPath("$.capacity").value(3))
        .andExpect(jsonPath("$.floor").value(1))
        .andExpect(jsonPath("$.description").value("Suite reformada"));
  }

  @Test
  @DisplayName("GET /api/apartments/types should return apartment types")
  void testGetApartmentTypes() throws Exception {
    Mockito.when(apartmentService.getApartmentTypes()).thenReturn(ApartmentType.values());

    mockMvc
        .perform(get("/api/apartments/types"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(ApartmentType.values().length)));
  }

  @Test
  @DisplayName("GET /api/apartments/available should return availability list")
  void testGetAvailableApartments() throws Exception {
    ApartmentAvailabilityDTO dto = new ApartmentAvailabilityDTO((short) 1, true);

    Mockito.when(
            apartmentService.getAvailabilityList(
                Mockito.eq(LocalDate.parse("2025-10-01")),
                Mockito.eq(LocalDate.parse("2025-10-10")),
                Mockito.eq(ApartmentType.TWO_BEDROOM)))
        .thenReturn(Collections.singletonList(dto));

    mockMvc
        .perform(
            get("/api/apartments/available")
                .param("type", "TWO_BEDROOM")
                .param("startDate", "2025-10-01")
                .param("endDate", "2025-10-10"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].apartment.id").value(1))
        .andExpect(jsonPath("$[0].available").value(true));
  }

  @Test
  @DisplayName("GET /api/apartments/ids should return all ids")
  void testGetAllIds() throws Exception {
    Mockito.when(apartmentService.getAllIds()).thenReturn(Arrays.asList((short) 1, (short) 2));

    mockMvc
        .perform(get("/api/apartments/ids"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").value(1))
        .andExpect(jsonPath("$[1]").value(2));
  }
}
