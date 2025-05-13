package com.verdemar.verdemar.service;

import com.verdemar.verdemar.domain.Client;
import java.util.List;

public interface ClientService {

    List<Client> getAllClients();

    Client getClientById(Integer id);

    Client createClient(Client client);

    Client updateClient(Integer id, Client client);

    void deleteClient(Integer id);
}
