package com.example.demo.service;

import com.example.demo.entities.Client;

import java.util.List;

public interface ClienteService {

    List<Client> getAllClientes();

    Client getClienteById(Long id);

    Client saveCliente(Client cliente);

    void deleteCliente(Long id);
}
