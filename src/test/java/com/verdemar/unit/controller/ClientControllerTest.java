package com.verdemar.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.verdemar.controller.ClientController;
import com.verdemar.domain.Client;
import com.verdemar.service.ClientService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client sampleClient;

    @BeforeEach
    void setUp() {
        sampleClient = new Client();
        sampleClient.setId(1);
        sampleClient.setName("Elio");
        sampleClient.setLastName("Costa");
        sampleClient.setPhone("123456789");
        sampleClient.setEmail("elio@example.com");
    }

    @Test
    void testGetAllClients() throws Exception {
        Mockito.when(clientService.getAllClients()).thenReturn(List.of(sampleClient));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Elio"));
    }

    @Test
    void testGetClientById() throws Exception {
        Mockito.when(clientService.getClientById(1)).thenReturn(sampleClient);

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("elio@example.com"));
    }

    @Test
    void testCreateClient() throws Exception {
        Mockito.when(clientService.createClient(Mockito.any(Client.class)))
                .thenReturn(sampleClient);

        mockMvc.perform(post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Elio"));
    }

    @Test
    void testUpdateClient() throws Exception {
        sampleClient.setLastName("Costa Updated");

        Mockito.when(clientService.updateClient(Mockito.eq(1), Mockito.any(Client.class)))
                .thenReturn(sampleClient);

        mockMvc.perform(put("/api/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Costa Updated"));
    }

    @Test
    void testDeleteClient() throws Exception {
        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }
}
