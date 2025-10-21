package com.verdemar.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.verdemar.controller.ClientController;
import com.verdemar.domain.Client;
import com.verdemar.service.client.ClientService;
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

/** Unit tests for ClientController using MockMvc. */
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ClientController.class)
class ClientControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private ClientService clientService;

  @Test
  @DisplayName("GET /api/clients should return all clients")
  void testGetAllClients() throws Exception {
    Client client = new Client(1, "Elio", "Amado", "666777888", "elio@correo.com");

    Mockito.when(clientService.getAllClients()).thenReturn(List.of(client));

    mockMvc
        .perform(get("/api/client"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1))
        .andExpect(jsonPath("$[0].name").value("Elio"))
        .andExpect(jsonPath("$[0].lastName").value("Amado"))
        .andExpect(jsonPath("$[0].phone").value("666777888"))
        .andExpect(jsonPath("$[0].email").value("elio@correo.com"));
  }

  @Test
  @DisplayName("GET /api/clients/{id} should return client by id")
  void testGetClientById() throws Exception {
    Client client = new Client(2, "Maria", "Costa", "999888777", "maria@correo.com");

    Mockito.when(clientService.getClientById(2)).thenReturn(client);

    mockMvc
        .perform(get("/api/client/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("Maria"))
        .andExpect(jsonPath("$.lastName").value("Costa"))
        .andExpect(jsonPath("$.phone").value("999888777"))
        .andExpect(jsonPath("$.email").value("maria@correo.com"));
  }

  @Test
  @DisplayName("POST /api/clients should create client")
  void testCreateClient() throws Exception {
    Client client = new Client(3, "Pedro", "Martínez", "123456789", "pedro@correo.com");

    Mockito.when(clientService.createClient(any(Client.class))).thenReturn(client);

    mockMvc
        .perform(
            post("/api/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\":\"Pedro\",\"lastName\":\"Martínez\",\"phone\":\"123456789\",\"email\":\"pedro@correo.com\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("Pedro"))
        .andExpect(jsonPath("$.lastName").value("Martínez"))
        .andExpect(jsonPath("$.phone").value("123456789"))
        .andExpect(jsonPath("$.email").value("pedro@correo.com"));
  }

  @Test
  @DisplayName("PUT /api/clients/{id} should update client")
  void testUpdateClient() throws Exception {
    Client client = new Client(4, "Ana", "López", "654987321", "ana@correo.com");

    Mockito.when(clientService.updateClient(eq(4), any(Client.class))).thenReturn(client);

    mockMvc
        .perform(
            put("/api/client/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"name\":\"Ana\",\"lastName\":\"López\",\"phone\":\"654987321\",\"email\":\"ana@correo.com\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(4))
        .andExpect(jsonPath("$.name").value("Ana"))
        .andExpect(jsonPath("$.lastName").value("López"))
        .andExpect(jsonPath("$.phone").value("654987321"))
        .andExpect(jsonPath("$.email").value("ana@correo.com"));
  }

  @Test
  @DisplayName("DELETE /api/client/{id} should delete client")
  void testDeleteClient() throws Exception {
    mockMvc.perform(delete("/api/clients/5")).andExpect(status().isNoContent());

    Mockito.verify(clientService).deleteClient(5);
  }
}
