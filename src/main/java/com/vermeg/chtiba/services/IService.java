package com.vermeg.chtiba.services;

import java.util.List;
import java.util.Optional;
import com.vermeg.chtiba.entities.Client;

public interface IService {
    Client createClient(Client paramClient);

    List<Client> getAllClients();

    Optional<Client> getClientById(Long paramLong);

    void deleteClientById(Long paramLong);
}
