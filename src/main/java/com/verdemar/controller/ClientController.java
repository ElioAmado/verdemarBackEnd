package com.verdemar.controller;

import com.verdemar.domain.Client;
import com.verdemar.service.client.ClientService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

  @Autowired private ClientService clientService;

  // Gets

  // Obtener todos los clientes
  @GetMapping
  public ResponseEntity<List<Client>> getAllClients() {
    List<Client> clients = clientService.getAllClients();
    return ResponseEntity.ok(clients);
  }

  // Obtener un cliente por ID
  @GetMapping("/{id}")
  public ResponseEntity<Client> getClientById(@PathVariable Integer id) {
    Client client = clientService.getClientById(id);
    return ResponseEntity.ok(client);
  }

  // Posts

  // Crear un nuevo cliente
  @PostMapping
  public ResponseEntity<Client> createClient(@RequestBody Client client) {
    Client createdClient = clientService.createClient(client);
    return ResponseEntity.ok(createdClient);
  }

  // Puts

  // Actualizar un cliente
  @PutMapping("/{id}")
  public ResponseEntity<Client> updateClient(@PathVariable Integer id, @RequestBody Client client) {
    Client updatedClient = clientService.updateClient(id, client);
    return ResponseEntity.ok(updatedClient);
  }

  // Deletes

  // Eliminar un cliente
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteClient(@PathVariable Integer id) {
    clientService.deleteClient(id);
    return ResponseEntity.noContent().build();
  }
}
