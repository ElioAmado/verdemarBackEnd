package com.verdemar.service.client;

import com.verdemar.domain.Client;
import com.verdemar.repository.ClientRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {

  @Autowired private ClientRepository clientRepository;

  // Devuelve todos los clientes
  @Override
  public List<Client> getAllClients() {
    return clientRepository.findAll();
  }

  // Devuelve un cliente por su ID
  @Override
  public Client getClientById(Integer id) {
    Optional<Client> client = clientRepository.findById(id);
    return client.orElseThrow(() -> new RuntimeException("Client not found with id: " + id));
  }

  // Crea un nuevo cliente
  @Override
  public Client createClient(Client client) {
    return clientRepository.save(client);
  }

  // Actualiza un cliente existente
  @Override
  public Client updateClient(Integer id, Client client) {
    if (!clientRepository.existsById(id)) {
      throw new RuntimeException("Client not found with id: " + id);
    }
    client.setId(id); // Asegura que se mantiene el mismo ID
    return clientRepository.save(client);
  }

  // Elimina un cliente por su ID
  @Override
  public void deleteClient(Integer id) {
    if (!clientRepository.existsById(id)) {
      throw new RuntimeException("Client not found with id: " + id);
    }
    clientRepository.deleteById(id);
  }
}
