// package com.verdemar.unit.controller;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.eq;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import com.verdemar.controller.PriceController;
// import com.verdemar.domain.apartment.Apartment;
// import com.verdemar.domain.price.Price;
// import com.verdemar.domain.price.PriceRequestDTO;
// import com.verdemar.domain.price.PriceResponseDTO;
// import com.verdemar.service.price.PriceService;
// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.util.List;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;

// /** Unit tests for PriceController using MockMvc. */
// // @AutoConfigureMockMvc(addFilters = false)
// // @WebMvcTest(PriceController.class)
// class PriceControllerTest {

//   @Autowired private MockMvc mockMvc;

//   @MockBean private PriceService priceService;

//   @Test
//   @DisplayName("GET /api/prices should return all prices")
//   void testGetAllPrices() throws Exception {
//     Apartment apt = new Apartment();
//     apt.setId((short) 1);

//     Price price = new Price(apt, LocalDate.of(2025, 1, 1), new BigDecimal("150.00"));

//     Mockito.when(priceService.getAllPrices()).thenReturn(List.of(price));

//     mockMvc
//         .perform(get("/api/price"))
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$[0].apartment.id").value(1))
//         .andExpect(jsonPath("$[0].date").value("2025-01-01"))
//         .andExpect(jsonPath("$[0].price").value(150.00));
//   }

//   @Test
//   @DisplayName("GET /api/prices/dto should return all prices DTO")
//   void testGetAllPricesDto() throws Exception {
//     PriceResponseDTO dto =
//         new PriceResponseDTO((short) 2, LocalDate.of(2025, 2, 1), new BigDecimal("200.00"));

//     Mockito.when(priceService.getAllPriceDto()).thenReturn(List.of(dto));

//     mockMvc.perform(get("/api/prices/dto")).andExpect(jsonPath("$[0].price").value(200.00));
//   }

//   @Test
//   @DisplayName("GET /api/prices/{apartment}/{date} should return a price by id")
//   void testGetPriceById() throws Exception {
//     Apartment apt = new Apartment();
//     apt.setId((short) 3);

//     Price price = new Price(apt, LocalDate.of(2025, 3, 1), new BigDecimal("180.00"));

//     Mockito.when(priceService.getPriceById((short) 3, LocalDate.of(2025, 3, 1))).thenReturn(price);

//     mockMvc
//         .perform(get("/api/price/3/2025-03-01"))
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$.apartment.id").value(3))
//         .andExpect(jsonPath("$.date").value("2025-03-01"))
//         .andExpect(jsonPath("$.price").value(180.00));
//   }

//   @Test
//   @DisplayName("PUT /api/prices/{apartment}/{date} should update a price")
//   void testUpdatePrice() throws Exception {
//     Apartment apt = new Apartment();
//     apt.setId((short) 4);

//     Price updatedPrice = new Price(apt, LocalDate.of(2025, 4, 1), new BigDecimal("220.00"));

//     Mockito.when(
//             priceService.updatePrice(
//                 eq((short) 4), eq(LocalDate.of(2025, 4, 1)), any(PriceRequestDTO.class)))
//         .thenReturn(updatedPrice);

//     mockMvc
//         .perform(
//             put("/api/price/4/2025-04-01")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"price\":220.00}"))
//         .andExpect(status().isOk())
//         .andExpect(jsonPath("$.apartment.id").value(4))
//         .andExpect(jsonPath("$.date").value("2025-04-01"))
//         .andExpect(jsonPath("$.price").value(220.00));
//   }
// }
